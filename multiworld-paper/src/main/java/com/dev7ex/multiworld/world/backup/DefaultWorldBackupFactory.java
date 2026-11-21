
package com.dev7ex.multiworld.world.backup;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.event.world.backup.WorldBackupClearEvent;
import com.dev7ex.multiworld.api.event.world.backup.WorldBackupCreateEvent;
import com.dev7ex.multiworld.api.event.world.backup.WorldBackupDeleteEvent;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.backup.WorldBackup;
import com.dev7ex.multiworld.api.world.backup.WorldBackupFactory;
import com.dev7ex.multiworld.util.ZipUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.logging.Level;

/**
 * @author Dev7ex
 * @since 19.06.2025
 */
public class DefaultWorldBackupFactory implements WorldBackupFactory {

    private static final String REGION_FILE_IO_CLASS = "ca.spottedleaf.moonrise.patches.chunk_system.io.MoonriseRegionFileIO";
    private static final long WRITE_TIMEOUT_MILLIS = TimeUnit.MINUTES.toMillis(2);
    private static final long WRITE_POLL_MILLIS = 250L;

    private final MultiWorldApi multiWorldApi;
    private final File backupDirectory;
    private final Map<UUID, Boolean> pausedAutoSaves = new ConcurrentHashMap<>();

    public DefaultWorldBackupFactory(@NotNull final MultiWorldApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
        this.backupDirectory = multiWorldApi.getBackupDirectory();
    }

    @Override
    public @NotNull CompletableFuture<WorldBackup> create(@NotNull final Audience audience, @NotNull final WorldContext context) {
        final AtomicReference<Path> worldFolder = new AtomicReference<>();
        final AtomicReference<UUID> worldId = new AtomicReference<>();
        final AtomicReference<BooleanSupplier> pendingWrites = new AtomicReference<>();

        return this.sync(() -> {
            if (this.multiWorldApi.getBackupRegistry().isLocked(context)) {
                audience.sendMessage(this.multiWorldApi.getLocalizationRegistry()
                        .getComponent(audience, WorldLocalizationEntry.MESSAGE_WORLD_BACKUP_IN_PROGRESS)
                        .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(context.getName())));
                return null;
            }
            final File directory = this.multiWorldApi.getWorldResolver().resolveDirectory(context);

            if (this.multiWorldApi.getWorldResolver().isLevelDirectory(directory)) {
                audience.sendMessage(Component.text("The main world contains all dimensions on this server version and can't be backed up", NamedTextColor.RED));
                return null;
            }
            worldFolder.set(directory.toPath());

            final long timestamp = System.currentTimeMillis();
            final WorldBackup backup = new DefaultWorldBackup(context, timestamp);
            final WorldBackupCreateEvent event = new WorldBackupCreateEvent(context, audience, backup);

            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                if (event.isNotifyOnCancel()) {
                    audience.sendMessage(this.multiWorldApi.getLocalizationRegistry()
                            .getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_CREATE_CANCELLED)
                            .replaceText(builder -> builder.match("%world_name%").replacement(context.getName())));
                }
                return null;
            }

            this.multiWorldApi.getBackupRegistry().lock(context);

            if (context.isLoaded()) {
                final World world = context.getWorld();
                if (world != null) {
                    worldId.set(world.getUID());
                    this.pauseAutoSave(world);

                    try {
                        pendingWrites.set(this.saveWorld(world));

                    } catch (final RuntimeException | Error exception) {
                        this.resumeAutoSave(world.getUID());
                        this.multiWorldApi.getBackupRegistry().unlock(context);
                        throw exception;
                    }
                }
            }

