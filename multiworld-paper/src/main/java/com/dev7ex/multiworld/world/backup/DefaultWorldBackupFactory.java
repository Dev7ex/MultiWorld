
package com.dev7ex.multiworld.world.backup;

import com.dev7ex.multiworld.MultiWorldPlugin;
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
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * @author Dev7ex
 * @since 19.06.2025
 */
public class DefaultWorldBackupFactory implements WorldBackupFactory {

    private final MultiWorldApi multiWorldApi;
    private final File backupDirectory;

    public DefaultWorldBackupFactory(@NotNull final MultiWorldApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
        this.backupDirectory = multiWorldApi.getBackupDirectory();
    }

    @Override
    public @NotNull CompletableFuture<WorldBackup> create(@NotNull final Audience audience, @NotNull final WorldContext context) {
        return this.sync(() -> {
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

            if (context.isLoaded()) {
                final World world = context.getWorld();
                if (world != null) {
                    world.save();
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
                    final Path worldFolder = Path.of(context.getDirectory().getPath());

                    if (!Files.isDirectory(worldFolder)) {
                        audience.sendMessage(Component.text("§cDas Weltverzeichnis wurde nicht gefunden: " + worldFolder));
                        return null;
                    }

                    Files.createDirectories(worldBackupDirectory);

                    final WorldBackup backup = event.getWorldBackup();

                    ZipUtil.zipWorld(worldFolder.toFile(), worldBackupDirectory.toFile(), backup.getTimestamp());

                    this.multiWorldApi.getBackupRegistry().register(event.getContext(), backup);

                    audience.sendMessage(this.multiWorldApi.getLocalizationRegistry()
                            .getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_CREATE_FINISHING)
                            .replaceText(builder -> builder.match("%world_name%").replacement(context.getName())));

                    return backup;
                } catch (final Exception e) {
                    audience.sendMessage(Component.text("§cFehler beim Erstellen des Backups: " + e.getMessage()));
                    throw new CompletionException(e);
                }
            });
        });
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
            Bukkit.getScheduler().runTask(MultiWorldPlugin.getInstance(), () -> {
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
