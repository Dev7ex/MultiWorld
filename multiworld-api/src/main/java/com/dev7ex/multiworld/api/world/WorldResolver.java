package com.dev7ex.multiworld.api.world;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author MGG
 * @since 20.11.2026
 */
public interface WorldResolver {

    @NotNull
    File resolveDirectory(@NotNull final WorldContext context);

    boolean isDimensionLayout();

    boolean isLevelDirectory(@NotNull final File directory);

}
