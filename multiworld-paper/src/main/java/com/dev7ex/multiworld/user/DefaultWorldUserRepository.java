package com.dev7ex.multiworld.user;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.user.WorldUser;
import com.dev7ex.multiworld.api.user.WorldUserRepository;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @author MGG
 * @since 22.11.2026
 */
public class DefaultWorldUserRepository implements WorldUserRepository {

    private static final String DRIVER_CLASS = "org.sqlite.JDBC";
    private static final String FILE_NAME = "user.db";
    private static final String THREAD_NAME = "MultiWorld-UserStorage";
    private static final int SCHEMA_VERSION = 1;
    private static final int SQLITE_IOERR = 10;
    private static final int SQLITE_CORRUPT = 11;
    private static final int SQLITE_CANTOPEN = 14;
    private static final int SQLITE_NOTADB = 26;
    private static final long REOPEN_INTERVAL_MILLIS = TimeUnit.MINUTES.toMillis(5);
    private static final long FAILURE_LOG_INTERVAL_MILLIS = TimeUnit.MINUTES.toMillis(1);
    private static final long SHUTDOWN_TIMEOUT_SECONDS = 10;

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS user (unique_id TEXT PRIMARY KEY, name TEXT NOT NULL, first_login INTEGER NOT NULL, last_login INTEGER NOT NULL)";
    private static final String SELECT_USER = "SELECT name, first_login, last_login FROM user WHERE unique_id = ?";
    private static final String SAVE_USER = "INSERT INTO user (unique_id, name, first_login, last_login) VALUES (?, ?, ?, ?) "
            + "ON CONFLICT (unique_id) DO UPDATE SET name = excluded.name, last_login = excluded.last_login";

    private final MultiWorldApi multiWorldApi;
    private final File file;
    private final ExecutorService executor;
    private Connection connection;
    private long lastOpenFailure;
    private long lastFailureLog;
    private int suppressedFailures;

    public DefaultWorldUserRepository(@NotNull final MultiWorldApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
        this.file = new File(multiWorldApi.getDataFolder(), DefaultWorldUserRepository.FILE_NAME);
        this.executor = Executors.newSingleThreadExecutor(runnable -> {
            final Thread thread = new Thread(runnable, DefaultWorldUserRepository.THREAD_NAME);
            thread.setDaemon(true);
            return thread;
        });
    }

    public void open() {
        this.submit(() -> {
            this.getConnection();
            return null;
        });
    }

