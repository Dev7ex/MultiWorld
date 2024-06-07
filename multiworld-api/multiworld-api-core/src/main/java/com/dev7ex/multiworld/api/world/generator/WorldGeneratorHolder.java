package com.dev7ex.multiworld.api.world.generator;

/**
 * Represents a holder for a world generator.
 * Implementations of this interface provide methods to retrieve the name of the world generator.
 *
 * @author Dev7ex
 * @since 06.06.2024
 */
public interface WorldGeneratorHolder {

    /**
     * Gets the name of the world generator.
     *
     * @return The name of the world generator.
     */
    String getName();

}