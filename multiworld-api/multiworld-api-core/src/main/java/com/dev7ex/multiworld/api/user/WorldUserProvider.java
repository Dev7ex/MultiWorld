package com.dev7ex.multiworld.api.user;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Provides methods for managing user data within the multi-world system.
 * Users can be registered, unregistered, retrieved by unique ID or name,
 * and their data can be saved.
 *
 * @param <T> The type of WorldUser implementation.
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldUserProvider<T extends WorldUser> {

    /**
     * Registers a user in the multi-world system.
     *
     * @param user The user to register.
     */
    void registerUser(@NotNull final T user);

    /**
     * Unregisters a user from the multi-world system.
     *
     * @param uniqueId The unique ID of the user to unregister.
     */
    void unregisterUser(@NotNull final UUID uniqueId);

    /**
     * Retrieves a user by their unique ID.
     *
     * @param uniqueId The unique ID of the user to retrieve.
     * @return An optional containing the user if found, otherwise empty.
     */
    Optional<T> getUser(@NotNull final UUID uniqueId);

    /**
     * Retrieves a user by their name.
     *
     * @param name The name of the user to retrieve.
     * @return An optional containing the user if found, otherwise empty.
     */
    Optional<T> getUser(@NotNull final String name);

    /**
     * Saves the data of a user.
     *
     * @param user The user whose data to save.
     */
    void saveUser(@NotNull final T user);

    /**
     * Saves specific properties of a user.
     *
     * @param user       The user whose data to save.
     * @param properties The properties to save.
     */
    void saveUser(@NotNull final T user, @NotNull final WorldUserProperty... properties);

    /**
     * Retrieves a map of all registered users, keyed by their unique ID.
     *
     * @return A map containing all registered users.
     */
    Map<UUID, T> getUsers();

}
