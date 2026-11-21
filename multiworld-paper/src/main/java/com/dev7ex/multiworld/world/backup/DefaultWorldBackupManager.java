package com.dev7ex.multiworld.world.backup;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.event.world.backup.WorldBackupRestoreEvent;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.WorldDirectory;
import com.dev7ex.multiworld.api.world.WorldResolver;
import com.dev7ex.multiworld.api.world.backup.WorldBackup;
import com.dev7ex.multiworld.api.world.backup.WorldBackupManager;
import com.dev7ex.multiworld.api.world.backup.WorldBackupRegistry;
import com.dev7ex.multiworld.api.world.result.WorldLifecycleResult;
import com.dev7ex.multiworld.util.ZipUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * @author Dev7ex
 * @since 19.06.2025
 */
public class DefaultWorldBackupManager implements WorldBackupManager {

    private static final String LEVEL_FILE = "level.dat";

    private final MultiWorldApi multiWorldApi;

    public DefaultWorldBackupManager(@NotNull final MultiWorldApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }

    @Override
    public @NotNull CompletableFuture<Void> restore(@NotNull final Audience audience, @NotNull final WorldContext worldContext, @NotNull final WorldBackup backup) {
        final CompletableFuture<Void> future = new CompletableFuture<>();

        this.multiWorldApi.getScheduler().runTask(() -> {
            try {
                this.startRestore(audience, worldContext, backup, future);

            } catch (final RuntimeException exception) {
                this.multiWorldApi.getBackupRegistry().unlock(worldContext);
                this.multiWorldApi.getLogger().log(Level.SEVERE, String.format("Can't restore Backup %s for World %s", backup.getName(), worldContext.getName()), exception);
                future.completeExceptionally(exception);
            }
        });
        return future;
    }

