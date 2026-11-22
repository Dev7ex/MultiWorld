package com.dev7ex.multiworld.user;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.user.WorldUser;
import com.dev7ex.multiworld.api.user.WorldUserLocation;
import com.dev7ex.multiworld.api.user.WorldUserRepository;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static final int SCHEMA_VERSION = 2;
    private static final int SQLITE_IOERR = 10;
    private static final int SQLITE_CORRUPT = 11;
    private static final int SQLITE_CANTOPEN = 14;
    private static final int SQLITE_NOTADB = 26;
    private static final long REOPEN_INTERVAL_MILLIS = TimeUnit.MINUTES.toMillis(5);
    private static final long FAILURE_LOG_INTERVAL_MILLIS = TimeUnit.MINUTES.toMillis(1);
    private static final long SHUTDOWN_TIMEOUT_SECONDS = 10;

    private static final String CREATE_WORLD_TABLE = "CREATE TABLE IF NOT EXISTS world (id INTEGER PRIMARY KEY, world_key TEXT NOT NULL UNIQUE)";
    private static final String CREATE_LOCATION_TABLE = "CREATE TABLE IF NOT EXISTS user_location (unique_id TEXT NOT NULL, world_id INTEGER NOT NULL, "
            + "x REAL NOT NULL, y REAL NOT NULL, z REAL NOT NULL, yaw REAL NOT NULL, pitch REAL NOT NULL, PRIMARY KEY (unique_id, world_id)) WITHOUT ROWID";
    private static final String SELECT_WORLDS = "SELECT id, world_key FROM world";
    private static final String INSERT_WORLD = "INSERT INTO world (world_key) VALUES (?)";
    private static final String SELECT_LOCATIONS = "SELECT world_id, x, y, z, yaw, pitch FROM user_location WHERE unique_id = ?";
    private static final String SAVE_LOCATION = "INSERT INTO user_location (unique_id, world_id, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?) "
            + "ON CONFLICT (unique_id, world_id) DO UPDATE SET x = excluded.x, y = excluded.y, z = excluded.z, yaw = excluded.yaw, pitch = excluded.pitch";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS user (unique_id TEXT PRIMARY KEY, name TEXT NOT NULL, first_login INTEGER NOT NULL, last_login INTEGER NOT NULL)";
    private static final String SELECT_USER = "SELECT name, first_login, last_login FROM user WHERE unique_id = ?";
    private static final String SAVE_USER = "INSERT INTO user (unique_id, name, first_login, last_login) VALUES (?, ?, ?, ?) "
            + "ON CONFLICT (unique_id) DO UPDATE SET name = excluded.name, last_login = excluded.last_login";

    private final MultiWorldApi multiWorldApi;
    private final File file;
    private final ExecutorService executor;
    private final Map<NamespacedKey, Integer> worldIds = new HashMap<>();
    private final Map<Integer, NamespacedKey> worldKeys = new HashMap<>();
    private Connection connection;
    private boolean worldsLoaded;
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

                final WorldUser user;

                try (final ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return Optional.empty();
                    }
                    user = this.multiWorldApi.getUserFactory().create(uniqueId, resultSet.getString(1), resultSet.getLong(2), resultSet.getLong(3));
                }
                this.loadLocations(user);
                return Optional.of(user);
            }
        });
    }

    @Override
    public @NotNull CompletableFuture<Void> save(@NotNull final WorldUser user) {
        final UserSnapshot snapshot = new UserSnapshot(user, false);
        return this.submit(() -> this.write(List.of(snapshot), List.of()));
    }

    @Override
    public @NotNull CompletableFuture<Void> saveLocation(@NotNull final UUID uniqueId, @NotNull final WorldUserLocation location) {
        final LocationSnapshot snapshot = new LocationSnapshot(uniqueId.toString(), location);
        return this.submit(() -> this.write(List.of(), List.of(snapshot)));
    }

    @Override
    public @NotNull CompletableFuture<Void> saveAll(@NotNull final Collection<? extends WorldUser> users) {
        final List<UserSnapshot> snapshots = users.stream()
                .map(user -> new UserSnapshot(user, true))
                .toList();
        final List<LocationSnapshot> locations = snapshots.stream()
                .flatMap(snapshot -> snapshot.getLocations().stream())
                .toList();

        return this.submit(() -> this.write(snapshots, locations));
    }

    private Void write(@NotNull final List<UserSnapshot> snapshots, @NotNull final List<LocationSnapshot> locations) throws Exception {
        final Connection connection = this.getConnection();
        connection.setAutoCommit(false);

        try (final PreparedStatement statement = connection.prepareStatement(DefaultWorldUserRepository.SAVE_USER);
             final PreparedStatement locationStatement = connection.prepareStatement(DefaultWorldUserRepository.SAVE_LOCATION)) {

            for (final UserSnapshot snapshot : snapshots) {
                statement.setString(1, snapshot.getUniqueId());
                statement.setString(2, snapshot.getName());
                statement.setLong(3, snapshot.getFirstLogin());
                statement.setLong(4, snapshot.getLastLogin());
                statement.executeUpdate();
            }

            for (final LocationSnapshot snapshot : locations) {
                final WorldUserLocation location = snapshot.getLocation();
                locationStatement.setString(1, snapshot.getUniqueId());
                locationStatement.setInt(2, this.getWorldId(connection, location.getWorldKey()));
                locationStatement.setDouble(3, location.getX());
                locationStatement.setDouble(4, location.getY());
                locationStatement.setDouble(5, location.getZ());
                locationStatement.setFloat(6, location.getYaw());
                locationStatement.setFloat(7, location.getPitch());
                locationStatement.executeUpdate();
            }
            connection.commit();

        } catch (final Exception exception) {
            this.worldIds.clear();
            this.worldKeys.clear();
            this.worldsLoaded = false;

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
                statement.executeUpdate(DefaultWorldUserRepository.CREATE_WORLD_TABLE);
                statement.executeUpdate(DefaultWorldUserRepository.CREATE_LOCATION_TABLE);
                statement.executeUpdate("PRAGMA user_version = " + DefaultWorldUserRepository.SCHEMA_VERSION);
            }

        } catch (final SQLException exception) {
            connection.close();
            throw exception;
        }
        return connection;
    }

    private void loadLocations(@NotNull final WorldUser user) throws SQLException {
        final Connection connection = this.getConnection();

        if (!this.worldsLoaded) {
            this.loadWorlds(connection);
        }

        try (final PreparedStatement statement = connection.prepareStatement(DefaultWorldUserRepository.SELECT_LOCATIONS)) {
            statement.setString(1, user.getUniqueId().toString());

            try (final ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    final NamespacedKey worldKey = this.worldKeys.get(resultSet.getInt(1));

                    if (worldKey == null) {
                        continue;
                    }
                    user.setLastLocation(new DefaultWorldUserLocation(worldKey, resultSet.getDouble(2), resultSet.getDouble(3), resultSet.getDouble(4),
                            resultSet.getFloat(5), resultSet.getFloat(6)));
                }
            }
        }
    }

    private void loadWorlds(@NotNull final Connection connection) throws SQLException {
        try (final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery(DefaultWorldUserRepository.SELECT_WORLDS)) {

            while (resultSet.next()) {
                final NamespacedKey worldKey = NamespacedKey.fromString(resultSet.getString(2));

                if (worldKey == null) {
                    continue;
                }
                this.worldIds.put(worldKey, resultSet.getInt(1));
                this.worldKeys.put(resultSet.getInt(1), worldKey);
            }
        }
        this.worldsLoaded = true;
    }

    private int getWorldId(@NotNull final Connection connection, @NotNull final NamespacedKey worldKey) throws SQLException {
        if (!this.worldsLoaded) {
            this.loadWorlds(connection);
        }
        final Integer cachedId = this.worldIds.get(worldKey);

        if (cachedId != null) {
            return cachedId;
        }

        try (final PreparedStatement statement = connection.prepareStatement(DefaultWorldUserRepository.INSERT_WORLD, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, worldKey.toString());
            statement.executeUpdate();

            try (final ResultSet resultSet = statement.getGeneratedKeys()) {
                if (!resultSet.next()) {
                    throw new SQLException("No id was generated for World " + worldKey);
                }
                final int id = resultSet.getInt(1);
                this.worldIds.put(worldKey, id);
                this.worldKeys.put(id, worldKey);
                return id;
            }
        }
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
        this.worldIds.clear();
        this.worldKeys.clear();
        this.worldsLoaded = false;
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

    @Getter(AccessLevel.PRIVATE)
    private static class UserSnapshot {

        private final String uniqueId;
        private final String name;
        private final long firstLogin;
        private final long lastLogin;
        private final List<LocationSnapshot> locations;

        private UserSnapshot(@NotNull final WorldUser user, final boolean withLocations) {
            this.uniqueId = user.getUniqueId().toString();
            this.name = user.getName();
            this.firstLogin = user.getFirstLogin();
            this.lastLogin = user.getLastLogin();
            this.locations = withLocations
                    ? user.getLastLocations().values().stream().map(location -> new LocationSnapshot(this.uniqueId, location)).toList()
                    : List.of();
        }

    }

    @Getter(AccessLevel.PRIVATE)
    private static class LocationSnapshot {

        private final String uniqueId;
        private final WorldUserLocation location;

        private LocationSnapshot(@NotNull final String uniqueId, @NotNull final WorldUserLocation location) {
            this.uniqueId = uniqueId;
            this.location = location;
        }

    }

}
