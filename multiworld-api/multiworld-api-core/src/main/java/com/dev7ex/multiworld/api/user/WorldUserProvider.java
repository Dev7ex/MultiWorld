package com.dev7ex.multiworld.api.user;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldUserProvider {

    void registerUser(@NotNull final WorldUser user);

    void unregisterUser(@NotNull final UUID uniqueId);

    Optional<WorldUser> getUser(@NotNull final UUID uniqueId);

    Optional<WorldUser> getUser(@NotNull final String name);

    void saveUser(@NotNull final WorldUser user);

    void saveUser(@NotNull final WorldUser user, @NotNull final WorldUserProperty... properties);

    Map<UUID, WorldUser> getUsers();

}
