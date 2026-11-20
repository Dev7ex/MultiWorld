package com.dev7ex.multiworld.api.user;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Dev7ex
 * @since 19.10.2025
 */
public interface WorldUserFactory {

    WorldUser create(@NotNull final UUID uniqueId, @NotNull final String name);

    WorldUser create(@NotNull final Player player);

    WorldUser create(@NotNull final PlayerProfile profile);

}
