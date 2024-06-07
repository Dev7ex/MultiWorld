package com.dev7ex.multiworld.api.world;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An enumeration representing different types of worlds.
 * Each world type has a boolean flag indicating if it belongs to the overworld.
 *
 * @author Dev7ex
 * @since 20.05.2021
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldType {

    /**
     * Represents a custom world type.
     */
    CUSTOM(false),

    /**
     * Represents an end world type.
     */
    END(false),

    /**
     * Represents a flat world type.
     */
    FLAT(true),

    /**
     * Represents a nether world type.
     */
    NETHER(false),

    /**
     * Represents a normal world type.
     */
    NORMAL(true),

    /**
     * Represents a water world type.
     */
    WATER(true),

    /**
     * Represents a void world type.
     */
    VOID(true);

    private final boolean overWorld;

    /**
     * Constructs a WorldType with the given overworld flag.
     *
     * @param overWorld True if the world belongs to the overworld, false otherwise.
     */
    WorldType(final boolean overWorld) {
        this.overWorld = overWorld;
    }

    /**
     * Retrieves a list of string representations of all WorldType values.
     *
     * @return A list of string representations of WorldType values.
     */
    public static List<String> toStringList() {
        return Arrays.stream(WorldType.values()).map(Enum::name).toList();
    }

    /**
     * Retrieves a WorldType enum value from its string representation.
     *
     * @param name The string representation of the WorldType.
     * @return An Optional containing the corresponding WorldType enum value, or empty if not found.
     */
    public static Optional<WorldType> fromString(final String name) {
        return Arrays.stream(WorldType.values())
                .filter(worldType -> worldType.name().equalsIgnoreCase(name.toUpperCase()))
                .findFirst();
    }

}