package com.dev7ex.multiworld.api;

import com.dev7ex.common.collect.map.ParsedMap;
import com.dev7ex.multiworld.api.world.WorldDefaultProperty;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Interface for accessing configuration settings of the MultiWorld plugin.
 * Provides methods to retrieve various configuration parameters.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface MultiWorldApiConfiguration {

    /**
     * Retrieves a string value from the configuration.
     *
     * @param path The path to the configuration value.
     * @return The string value at the specified path.
     */
    String getString(@NotNull final String path);

    /**
     * Retrieves a boolean value from the configuration.
     *
     * @param path The path to the configuration value.
     * @return The boolean value at the specified path.
     */
    boolean getBoolean(@NotNull final String path);

    /**
     * Retrieves an integer value from the configuration.
     *
     * @param path The path to the configuration value.
     * @return The integer value at the specified path.
     */
    int getInteger(@NotNull final String path);

    /**
     * Retrieves a message from the configuration.
     *
     * @param path The path to the configuration value.
     * @return The message at the specified path.
     */
    String getMessage(@NotNull final String path);

    /**
     * Retrieves the prefix used in messages from the configuration.
     *
     * @return The message prefix.
     */
    String getPrefix();

    /**
     * Retrieves the time format used in the plugin.
     *
     * @return The time format.
     */
    SimpleDateFormat getTimeFormat();

    /**
     * Checks if auto game mode is enabled.
     *
     * @return True if auto game mode is enabled, otherwise false.
     */
    boolean isAutoGameModeEnabled();

    /**
     * Checks if world linking is enabled.
     *
     * @return True if world linking is enabled, otherwise false.
     */
    boolean isWorldLinkEnabled();

    /**
     * Checks if the automatic world unloading system is enabled.
     *
     * This method indicates whether the system responsible for automatically
     * unloading inactive or unused worlds is currently active. When enabled,
     * worlds that have not been accessed for a certain period will be
     * automatically unloaded to free up resources.
     *
     * @return {@code true} if the auto-unload system is enabled, {@code false} otherwise.
     */
    boolean isAutoUnloadSystemEnabled();


    /**
     * Checks if Nether world access via command is allowed.
     *
     * @return True if Nether world access via command is allowed, otherwise false.
     */
    boolean canNetherWorldAccessViaCommand();

    /**
     * Checks if End world access via command is allowed.
     *
     * @return True if End world access via command is allowed, otherwise false.
     */
    boolean canEndWorldAccessViaCommand();

    /**
     * Retrieves the delay in seconds before a world is automatically unloaded.
     *
     * Worlds that are no longer needed or are inactive will be unloaded after
     * this specified delay. The delay helps to ensure that resources are freed
     * efficiently, while allowing for any pending processes or tasks related to
     * the world to be completed.
     *
     * @return the delay in seconds before the world is automatically unloaded.
     */
    long getAutoUnloadSystemDelay();

    long getAutoUnloadLoadDelay();

    boolean isAutoWorldImportEnabled();

    /**
     * Retrieves the default properties for worlds.
     *
     * @return The default properties for worlds.
     */
    ParsedMap<WorldDefaultProperty, Object> getDefaultProperties();

    /**
     * Retrieves a list of strings from the configuration.
     *
     * @param path The path to the configuration value.
     * @return The list of strings at the specified path.
     */
    List<String> getStringList(@NotNull final String path);

}