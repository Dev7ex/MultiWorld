package com.dev7ex.multiworld.api.bukkit.event;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApiConfiguration;
import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUserProvider;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldManager;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldProvider;
import com.dev7ex.multiworld.api.bukkit.world.generator.BukkitWorldGeneratorProvider;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for event listeners in the MultiWorld plugin.
 * Provides access to various components of the MultiWorld Bukkit API.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
public abstract class MultiWorldListener implements Listener {

    private final MultiWorldBukkitApi multiWorldApi;

    /**
     * Constructs a MultiWorldListener with the given MultiWorldBukkitApi.
     *
     * @param multiWorldApi The MultiWorldBukkitApi instance.
     */
    public MultiWorldListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }

    /**
     * Retrieves the MultiWorldApiConfiguration.
     *
     * @return The MultiWorldApiConfiguration.
     */
    public MultiWorldBukkitApiConfiguration getConfiguration() {
        return this.multiWorldApi.getConfiguration();
    }

    /**
     * Retrieves the prefix used for messages in the plugin.
     *
     * @return The plugin prefix.
     */
    public String getPrefix() {
        return this.multiWorldApi.getConfiguration().getPrefix();
    }

    /**
     * Retrieves the BukkitWorldProvider.
     *
     * @return The BukkitWorldProvider.
     */
    public BukkitWorldProvider getWorldProvider() {
        return this.multiWorldApi.getWorldProvider();
    }

    /**
     * Retrieves the BukkitWorldManager.
     *
     * @return The BukkitWorldManager.
     */
    public BukkitWorldManager getWorldManager() {
        return this.multiWorldApi.getWorldManager();
    }

    /**
     * Retrieves the BukkitWorldGeneratorProvider.
     *
     * @return The BukkitWorldGeneratorProvider.
     */
    public BukkitWorldGeneratorProvider getWorldGeneratorProvider() {
        return this.multiWorldApi.getWorldGeneratorProvider();
    }

    /**
     * Retrieves the WorldUserProvider.
     *
     * @return The WorldUserProvider.
     */
    protected BukkitWorldUserProvider getUserProvider() {
        return this.multiWorldApi.getUserProvider();
    }

}