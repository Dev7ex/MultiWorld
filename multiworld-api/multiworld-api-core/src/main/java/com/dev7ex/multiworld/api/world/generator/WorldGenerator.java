package com.dev7ex.multiworld.api.world.generator;

/**
 * Represents a world generator.
 * Implementations of this interface provide methods for generating worlds.
 *
 * @author Dev7ex
 * @since 26.03.2024
 */
public interface WorldGenerator {

    /**
     * Gets the name of the world generator.
     *
     * @return The name of the world generator.
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }

}