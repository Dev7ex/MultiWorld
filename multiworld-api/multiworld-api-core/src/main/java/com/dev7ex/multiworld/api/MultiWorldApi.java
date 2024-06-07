package com.dev7ex.multiworld.api;

import com.dev7ex.multiworld.api.user.WorldUserProvider;
import com.dev7ex.multiworld.api.world.WorldConfiguration;
import com.dev7ex.multiworld.api.world.WorldManager;
import com.dev7ex.multiworld.api.world.WorldProvider;
import com.dev7ex.multiworld.api.world.generator.WorldGeneratorProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Interface for interacting with the MultiWorld API.
 * Provides access to various functionalities and data related to MultiWorld.
 *
 * @author Dev7ex
 * @since 20.12.2022
 */
public interface MultiWorldApi {

    /**
     * Retrieves the configuration settings for MultiWorld.
     *
     * @return The configuration settings for MultiWorld.
     */
    @NotNull
    MultiWorldApiConfiguration getConfiguration();

    /**
     * Retrieves the configuration settings for a world.
     *
     * @return The configuration settings for a world.
     */
    @NotNull
    WorldConfiguration<?> getWorldConfiguration();

    /**
     * Retrieves the provider for accessing information about worlds.
     *
     * @return The provider for accessing information about worlds.
     */
    @NotNull
    WorldProvider<?> getWorldProvider();

    /**
     * Retrieves the manager for managing worlds.
     *
     * @return The manager for managing worlds.
     */
    @NotNull
    WorldManager getWorldManager();

    /**
     * Retrieves the provider for generating worlds.
     *
     * @return The provider for generating worlds.
     */
    @NotNull
    WorldGeneratorProvider getWorldGeneratorProvider();

    /**
     * Retrieves the user provider for worlds.
     *
     * @return The user provider for worlds.
     */
    @NotNull
    WorldUserProvider getUserProvider();

    /**
     * Retrieves the folder where user-related data is stored.
     *
     * @return The folder where user-related data is stored.
     */
    @Nullable
    File getUserFolder();

    /**
     * Retrieves the folder where backups of worlds are stored.
     *
     * @return The folder where backups of worlds are stored.
     */
    @Nullable
    File getBackupFolder();

}