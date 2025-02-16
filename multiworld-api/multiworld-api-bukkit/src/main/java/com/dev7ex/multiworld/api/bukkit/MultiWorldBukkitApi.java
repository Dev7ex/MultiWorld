package com.dev7ex.multiworld.api.bukkit;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.bukkit.translation.BukkitTranslationProvider;
import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUserProvider;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldConfiguration;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldManager;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldProvider;
import com.dev7ex.multiworld.api.bukkit.world.generator.BukkitWorldGeneratorProvider;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for interacting with the MultiWorld API in a Bukkit environment.
 * Provides access to various functionalities and data related to MultiWorld for Bukkit servers.
 *
 * <p>This interface extends {@link MultiWorldApi} to provide Bukkit-specific functionality,
 * including world management, configuration, and command handling.</p>
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface MultiWorldBukkitApi extends MultiWorldApi {

    /**
     * Retrieves the configuration settings for MultiWorld in a Bukkit environment.
     *
     * @return The {@link MultiWorldBukkitApiConfiguration} instance containing the configuration settings.
     */
    @Override
    @NotNull
    MultiWorldBukkitApiConfiguration getConfiguration();

    /**
     * Retrieves the command handler for MultiWorld commands in a Bukkit environment.
     *
     * @return The {@link BukkitCommand} instance handling MultiWorld commands.
     */
    @NotNull
    BukkitCommand getWorldCommand();

    /**
     * Retrieves the console command sender.
     *
     * @return The {@link ConsoleCommandSender} instance representing the Bukkit console sender.
     */
    ConsoleCommandSender getConsoleSender();

    /**
     * Retrieves the translation provider for Bukkit environments.
     *
     * @return The {@link BukkitTranslationProvider} instance handling translations.
     */
    @Override
    @NotNull
    BukkitTranslationProvider getTranslationProvider();

    /**
     * Retrieves the provider for managing users in Bukkit worlds.
     *
     * @return The {@link BukkitWorldUserProvider} instance managing Bukkit world users.
     */
    @Override
    @NotNull
    BukkitWorldUserProvider getUserProvider();

    /**
     * Retrieves the configuration settings for a Bukkit world.
     *
     * @return The {@link BukkitWorldConfiguration} instance representing Bukkit world settings.
     */
    @Override
    @NotNull
    BukkitWorldConfiguration getWorldConfiguration();

    /**
     * Retrieves the provider for generating Bukkit worlds.
     *
     * @return The {@link BukkitWorldGeneratorProvider} responsible for Bukkit world generation.
     */
    @Override
    @NotNull
    BukkitWorldGeneratorProvider getWorldGeneratorProvider();

    /**
     * Retrieves the manager for managing Bukkit worlds.
     *
     * @return The {@link BukkitWorldManager} instance handling Bukkit world management.
     */
    @Override
    @NotNull
    BukkitWorldManager getWorldManager();

    /**
     * Retrieves the provider for accessing information about Bukkit worlds.
     *
     * @return The {@link BukkitWorldProvider} instance providing Bukkit world-related data.
     */
    @Override
    @NotNull
    BukkitWorldProvider getWorldProvider();

}