
package com.dev7ex.multiworld.scheduler;

import com.dev7ex.multiworld.api.scheduler.WorldScheduler;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Paper-based implementation of the {@link WorldScheduler} supporting
 * both traditional and regionized server environments.
 *
 * <p>Provides access to global, asynchronous and entity-specific
 * schedulers through the Paper scheduling API.</p>
 *
 * @author MGG
 * @since 21.11.2026
 */
@Getter(AccessLevel.PUBLIC)
public class PaperWorldScheduler implements WorldScheduler {

    private final Plugin plugin;
    private final boolean regionized;

    /**
     * Constructs a new Paper world scheduler.
     *
     * @param plugin     the plugin responsible for scheduling tasks
     * @param regionized whether the server uses regionized multithreading
     */
    public PaperWorldScheduler(@NotNull final Plugin plugin, final boolean regionized) {
        this.plugin = plugin;
        this.regionized = regionized;
    }

    /**
     * Schedules the specified task for execution on the global region.
     *
     * <p>On regionized servers, this method must not be used for
     * operations requiring ownership of a specific region or entity.</p>
     *
     * @param task the task to execute
     */
    @Override
    public void runTask(@NotNull final Runnable task) {
        this.plugin.getServer().getGlobalRegionScheduler().execute(this.plugin, task);
    }

    /**
     * Schedules the specified task for asynchronous execution.
     *
     * <p>The task is executed independently of the server tick process
     * and must not access non-thread-safe server APIs.</p>
     *
     * @param task the task to execute asynchronously
     */
    @Override
    public void runTaskAsynchronously(@NotNull final Runnable task) {
        this.plugin.getServer().getAsyncScheduler().runNow(this.plugin, scheduledTask -> task.run());
    }

    /**
     * Schedules the specified task for execution on the global region
     * after the given delay.
     *
     * <p>The delay is clamped to a minimum of one tick.</p>
     *
     * @param delayTicks the delay before execution, measured in ticks
     * @param task       the task to execute
     */
    @Override
    public void runGlobalLater(final long delayTicks, @NotNull final Runnable task) {
        this.plugin.getServer().getGlobalRegionScheduler().runDelayed(this.plugin, scheduledTask -> task.run(), Math.max(1L, delayTicks));
    }

    /**
     * Schedules the specified task for execution on the region owning
     * the given entity.
     *
     * <p>The scheduler follows the entity across region and world
     * boundaries. The task will not be executed if the entity scheduler
     * is retired before execution.</p>
     *
     * @param entity the entity associated with the task
     * @param task   the task to execute
     */
    @Override
    public void runOnEntity(@NotNull final Entity entity, @NotNull final Runnable task) {
        entity.getScheduler().run(this.plugin, scheduledTask -> task.run(), null);
    }

}
