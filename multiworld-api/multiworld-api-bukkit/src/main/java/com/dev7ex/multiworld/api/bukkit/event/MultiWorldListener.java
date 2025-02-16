package com.dev7ex.multiworld.api.bukkit.event;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApiConfiguration;
import com.dev7ex.multiworld.api.bukkit.translation.BukkitTranslationProvider;
import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUserProvider;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldConfiguration;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldManager;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldProvider;
import com.dev7ex.multiworld.api.bukkit.world.generator.BukkitWorldGeneratorProvider;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for event listeners in the MultiWorld plugin.
 * Provides access to various components of the MultiWorld Bukkit API.
 *
 * <p>This class serves as a foundation for event listeners, ensuring easy access to essential
 * MultiWorld components such as world management, user handling, and translation services.</p>
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
public abstract class MultiWorldListener implements Listener {

    private final MultiWorldBukkitApi multiWorldApi;

    /**
     * Constructs a MultiWorldListener with the given MultiWorldBukkitApi.
     *
     * @param multiWorldApi The {@link MultiWorldBukkitApi} instance providing access to the API.
     */
    public MultiWorldListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }

    /**
     * Retrieves the MultiWorld API configuration.
     *
     * @return The {@link MultiWorldBukkitApiConfiguration} instance.
     */
    public MultiWorldBukkitApiConfiguration getConfiguration() {
        return this.multiWorldApi.getConfiguration();
    }

    /**
     * Retrieves the prefix used for messages in the plugin.
     *
     * @return The plugin message prefix.
     */
    public String getPrefix() {
        return this.multiWorldApi.getConfiguration().getPrefix();
    }

    /**
     * Retrieves the Bukkit console command sender.
     *
     * @return The {@link ConsoleCommandSender} instance.
     */
    public ConsoleCommandSender getConsoleSender() {
        return this.multiWorldApi.getConsoleSender();
    }

    /**
     * Retrieves the translation provider for handling translations.
     *
     * @return The {@link BukkitTranslationProvider} instance.
     */
    public BukkitTranslationProvider getTranslationProvider() {
        return this.multiWorldApi.getTranslationProvider();
    }

    /**
     * Retrieves the provider for managing users in Bukkit worlds.
     *
     * @return The {@link BukkitWorldUserProvider} instance.
     */
    public BukkitWorldUserProvider getUserProvider() {
        return this.multiWorldApi.getUserProvider();
    }

    /**
     * Retrieves the provider for generating Bukkit worlds.
     *
     * @return The {@link BukkitWorldGeneratorProvider} instance.
     */
    public BukkitWorldGeneratorProvider getWorldGeneratorProvider() {
        return this.multiWorldApi.getWorldGeneratorProvider();
    }

    /**
     * Retrieves the configuration settings for a Bukkit world.
     *
     * @return The {@link BukkitWorldConfiguration} instance.
     */
    public BukkitWorldConfiguration getWorldConfiguration() {
        return this.multiWorldApi.getWorldConfiguration();
    }

    /**
     * Retrieves the world manager responsible for handling Bukkit worlds.
     *
     * @return The {@link BukkitWorldManager} instance.
     */
    public BukkitWorldManager getWorldManager() {
        return this.multiWorldApi.getWorldManager();
    }

    /**
     * Retrieves the provider for accessing information about Bukkit worlds.
     *
     * @return The {@link BukkitWorldProvider} instance.
     */
    public BukkitWorldProvider getWorldProvider() {
        return this.multiWorldApi.getWorldProvider();
    }

}
