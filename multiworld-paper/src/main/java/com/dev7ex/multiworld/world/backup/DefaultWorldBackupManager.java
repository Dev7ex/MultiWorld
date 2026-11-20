package com.dev7ex.multiworld.world.backup;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.event.world.backup.WorldBackupRestoreEvent;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.WorldDirectory;
import com.dev7ex.multiworld.api.world.backup.WorldBackup;
import com.dev7ex.multiworld.api.world.backup.WorldBackupManager;
import com.dev7ex.multiworld.api.world.result.WorldDeletionResult;
import com.dev7ex.multiworld.util.ZipUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author Dev7ex
 * @since 19.06.2025
 */
public class DefaultWorldBackupManager implements WorldBackupManager {

    private final MultiWorldApi multiWorldApi;

    public DefaultWorldBackupManager(@NotNull final MultiWorldApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }

    @Override
    public @NotNull CompletableFuture<Void> restore(@NotNull final Audience audience, @NotNull final WorldContext worldContext, @NotNull final WorldBackup backup) {
        final CompletableFuture<Void> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTask(this.multiWorldApi, () -> {
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

            final File worldDirectory = this.multiWorldApi.getWorldResolver().resolveDirectory(worldContext);

            if (this.multiWorldApi.getWorldResolver().isLevelDirectory(worldDirectory)) {
                audience.sendMessage(Component.text("The main world contains all dimensions on this server version and can't be restored", NamedTextColor.RED));
                future.complete(null);
                return;
            }
            worldContext.setDirectory(new WorldDirectory(worldDirectory));

            if (this.multiWorldApi.getWorldFactory().delete(audience, worldContext) != WorldDeletionResult.SUCCESS) {
                future.complete(null);
                return;
            }

            CompletableFuture.runAsync(() -> {
                try {
                    ZipUtil.unzipWorld(backup.getPath().toFile(), worldDirectory);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }).whenComplete((result, throwable) -> {
                Bukkit.getScheduler().runTask(this.multiWorldApi, () -> {
                    if (throwable != null) {
                        audience.sendMessage(Component.text("§cFehler beim Wiederherstellen des Backups: " + throwable.getMessage())); // TODO: Language
                        future.completeExceptionally(throwable);
                        return;
                    }

                    this.multiWorldApi.getWorldFactory().create(audience, worldContext);

                    if (audience instanceof final Player player) {
                        player.teleport(worldContext.getWorld().getSpawnLocation());
                    }

                    audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_RESTORE_FINISHING)
                            .replaceText(builder -> builder.match("%world_backup_name%").replacement(backup.getFormattedName(this.multiWorldApi.getLocalizationRegistry().getDateTimeFormat(audience))))
                            .replaceText(builder -> builder.match("%world_name%").replacement(worldContext.getDisplayName())));

                    future.complete(null);
                });
            });
        });

        return future;
    }

}