            return event;
        }).thenCompose(event -> {
            if (event == null) {
                return CompletableFuture.completedFuture(null);
            }

            return CompletableFuture.supplyAsync(() -> {
                try {
                    final Path worldBackupDirectory = this.backupDirectory.toPath().resolve(context.getName());
                    if (!Files.isDirectory(worldFolder.get())) {
                        audience.sendMessage(Component.text("§cDas Weltverzeichnis wurde nicht gefunden: " + worldFolder.get()));
                        return null;
                    }

                    Files.createDirectories(worldBackupDirectory);
                    this.awaitWrites(pendingWrites.get(), context);

                    final WorldBackup backup = event.getWorldBackup();

                    ZipUtil.zipWorld(worldFolder.get().toFile(), worldBackupDirectory.toFile(), backup.getTimestamp());

                    this.multiWorldApi.getBackupRegistry().register(event.getContext(), backup);

                    audience.sendMessage(this.multiWorldApi.getLocalizationRegistry()
                            .getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_CREATE_FINISHING)
                            .replaceText(builder -> builder.match("%world_name%").replacement(context.getName())));

                    return backup;
                } catch (final Exception e) {
                    this.multiWorldApi.getLogger().log(Level.SEVERE, String.format("Can't create Backup for World %s", context.getName()), e);
                    audience.sendMessage(Component.text("§cFehler beim Erstellen des Backups: " + e.getMessage()));
                    throw new CompletionException(e);
                }
            }).whenComplete((backup, throwable) -> {
                pendingWrites.set(null);
                this.finishBackup(context, worldId.get());
            });
        });
    }

    public void resumeAutoSaves() {
        for (final UUID worldId : List.copyOf(this.pausedAutoSaves.keySet())) {
            this.resumeAutoSave(worldId);
        }
    }

    private void pauseAutoSave(@NotNull final World world) {
        this.pausedAutoSaves.putIfAbsent(world.getUID(), world.isAutoSave());
        world.setAutoSave(false);
    }

    private void resumeAutoSave(@NotNull final UUID worldId) {
        final Boolean autoSave = this.pausedAutoSaves.remove(worldId);

        if (autoSave == null) {
            return;
        }
        final World world = Bukkit.getWorld(worldId);

        if (world != null) {
            world.setAutoSave(autoSave);
        }
    }

    private void finishBackup(@NotNull final WorldContext context, @Nullable final UUID worldId) {
        if (worldId == null) {
            this.multiWorldApi.getBackupRegistry().unlock(context);
            return;
        }

        try {
            this.sync(() -> {
                try {
                    this.resumeAutoSave(worldId);

                } finally {
                    this.multiWorldApi.getBackupRegistry().unlock(context);
                }
                return null;
            });

        } catch (final IllegalPluginAccessException exception) {
            this.multiWorldApi.getBackupRegistry().unlock(context);
        }
    }

    @Nullable
    private BooleanSupplier saveWorld(@NotNull final World world) {
        final BooleanSupplier pendingWrites = this.findPendingWrites(world);

        if (pendingWrites != null) {
            world.save();
            return pendingWrites;
        }

        this.multiWorldApi.getLogger().info(String.format("Region write tracking isn't available, flushing World %s on the main thread", world.getName()));

        try {
            world.save(true);

        } catch (final NoSuchMethodError exception) {
            world.save();
        }
        return null;
    }

    @Nullable
    private BooleanSupplier findPendingWrites(@NotNull final World world) {
        try {
            final ClassLoader classLoader = world.getClass().getClassLoader();
            final Object level = world.getClass().getMethod("getHandle").invoke(world);
            final Class<?> regionFileIoClass = Class.forName(DefaultWorldBackupFactory.REGION_FILE_IO_CLASS, false, classLoader);
            final Class<?> regionFileTypeClass = Class.forName(DefaultWorldBackupFactory.REGION_FILE_IO_CLASS + "$RegionFileType", false, classLoader);
            final Class<?> controllerClass = Class.forName(DefaultWorldBackupFactory.REGION_FILE_IO_CLASS + "$RegionDataController", false, classLoader);
            final Method controllerMethod = Arrays.stream(regionFileIoClass.getMethods())
                    .filter(method -> (method.getName().equals("getControllerFor")) && (method.getParameterCount() == 2))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchMethodException("getControllerFor"));
            final Method hasTasksMethod = controllerClass.getMethod("hasTasks");
            final List<Object> controllers = new ArrayList<>();

            for (final Object regionFileType : regionFileTypeClass.getEnumConstants()) {
                controllers.add(controllerMethod.invoke(null, level, regionFileType));
            }

            final BooleanSupplier pendingWrites = () -> {
                for (final Object controller : controllers) {
                    try {
                        if ((boolean) hasTasksMethod.invoke(controller)) {
                            return true;
                        }

                    } catch (final IllegalAccessException | InvocationTargetException exception) {
                        throw new IllegalStateException("Can't check pending region writes", exception);
                    }
                }
                return false;
            };
            pendingWrites.getAsBoolean();
            return pendingWrites;

        } catch (final ReflectiveOperationException | RuntimeException | LinkageError exception) {
            return null;
        }
    }

    private void awaitWrites(@Nullable final BooleanSupplier pendingWrites, @NotNull final WorldContext context) {
        if (pendingWrites == null) {
            return;
        }
        final long deadline = System.currentTimeMillis() + DefaultWorldBackupFactory.WRITE_TIMEOUT_MILLIS;

        while (pendingWrites.getAsBoolean()) {
            if (System.currentTimeMillis() > deadline) {
                throw new IllegalStateException(String.format("World %s is still writing region files, backup aborted", context.getName()));
            }

            try {
                Thread.sleep(DefaultWorldBackupFactory.WRITE_POLL_MILLIS);

            } catch (final InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(String.format("Backup of World %s was interrupted", context.getName()), exception);
            }
        }
    }

    @Override
    public @NotNull CompletableFuture<Integer> clear(@NotNull final Audience audience, @NotNull final WorldContext context) {
        return this.sync(() -> {
            final WorldBackupClearEvent event = new WorldBackupClearEvent(context, audience);

            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                if (event.isNotifyOnCancel()) {
                    audience.sendMessage(this.multiWorldApi.getLocalizationRegistry()
                            .getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_CLEAR_CANCELLED)
                            .replaceText(builder -> builder.match("%world_name%").replacement(context.getName())));
                }
                return false;
            }

            audience.sendMessage(this.multiWorldApi.getLocalizationRegistry()
                    .getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_CLEAR_STARTING)
                    .replaceText(builder -> builder.match("%world_name%").replacement(context.getName())));

            return true;
        }).thenCompose(allowed -> {
            if (Boolean.FALSE.equals(allowed)) {
                return CompletableFuture.completedFuture(0);
            }

            return CompletableFuture.supplyAsync(() -> {
                final File worldBackupDirectory = new File(this.backupDirectory, context.getName());
                final File[] files = worldBackupDirectory.listFiles();

                if (files == null) {
                    return 0;
                }

                int deleted = 0;

                for (final File file : files) {
                    if (!file.isFile() || !file.getName().endsWith(".zip")) {
                        continue;
                    }

                    if (!file.delete()) {
                        this.multiWorldApi.getLogger().warning("Could not delete backup file: " + file.getAbsolutePath());
                        continue;
                    }

                    deleted++;
                }

                return deleted;
            }).thenCompose(deleted -> this.sync(() -> {
                this.multiWorldApi.getBackupRegistry().getBackups(context)
                        .stream()
                        .filter(backup -> !Files.exists(backup.getPath()))
                        .toList()
                        .forEach(backup -> this.multiWorldApi.getBackupRegistry().unregister(context, backup));

                audience.sendMessage(this.multiWorldApi.getLocalizationRegistry()
                        .getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_CLEAR_FINISHING)
                        .replaceText(builder -> builder.match("%world_name%").replacement(context.getName())));

                return deleted;
            }));
        });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> deleteLast(@NotNull final Audience audience, @NotNull final WorldContext context) {
        final Optional<WorldBackup> backup = this.multiWorldApi.getBackupRegistry().findLatest(context);

        return backup.map(worldBackup -> this.delete(audience, worldBackup)).orElseGet(() -> CompletableFuture.completedFuture(false));

    }

    @Override
    public @NotNull CompletableFuture<Boolean> delete(@NotNull final Audience audience, @NotNull final WorldBackup backup) {
        return this.sync(() -> {
            final WorldContext context = backup.getContext();
            final WorldBackupDeleteEvent event = new WorldBackupDeleteEvent(context, audience, backup);

            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                if (event.isNotifyOnCancel()) {
                    audience.sendMessage(this.multiWorldApi.getLocalizationRegistry()
                            .getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_DELETE_CANCELLED)
                            .replaceText(builder -> builder.match("%world_name%").replacement(context.getName())));
                }
                return null;
            }

            audience.sendMessage(this.multiWorldApi.getLocalizationRegistry()
                    .getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_DELETE_STARTING)
                    .replaceText(builder -> builder.match("%world_backup_name%").replacement(backup.getFormattedName(this.multiWorldApi.getLocalizationRegistry().getDateTimeFormat(audience))))
                    .replaceText(builder -> builder.match("%world_name%").replacement(context.getDisplayName())));

            return context;
        }).thenCompose(context -> {
            if (context == null) {
                return CompletableFuture.completedFuture(false);
            }

            return CompletableFuture.supplyAsync(() -> {
                try {
                    return Files.deleteIfExists(backup.getPath());
                } catch (final Exception e) {
                    throw new CompletionException(e);
                }
            }).thenCompose(deleted -> this.sync(() -> {
                if (Boolean.TRUE.equals(deleted)) {
                    this.multiWorldApi.getBackupRegistry().unregister(context, backup);

                    audience.sendMessage(this.multiWorldApi.getLocalizationRegistry()
                            .getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_DELETE_FINISHING)
                            .replaceText(builder -> builder.match("%world_backup_name%").replacement(backup.getFormattedName(this.multiWorldApi.getLocalizationRegistry().getDateTimeFormat(audience))))
                            .replaceText(builder -> builder.match("%world_name%").replacement(context.getDisplayName())));
                }

                return deleted;
            }));
        });
    }

    private <T> CompletableFuture<T> sync(@NotNull final java.util.function.Supplier<T> supplier) {
        final CompletableFuture<T> future = new CompletableFuture<>();

        if (Bukkit.isPrimaryThread()) {
            try {
                future.complete(supplier.get());
            } catch (final Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        } else {
            this.multiWorldApi.getScheduler().runTask(() -> {
                try {
                    future.complete(supplier.get());
                } catch (final Throwable throwable) {
                    future.completeExceptionally(throwable);
                }
            });
        }

        return future;
    }

}