    private void startRestore(@NotNull final Audience audience, @NotNull final WorldContext worldContext, @NotNull final WorldBackup backup,
                              @NotNull final CompletableFuture<Void> future) {
        final WorldBackupRegistry backupRegistry = this.multiWorldApi.getBackupRegistry();

        if (backupRegistry.isLocked(worldContext)) {
            audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, WorldLocalizationEntry.MESSAGE_WORLD_BACKUP_IN_PROGRESS)
                    .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(worldContext.getName())));
            future.complete(null);
            return;
        }
        audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_RESTORE_STARTING)
                .replaceText(builder -> builder.match("%world_backup_name%").replacement(backup.getFormattedName(this.multiWorldApi.getLocalizationRegistry().getDateTimeFormat(audience))))
                .replaceText(builder -> builder.match("%world_name%").replacement(worldContext.getDisplayName())));

        final WorldBackupRestoreEvent event = new WorldBackupRestoreEvent(worldContext, audience, backup);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            if (event.isNotifyOnCancel()) {
                audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_RESTORE_CANCELLED)
                        .replaceText(builder -> builder.match("%world_name%").replacement(worldContext.getName())));
            }
            future.complete(null);
            return;
        }
        final File backupFile = backup.getPath().toFile();

        if (!backupFile.isFile()) {
            this.completeRestore(audience, worldContext, backup, false, new IllegalStateException(String.format("Backup file not found: %s", backupFile)), future);
            return;
        }
        final WorldResolver resolver = this.multiWorldApi.getWorldResolver();
        final File worldDirectory = resolver.resolveDirectory(worldContext);

        if (resolver.isLevelDirectory(worldDirectory)) {
            audience.sendMessage(Component.text("The main world contains all dimensions on this server version and can't be restored", NamedTextColor.RED));
            future.complete(null);
            return;
        }
        worldContext.setDirectory(new WorldDirectory(worldDirectory));

        final boolean loaded = Bukkit.getWorld(worldContext.getKey()) != null;

        if ((loaded) && (this.multiWorldApi.getWorldManager().unload(audience, worldContext.getKey(), true) != WorldLifecycleResult.SUCCESS)) {
            this.completeRestore(audience, worldContext, backup, false, new IllegalStateException(String.format("World %s could not be unloaded", worldContext.getName())), future);
            return;
        }

        if (!resolver.isDeletable(worldDirectory)) {
            this.completeRestore(audience, worldContext, backup, loaded, new IllegalStateException(String.format("Unsafe World directory %s", worldDirectory)), future);
            return;
        }
        if (!backupRegistry.lock(worldContext)) {
            this.completeRestore(audience, worldContext, backup, loaded, new IllegalStateException(String.format("World %s is locked by another backup operation", worldContext.getName())), future);
            return;
        }

        this.multiWorldApi.getScheduler().runTaskAsynchronously(() -> {
            Throwable failure = null;

            try {
                this.replaceDirectory(backupFile, worldDirectory);

            } catch (final Throwable exception) {
                failure = exception;
            }
            final Throwable result = failure;

            try {
                this.multiWorldApi.getScheduler().runTask(() -> {
                    backupRegistry.unlock(worldContext);
                    this.completeRestore(audience, worldContext, backup, loaded, result, future);
                });

            } catch (final IllegalPluginAccessException exception) {
                backupRegistry.unlock(worldContext);
                future.completeExceptionally(exception);
            }
        });
    }

    private void completeRestore(@NotNull final Audience audience, @NotNull final WorldContext worldContext, @NotNull final WorldBackup backup,
                                 final boolean reload, @Nullable final Throwable restoreFailure, @NotNull final CompletableFuture<Void> future) {
        Throwable failure = restoreFailure;

        if ((reload) && (this.multiWorldApi.getWorldManager().load(audience, worldContext.getKey()) == WorldLifecycleResult.FAILED) && (failure == null)) {
            failure = new IllegalStateException(String.format("World %s could not be loaded after the restore", worldContext.getName()));
        }

        if (failure != null) {
            this.multiWorldApi.getLogger().log(Level.SEVERE, String.format("Can't restore Backup %s for World %s", backup.getName(), worldContext.getName()), failure);
            audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_RESTORE_FAILED)
                    .replaceText(builder -> builder.match("%world_backup_name%").replacement(backup.getFormattedName(this.multiWorldApi.getLocalizationRegistry().getDateTimeFormat(audience))))
                    .replaceText(builder -> builder.match("%world_name%").replacement(worldContext.getDisplayName())));
            future.completeExceptionally(failure);
            return;
        }
        final World world = Bukkit.getWorld(worldContext.getKey());

        if ((world != null) && (audience instanceof final Player player)) {
            player.teleportAsync(world.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        }

        audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_RESTORE_FINISHING)
                .replaceText(builder -> builder.match("%world_backup_name%").replacement(backup.getFormattedName(this.multiWorldApi.getLocalizationRegistry().getDateTimeFormat(audience))))
                .replaceText(builder -> builder.match("%world_name%").replacement(worldContext.getDisplayName())));
        future.complete(null);
    }

    private void replaceDirectory(@NotNull final File backupFile, @NotNull final File worldDirectory) throws IOException {
        final long timestamp = System.currentTimeMillis();
        final File parentDirectory = worldDirectory.getAbsoluteFile().getParentFile();
        final File stagingDirectory = new File(parentDirectory, String.format("%s.restoring-%d", worldDirectory.getName(), timestamp));
        final File previousDirectory = new File(parentDirectory, String.format("%s.previous-%d", worldDirectory.getName(), timestamp));

        try {
            ZipUtil.unzipWorld(backupFile, stagingDirectory);

            if ((!this.multiWorldApi.getWorldResolver().isDimensionLayout()) && (!new File(stagingDirectory, DefaultWorldBackupManager.LEVEL_FILE).isFile())) {
                throw new IOException(String.format("Backup %s does not contain %s", backupFile, DefaultWorldBackupManager.LEVEL_FILE));
            }

            if (worldDirectory.exists()) {
                Files.move(worldDirectory.toPath(), previousDirectory.toPath());
            }

            try {
                Files.move(stagingDirectory.toPath(), worldDirectory.toPath());

            } catch (final IOException exception) {
                if (previousDirectory.exists()) {
                    Files.move(previousDirectory.toPath(), worldDirectory.toPath());
                }
                throw exception;
            }

        } finally {
            if (stagingDirectory.exists()) {
                FileUtils.deleteQuietly(stagingDirectory);
            }
        }

        if (previousDirectory.exists()) {
            try {
                FileUtils.deleteDirectory(previousDirectory);

            } catch (final IOException exception) {
                this.multiWorldApi.getLogger().log(Level.WARNING, String.format("Can't remove previous World directory %s", previousDirectory), exception);
            }
        }
    }

}
