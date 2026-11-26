package com.dev7ex.multiworld.api.scheduler;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * @author MGG
 * @since 21.11.2026
 */
public interface WorldScheduler {

    void runTask(@NotNull final Runnable task);

    void runTaskAsynchronously(@NotNull final Runnable task);

    void runGlobalLater(final long delayTicks, @NotNull final Runnable task);

    void runOnEntity(@NotNull final Entity entity, @NotNull final Runnable task);

    boolean isRegionized();

}
