package com.dev7ex.multiworld.scheduler;

import com.dev7ex.multiworld.api.scheduler.WorldScheduler;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author MGG
 * @since 21.11.2026
 */
public class PaperWorldScheduler implements WorldScheduler {

    private final Plugin plugin;
    private final boolean regionized;

    public PaperWorldScheduler(@NotNull final Plugin plugin, final boolean regionized) {
        this.plugin = plugin;
        this.regionized = regionized;
    }

    @Override
    public void runTask(@NotNull final Runnable task) {
        this.plugin.getServer().getGlobalRegionScheduler().execute(this.plugin, task);
    }

    @Override
    public void runTaskAsynchronously(@NotNull final Runnable task) {
        this.plugin.getServer().getAsyncScheduler().runNow(this.plugin, scheduledTask -> task.run());
    }

    @Override
    public void runGlobalLater(final long delayTicks, @NotNull final Runnable task) {
        this.plugin.getServer().getGlobalRegionScheduler().runDelayed(this.plugin, scheduledTask -> task.run(), Math.max(1L, delayTicks));
    }

    @Override
    public void runOnEntity(@NotNull final Entity entity, @NotNull final Runnable task) {
        entity.getScheduler().run(this.plugin, scheduledTask -> task.run(), null);
    }

    @Override
    public boolean isRegionized() {
        return this.regionized;
    }

}
