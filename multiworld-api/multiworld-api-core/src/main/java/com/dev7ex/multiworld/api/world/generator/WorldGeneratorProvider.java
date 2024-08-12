package com.dev7ex.multiworld.api.world.generator;

import java.util.List;
import java.util.Map;

/**
 * Represents a provider for world generators.
 * Implementations of this interface provide methods to retrieve a map of world generators.
 *
 * @param <T> The type of WorldGeneratorHolder.
 * @since 29.06.2023
 */
public interface WorldGeneratorProvider<T extends WorldGeneratorHolder, G extends WorldGenerator> {

    /**
     * Retrieves a map of custom world generators.
     *
     * @return A map containing instances of WorldGeneratorHolder as keys and their corresponding world generator names as values.
     */
    Map<T, String> getCustomGenerators();

    /**
     * Retrieves a map of file-based world generators.
     *
     * @return A map containing instances of WorldGeneratorHolder as keys and their corresponding world generators as values.
     */
    Map<T, WorldGenerator> getFileGenerators();

    /**
     * Retrieves a map of default world generators.
     *
     * @return A map containing instances of WorldGeneratorHolder as keys and their corresponding default world generators as values.
     */
    List<G> getDefaultGenerators();

    /**
     * Checks if a custom world generator is registered.
     *
     * @param generator The name of the generator to check.
     * @return True if the generator is registered, false otherwise.
     */
    boolean isRegistered(String generator);

    List<String> getAllGenerators();

}