    public void close() {
        this.submit(() -> {
            this.closeConnection();
            return null;
        });
        this.executor.shutdown();

        try {
            if (!this.executor.awaitTermination(DefaultWorldUserRepository.SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                this.executor.shutdownNow();
                this.multiWorldApi.getLogger().warning("User storage didn't finish writing in time, some player data may not be saved");
            }

        } catch (final InterruptedException exception) {
            this.executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public @NotNull CompletableFuture<Optional<WorldUser>> find(@NotNull final UUID uniqueId) {
        return this.submit(() -> {
            try (final PreparedStatement statement = this.getConnection().prepareStatement(DefaultWorldUserRepository.SELECT_USER)) {
                statement.setString(1, uniqueId.toString());

                try (final ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return Optional.empty();
                    }
                    return Optional.of(this.multiWorldApi.getUserFactory().create(uniqueId, resultSet.getString(1), resultSet.getLong(2), resultSet.getLong(3)));
                }
            }
        });
    }

    @Override
    public @NotNull CompletableFuture<Void> save(@NotNull final WorldUser user) {
        return this.saveAll(List.of(user));
    }

    @Override
    public @NotNull CompletableFuture<Void> saveAll(@NotNull final Collection<? extends WorldUser> users) {
        final List<UserSnapshot> snapshots = users.stream()
                .map(user -> new UserSnapshot(user.getUniqueId().toString(), user.getName(), user.getFirstLogin(), user.getLastLogin()))
                .toList();

        return this.submit(() -> {
            final Connection connection = this.getConnection();
            connection.setAutoCommit(false);

            try (final PreparedStatement statement = connection.prepareStatement(DefaultWorldUserRepository.SAVE_USER)) {
                for (final UserSnapshot snapshot : snapshots) {
                    statement.setString(1, snapshot.uniqueId());
                    statement.setString(2, snapshot.name());
                    statement.setLong(3, snapshot.firstLogin());
                    statement.setLong(4, snapshot.lastLogin());
                    statement.executeUpdate();
                }
                connection.commit();

            } catch (final Exception exception) {
                try {
                    connection.rollback();

                } catch (final SQLException rollbackException) {
                    exception.addSuppressed(rollbackException);
                }
                throw exception;

            } finally {
                if (!connection.isClosed()) {
                    connection.setAutoCommit(true);
                }
            }
            return null;
        });
    }

    private <T> CompletableFuture<T> submit(@NotNull final Callable<T> task) {
        final CompletableFuture<T> future = new CompletableFuture<>();

        try {
            this.executor.execute(() -> {
                if (future.isDone()) {
                    return;
                }

                try {
                    future.complete(task.call());

                } catch (final Exception exception) {
                    this.handleFailure(exception);
                    future.completeExceptionally(exception);
                }
            });

        } catch (final RejectedExecutionException exception) {
            future.completeExceptionally(exception);
        }
        return future;
    }

    private Connection getConnection() throws SQLException {
        if ((this.connection != null) && (!this.connection.isClosed())) {
            return this.connection;
        }

        if ((this.lastOpenFailure != 0) && ((System.currentTimeMillis() - this.lastOpenFailure) < DefaultWorldUserRepository.REOPEN_INTERVAL_MILLIS)) {
            throw new SQLException("User storage is unavailable, next attempt in a few minutes");
        }

        try {
            try {
                this.connection = this.openConnection();

            } catch (final SQLException exception) {
                if (!this.isCorrupt(exception)) {
                    throw exception;
                }
                this.moveCorruptFile();
                this.connection = this.openConnection();
            }

        } catch (final SQLException exception) {
            this.lastOpenFailure = System.currentTimeMillis();
            throw exception;

        } catch (final ReflectiveOperationException | LinkageError exception) {
            this.lastOpenFailure = System.currentTimeMillis();
            this.multiWorldApi.getLogger().warning("SQLite isn't available, player data won't be saved. If the native library can't be extracted, set -Dorg.sqlite.tmpdir to a writable directory");
            throw new SQLException("SQLite driver can't be loaded", exception);
        }
        this.lastOpenFailure = 0;
        return this.connection;
    }

    private Connection openConnection() throws SQLException, ReflectiveOperationException {
        final Driver driver = (Driver) Class.forName(DefaultWorldUserRepository.DRIVER_CLASS).getDeclaredConstructor().newInstance();
        final Connection connection = driver.connect("jdbc:sqlite:" + this.file.getAbsolutePath(), new Properties());

        if (connection == null) {
            throw new SQLException("SQLite driver refused the connection");
        }

        try (final Statement statement = connection.createStatement()) {
            statement.executeUpdate("PRAGMA busy_timeout = 1000");

            try (final ResultSet resultSet = statement.executeQuery("PRAGMA journal_mode = WAL")) {
                if ((!resultSet.next()) || (!"wal".equalsIgnoreCase(resultSet.getString(1)))) {
                    this.multiWorldApi.getLogger().info("User storage runs without WAL journal on this file system");
                }
            }
            statement.executeUpdate("PRAGMA synchronous = NORMAL");

            final int version;

            try (final ResultSet resultSet = statement.executeQuery("PRAGMA user_version")) {
                version = resultSet.next() ? resultSet.getInt(1) : 0;
            }

            if (version < DefaultWorldUserRepository.SCHEMA_VERSION) {
                statement.executeUpdate(DefaultWorldUserRepository.CREATE_TABLE);
                statement.executeUpdate("PRAGMA user_version = " + DefaultWorldUserRepository.SCHEMA_VERSION);
            }

        } catch (final SQLException exception) {
            connection.close();
            throw exception;
        }
        return connection;
    }

    private boolean isCorrupt(@NotNull final SQLException exception) {
        final int code = exception.getErrorCode() & 0xFF;
        return (code == DefaultWorldUserRepository.SQLITE_CORRUPT) || (code == DefaultWorldUserRepository.SQLITE_NOTADB);
    }

    private boolean isFatal(@NotNull final SQLException exception) {
        final int code = exception.getErrorCode() & 0xFF;
        return (this.isCorrupt(exception)) || (code == DefaultWorldUserRepository.SQLITE_IOERR) || (code == DefaultWorldUserRepository.SQLITE_CANTOPEN);
    }

    private void moveCorruptFile() throws SQLException {
        final String suffix = ".corrupt-" + System.currentTimeMillis();

        for (final String extension : List.of("-wal", "-shm", "")) {
            final File source = new File(this.file.getPath() + extension);

            if ((source.exists()) && (!source.renameTo(new File(this.file.getPath() + suffix + extension)))) {
                throw new SQLException("Can't move the broken user storage " + source);
            }
        }
        this.multiWorldApi.getLogger().warning(String.format("User storage %s was broken, moved it to %s and started a new one", this.file.getName(), this.file.getName() + suffix));
    }

    private void closeConnection() {
        if (this.connection == null) {
            return;
        }

        try {
            this.connection.close();

        } catch (final SQLException exception) {
            this.multiWorldApi.getLogger().log(Level.WARNING, "Can't close the user storage", exception);
        }
        this.connection = null;
    }

    private void handleFailure(@NotNull final Exception exception) {
        if ((exception instanceof final SQLException sqlException) && (this.isFatal(sqlException))) {
            this.closeConnection();

            if (this.isCorrupt(sqlException)) {
                try {
                    this.moveCorruptFile();

                } catch (final SQLException moveException) {
                    exception.addSuppressed(moveException);
                }
            }
        }
        final long now = System.currentTimeMillis();

        if ((now - this.lastFailureLog) < DefaultWorldUserRepository.FAILURE_LOG_INTERVAL_MILLIS) {
            this.suppressedFailures++;
            return;
        }

        if (this.suppressedFailures > 0) {
            this.multiWorldApi.getLogger().warning(String.format("User storage failed [%d] more time(s) in the last minute", this.suppressedFailures));
        }
        this.multiWorldApi.getLogger().log(Level.WARNING, "User storage operation failed", exception);
        this.lastFailureLog = now;
        this.suppressedFailures = 0;
    }

    private record UserSnapshot(@NotNull String uniqueId, @NotNull String name, long firstLogin, long lastLogin) {}

}
