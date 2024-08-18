package com.dev7ex.multiworld.api.bukkit;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUserProvider;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldConfiguration;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldManager;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldProvider;
import com.dev7ex.multiworld.api.bukkit.world.generator.BukkitWorldGeneratorProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for interacting with the MultiWorld API in a Bukkit environment.
 * Provides access to various functionalities and data related to MultiWorld for Bukkit servers.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface MultiWorldBukkitApi extends MultiWorldApi {

    @Override
    @NotNull MultiWorldBukkitApiConfiguration getConfiguration();

    /**
     * Retrieves the provider for managing users in Bukkit worlds.
     *
     * @return The provider for managing users in Bukkit worlds.
     */
    @Override
    @NotNull
    BukkitWorldUserProvider getUserProvider();

    /**
     * Retrieves the configuration settings for a Bukkit world.
     *
     * @return The configuration settings for a Bukkit world.
     */
    @Override
    @NotNull
    BukkitWorldConfiguration getWorldConfiguration();

    /**
     * Retrieves the provider for accessing information about Bukkit worlds.
     *
     * @return The provider for accessing information about Bukkit worlds.
     */
    @Override
    @NotNull
    BukkitWorldProvider getWorldProvider();

    /**
     * Retrieves the provider for generating Bukkit worlds.
     *
     * @return The provider for generating Bukkit worlds.
     */
    @Override
    @NotNull
    BukkitWorldGeneratorProvider getWorldGeneratorProvider();

    /**
     * Retrieves the manager for managing Bukkit worlds.
     *
     * @return The manager for managing Bukkit worlds.
     */
    @Override
    @NotNull
    BukkitWorldManager getWorldManager();

    /**
     * Retrieves the command handler for MultiWorld commands in a Bukkit environment.
     *
     * @return The command handler for MultiWorld commands in a Bukkit environment.
     */
    @NotNull
    BukkitCommand getWorldCommand();

}
