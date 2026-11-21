package com.dev7ex.multiworld.api.scheduler;

import org.jetbrains.annotations.NotNull;

/**
 * @author MGG
 * @since 21.11.2026
 */
public interface WorldScheduler {

    void runTask(@NotNull final Runnable task);

    void runTaskAsynchronously(@NotNull final Runnable task);

    boolean isRegionized();

}
