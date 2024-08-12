package com.dev7ex.multiworld.api.world;

import org.jetbrains.annotations.NotNull;

/**
 * The interface defining operations for managing worlds within a Minecraft server.
 * This includes actions like creating, deleting, loading, unloading, and cloning worlds,
 * as well as accessing world configuration and provider instances.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldManager {

    /**
     * Clones an existing world with the specified name and creates a new world with the cloned data.
     *
     * @param creatorName The name of the creator initiating the action.
     * @param name        The name of the existing world to clone.
     * @param clonedName  The name for the newly cloned world.
     */
    void cloneWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final String clonedName);

    /**
     * Creates a backup of the specified world.
     *
     * @param creatorName The name of the creator initiating the action.
     * @param name        The name of the world to create a backup for.
     */
    void createBackup(@NotNull final String creatorName, @NotNull final String name);

    /**
     * Creates a new world with the specified name and type.
     *
     * @param creatorName The name of the creator initiating the action.
     * @param name        The name for the new world.
     * @param type        The type of world to create.
     */
    void createWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final WorldType type);

    /**
     * Creates a new world with the specified name and seed.
     *
     * @param creatorName The name of the creator initiating the action.
     * @param name        The name for the new world.
     * @param seed        The seed value for world generation.
     */
    void createWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final WorldEnvironment environment, final long seed);

    /**
     * Creates a new world with the specified name and generator.
     *
     * @param creatorName The name of the creator initiating the action.
     * @param name        The name for the new world.
     * @param generator   The generator type for world generation.
     */
    void createWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final WorldEnvironment environment, @NotNull final String generator);

    /**
     * Deletes the world with the specified name.
     *
     * @param creatorName The name of the creator initiating the action.
     * @param name        The name of the world to delete.
     */
    void deleteWorld(@NotNull final String creatorName, @NotNull final String name);

    /**
     * Imports a world with the specified name and generator into the server.
     *
     * @param creatorName The name of the creator initiating the action.
     * @param name        The name for the imported world.
     * @param generator   The name for the generator.
     */
    void importWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final WorldEnvironment environment, @NotNull final String generator);

    /**
     * Imports a world with the specified name and type into the server.
     *
     * @param creatorName The name of the creator initiating the action.
     * @param name        The name for the imported world.
     * @param type        The type of world being imported.
     */
    void importWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final WorldEnvironment environment, @NotNull final WorldType type);

    /**
     * Loads the world with the specified name.
     *
     * @param creatorName The name of the creator initiating the action.
     * @param name        The name of the world to load.
     */
    void loadWorld(@NotNull final String creatorName, @NotNull final String name);

    /**
     * Unloads the world with the specified name.
     *
     * @param creatorName The name of the creator initiating the action.
     * @param name        The name of the world to unload.
     */
    void unloadWorld(@NotNull final String creatorName, @NotNull final String name);

    /**
     * Retrieves the configuration manager associated with worlds.
     *
     * @return The WorldConfiguration instance.
     */
    WorldConfiguration<?> getConfiguration();

    /**
     * Retrieves the provider manager associated with worlds.
     *
     * @return The WorldProvider instance.
     */
    WorldProvider<?> getProvider();

}