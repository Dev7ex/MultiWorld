package com.dev7ex.multiworld.api.world;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * A provider interface for managing worlds.
 * Implementations of this interface handle the registration, retrieval,
 * and configuration of world holders.
 *
 * @param <T> The type of world holder managed by this provider.
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldProvider<T extends WorldHolder> {

    /**
     * Registers a new world holder.
     *
     * @param worldHolder The world holder to register.
     */
    void register(@NotNull final T worldHolder);

    /**
     * Unregisters a world holder by its name.
     *
     * @param name The name of the world holder to unregister.
     */
    void unregister(@NotNull final String name);

    /**
     * Checks if a world holder with the given name is registered.
     *
     * @param name The name of the world holder to check.
     * @return True if the world holder is registered, false otherwise.
     */
    boolean isRegistered(@NotNull final String name);

    /**
     * Retrieves a world holder by its name.
     *
     * @param name The name of the world holder to retrieve.
     * @return An Optional containing the retrieved world holder, or empty if not found.
     */
    Optional<T> getWorldHolder(@NotNull final String name);

    /**
     * Retrieves all registered world holders.
     *
     * @return A map of world holder names to their corresponding world holders.
     */
    Map<String, T> getWorldHolders();

    /**
     * Retrieves the configuration associated with this world provider.
     *
     * @return The configuration of the world provider.
     */
    WorldConfiguration<T> getConfiguration();
}