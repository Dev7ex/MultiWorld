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
public interface WorldBackupFactory {

    @NotNull
    CompletableFuture<WorldBackup> create(@NotNull final Audience audience, @NotNull final WorldContext context);

    @NotNull
    default CompletableFuture<WorldBackup> create(@NotNull final WorldContext context) {
        return this.create(Bukkit.getConsoleSender(), context);
    }

    @NotNull
    CompletableFuture<Integer> clear(@NotNull final Audience audience, @NotNull final WorldContext context);

    @NotNull
    default CompletableFuture<Integer> clear(@NotNull final WorldContext context) {
        return this.clear(Bukkit.getConsoleSender(), context);
    }

    @NotNull
    CompletableFuture<Boolean> deleteLast(@NotNull final Audience audience, @NotNull final WorldContext context);

    @NotNull
    default CompletableFuture<Boolean> deleteLast(@NotNull final WorldContext context) {
        return this.deleteLast(Bukkit.getConsoleSender(), context);
    }

    @NotNull
    CompletableFuture<Boolean> delete(@NotNull final Audience audience, @NotNull final WorldBackup backup);

    @NotNull
    default CompletableFuture<Boolean> delete(@NotNull final WorldBackup backup) {
        return this.delete(Bukkit.getConsoleSender(), backup);
    }

}