package com.dev7ex.multiworld.api.world;

import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldManager {

    void cloneWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final String clonedName);

    void createBackup(@NotNull final String creatorName, @NotNull final String name);

    void createWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final WorldType type);

    void createWorld(@NotNull final String creatorName, @NotNull final String name, final long seed);

    void createWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final String generator);

    void deleteWorld(@NotNull final String creatorName, @NotNull final String name);

    void importWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final WorldType type);

    void loadWorld(@NotNull final String creatorName, @NotNull final String name);

    void unloadWorld(@NotNull final String creatorName, @NotNull final String name);

    WorldConfiguration<?> getConfiguration();

    WorldProvider<?> getProvider();

}
