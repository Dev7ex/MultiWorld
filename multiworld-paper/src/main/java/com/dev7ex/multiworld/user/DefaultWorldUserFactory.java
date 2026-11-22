package com.dev7ex.multiworld.user;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.dev7ex.multiworld.api.user.WorldUser;
import com.dev7ex.multiworld.api.user.WorldUserFactory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Dev7ex
 * @since 14.11.2026
 */
public class DefaultWorldUserFactory implements WorldUserFactory {

    @Override
    public WorldUser create(@NotNull final UUID uniqueId, @NotNull final String name) {
        return new DefaultWorldUser(uniqueId, name);
    }

    @Override
    public WorldUser create(@NotNull final UUID uniqueId, @NotNull final String name, final long firstLogin, final long lastLogin) {
        final WorldUser user = new DefaultWorldUser(uniqueId, name);
        user.setFirstLogin(firstLogin);
        user.setLastLogin(lastLogin);
        return user;
    }

    @Override
    public WorldUser create(@NotNull final Player player) {
        return new DefaultWorldUser(player.getUniqueId(), player.getName());
    }

    @Override
    public WorldUser create(@NotNull final PlayerProfile profile) {
        return new DefaultWorldUser(Objects.requireNonNull(profile.getId()), Objects.requireNonNull(profile.getName()));
    }

}
