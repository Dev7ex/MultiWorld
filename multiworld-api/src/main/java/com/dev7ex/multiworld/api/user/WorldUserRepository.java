package com.dev7ex.multiworld.api.user;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldUserRepository {

    @NotNull
    CompletableFuture<Optional<WorldUser>> find(@NotNull final UUID uniqueId);

    @NotNull
    CompletableFuture<Void> save(@NotNull final WorldUser user);

    @NotNull
    CompletableFuture<Void> saveAll(@NotNull final Collection<? extends WorldUser> users);

}
