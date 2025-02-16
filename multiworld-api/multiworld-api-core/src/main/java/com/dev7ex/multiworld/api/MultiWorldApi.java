package com.dev7ex.multiworld.api;

import com.dev7ex.multiworld.api.translation.TranslationProvider;
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
 * <p>This interface serves as the main entry point for retrieving configurations, providers,
 * and managers related to the MultiWorld system.</p>
 *
 * @author Dev7ex
 * @since 20.12.2022
 */
public interface MultiWorldApi {

    /**
     * Retrieves the configuration settings for MultiWorld.
     *
     * @return The {@link MultiWorldApiConfiguration} instance containing the configuration settings.
     */
    @NotNull
    MultiWorldApiConfiguration getConfiguration();

    /**
     * Retrieves the translation provider.
     *
     * @return The {@link TranslationProvider} used for handling translations.
     */
    @NotNull
    TranslationProvider<?> getTranslationProvider();

    /**
     * Retrieves the user provider for worlds.
     *
     * @return The {@link WorldUserProvider} instance responsible for managing world users.
     */
    @NotNull
    WorldUserProvider<?> getUserProvider();

    /**
     * Retrieves the provider for generating worlds.
     *
     * @return The {@link WorldGeneratorProvider} responsible for world generation.
     */
    @NotNull
    WorldGeneratorProvider<?, ?> getWorldGeneratorProvider();

    /**
     * Retrieves the configuration settings for a world.
     *
     * @return The {@link WorldConfiguration} instance representing world-specific settings.
     */
    @NotNull
    WorldConfiguration<?> getWorldConfiguration();

    /**
     * Retrieves the manager for managing worlds.
     *
     * @return The {@link WorldManager} instance responsible for managing worlds.
     */
    @NotNull
    WorldManager getWorldManager();

    /**
     * Retrieves the provider for accessing information about worlds.
     *
     * @return The {@link WorldProvider} instance offering world-related data access.
     */
    @NotNull
    WorldProvider<?> getWorldProvider();

    /**
     * Retrieves the folder where user-related data is stored.
     *
     * @return The {@link File} instance representing the user data folder, or null if unavailable.
     */
    @Nullable
    File getUserFolder();

    /**
     * Retrieves the folder where backups of worlds are stored.
     *
     * @return The {@link File} instance representing the backup folder, or null if unavailable.
     */
    @Nullable
    File getBackupFolder();

}
