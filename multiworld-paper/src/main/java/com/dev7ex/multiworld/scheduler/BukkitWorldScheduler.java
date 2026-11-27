package com.dev7ex.multiworld.scheduler;

import com.dev7ex.multiworld.api.scheduler.WorldScheduler;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Bukkit-based implementation of the {@link WorldScheduler} for
 * traditional server environments using a single main server thread.
 *
 * <p>Provides synchronous, asynchronous and delayed task execution
 * through the Bukkit scheduling API.</p>
 *
 * @author MGG
 * @since 21.11.2026
 */
public class BukkitWorldScheduler implements WorldScheduler {

    private final Plugin plugin;

    /**
     * Constructs a new Bukkit world scheduler.
     *
     * @param plugin the plugin responsible for scheduling tasks
     */
    public BukkitWorldScheduler(@NotNull final Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Schedules the specified task for execution on the main server thread.
     *
     * @param task the task to execute
     */
    @Override
    public void runTask(@NotNull final Runnable task) {
        this.plugin.getServer().getScheduler().runTask(this.plugin, task);
    }

    /**
     * Schedules the specified task for asynchronous execution.
     *
     * <p>The task is executed independently of the main server thread
     * and must not access non-thread-safe server APIs.</p>
     *
     * @param task the task to execute asynchronously
     */
    @Override
    public void runTaskAsynchronously(@NotNull final Runnable task) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, task);
    }

    /**
     * Schedules the specified task for execution on the main server thread
     * after the given delay.
     *
     * @param delayTicks the delay before execution, measured in ticks
     * @param task       the task to execute
     */
    @Override
    public void runGlobalLater(final long delayTicks, @NotNull final Runnable task) {
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, task, delayTicks);
    }

    /**
     * Schedules the specified entity-related task for execution
     * on the main server thread.
     *
     * <p>Traditional Bukkit servers do not require entity-specific
     * scheduling. The entity parameter is retained for compatibility
     * with the {@link WorldScheduler} interface and is not used
     * by this implementation.</p>
     *
     * @param entity the entity associated with the task
     * @param task   the task to execute
     */
    @Override
    public void runOnEntity(@NotNull final Entity entity, @NotNull final Runnable task) {
        this.plugin.getServer().getScheduler().runTask(this.plugin, task);
    }

    /**
     * Returns whether the server uses region-based multithreading.
     *
     * @return always {@code false}
     */
    @Override
    public boolean isRegionized() {
        return false;
    }

}
