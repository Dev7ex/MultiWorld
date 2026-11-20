package com.dev7ex.multiworld.api.user;

import com.dev7ex.multiworld.api.module.WorldModule;
import com.dev7ex.multiworld.api.user.offline.WorldOfflineUser;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldUserRegistry extends WorldModule {

    void register(@NotNull final WorldOfflineUser worldUser);

    void unregister(@NotNull final WorldOfflineUser user);

    void unregister(@NotNull final UUID uniqueId);

    Optional<WorldOfflineUser> find(@NotNull final UUID uniqueId);

    Optional<WorldOfflineUser> find(@NotNull final String name);

}
