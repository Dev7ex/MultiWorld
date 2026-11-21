package com.dev7ex.multiworld.scheduler;

import com.dev7ex.multiworld.api.scheduler.WorldScheduler;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author MGG
 * @since 21.11.2026
 */
public class BukkitWorldScheduler implements WorldScheduler {

    private final Plugin plugin;

    public BukkitWorldScheduler(@NotNull final Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runTask(@NotNull final Runnable task) {
        this.plugin.getServer().getScheduler().runTask(this.plugin, task);
    }

    @Override
    public void runTaskAsynchronously(@NotNull final Runnable task) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, task);
    }

    @Override
    public boolean isRegionized() {
        return false;
    }

}
