
package com.dev7ex.multiworld.api.scheduler;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a platform-independent scheduler for executing tasks
 * across traditional and region-based server environments.
 *
 * <p>Provides a unified abstraction for synchronous, asynchronous,
 * delayed and entity-specific task execution.</p>
 *
 * @author MGG
 * @since 21.11.2026
 */
public interface WorldScheduler {

    /**
     * Schedules the specified task for execution on the main server
     * thread or the global region, depending on the server environment.
     *
     * <p>On region-based servers, this method must not be used for
     * operations requiring ownership of a specific region or entity.</p>
     *
     * @param task the task to execute
     */
    void runTask(@NotNull final Runnable task);

    /**
     * Schedules the specified task for asynchronous execution.
     *
     * <p>The task is executed independently of the server tick process
     * and must not access non-thread-safe server APIs.</p>
     *
     * @param task the task to execute asynchronously
     */
    void runTaskAsynchronously(@NotNull final Runnable task);

    /**
     * Schedules the specified task for execution on the main server
     * thread or the global region after the given delay.
     *
     * @param delayTicks the delay before execution, measured in ticks
     * @param task       the task to execute
     */
    void runGlobalLater(final long delayTicks, @NotNull final Runnable task);

    /**
     * Schedules the specified task for execution on the thread
     * responsible for the given entity.
     *
     * <p>On traditional servers, the task is executed on the main
     * server thread. On region-based servers, the task is executed
     * on the region owning the entity.</p>
     *
     * <p>Depending on the underlying scheduler implementation,
     * the task may not be executed if the entity has been removed.</p>
     *
     * @param entity the entity associated with the task
     * @param task   the task to execute
     */
    void runOnEntity(@NotNull final Entity entity, @NotNull final Runnable task);

    /**
     * Returns whether the server uses region-based multithreading.
     *
     * @return {@code true} if the server uses region-based multithreading,
     * {@code false} otherwise
     */
    boolean isRegionized();

}
