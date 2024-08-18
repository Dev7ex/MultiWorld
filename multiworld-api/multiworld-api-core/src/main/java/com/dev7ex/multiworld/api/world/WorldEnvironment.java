package com.dev7ex.multiworld.api.world;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Represents different types of world environments.
 * This enum provides methods to convert between WorldType and WorldEnvironment,
 * as well as to obtain string representations of the environment.
 *
 * @author Dev7ex
 * @since 26.06.2023
 */
public enum WorldEnvironment {

    CUSTOM,
    NORMAL,
    NETHER,
    THE_END;

    /**
     * Converts a WorldType to its corresponding WorldEnvironment.
     *
     * @param worldType The WorldType to convert.
     * @return The corresponding WorldEnvironment.
     */
    public static WorldEnvironment fromType(final WorldType worldType) {
        switch (worldType) {
            case THE_END:
                return WorldEnvironment.THE_END;

            case NETHER:
                return WorldEnvironment.NETHER;

            case NORMAL:
                return WorldEnvironment.NORMAL;

            default:
                return WorldEnvironment.CUSTOM;
        }
    }

    /**
     * Returns a list of string representations of all available WorldEnvironment values.
     *
     * @return A list of string representations of WorldEnvironment values.
     */
    public static List<String> toStringList() {
        return Arrays.stream(WorldEnvironment.values()).map(Enum::name).toList();
    }

    /**
     * Returns an Optional containing the WorldEnvironment enum constant with the specified name,
     * or an empty Optional if no such constant exists.
     *
     * @param name The name of the WorldEnvironment constant to find.
     * @return An Optional containing the found WorldEnvironment constant, or an empty Optional if not found.
     */
    public static Optional<WorldEnvironment> fromString(@NotNull final String name) {
        return Arrays.stream(WorldEnvironment.values()).filter(worldType -> worldType.name().equalsIgnoreCase(name)).findFirst();
    }

}