package com.dev7ex.multiworld.api.user;

import com.dev7ex.common.collect.map.ParsedMap;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the configuration of a user within the multi-world system.
 * This interface provides methods to read and write user configuration data.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldUserConfiguration {

    /**
     * Reads the user configuration data.
     *
     * @return A parsed map containing the user configuration data.
     */
    ParsedMap<WorldUserProperty, Object> read();

    /**
     * Reads specific properties from the user configuration data.
     *
     * @param properties The properties to read.
     * @return A parsed map containing the specified user configuration data.
     */
    ParsedMap<WorldUserProperty, Object> read(@NotNull final WorldUserProperty... properties);

    /**
     * Writes user configuration data.
     *
     * @param userData The user configuration data to write.
     */
    void write(@NotNull final ParsedMap<WorldUserProperty, Object> userData);

    /**
     * Saves the user configuration data to a file.
     */
    void save();

}