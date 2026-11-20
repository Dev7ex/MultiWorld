package com.dev7ex.multiworld.api.world.backup;

import com.dev7ex.multiworld.api.world.WorldContext;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * @author Dev7ex
 * @since 19.06.2025
 */
public interface WorldBackupManager {

    @NotNull
    CompletableFuture<Void> restore(@NotNull final Audience audience, @NotNull final WorldContext worldContext, @NotNull final WorldBackup backup);

    @NotNull
    default CompletableFuture<Void> restore(@NotNull final WorldContext worldContext, @NotNull final WorldBackup backup) {
        return this.restore(Bukkit.getConsoleSender(), worldContext, backup);
    }

}