package com.dev7ex.multiworld.user;

import com.dev7ex.common.bukkit.plugin.service.PluginService;

import com.google.common.collect.Maps;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 * @author Dev7ex
 * @since 19.05.2021
 */
@Getter(AccessLevel.PUBLIC)
public final class WorldUserService implements PluginService {

    private final Map<UUID, WorldUser> users = Maps.newHashMap();

    @Override
    public void onEnable() {
        for(final Player player : Bukkit.getOnlinePlayers()) {
            final WorldUser worldUser = new WorldUser(player.getUniqueId());
            this.users.put(player.getUniqueId(), worldUser);
        }
    }

    @Override
    public void onDisable() {
        this.users.clear();
    }

    public void registerUser(final WorldUser worldUser) {
        this.users.put(worldUser.getUniqueId(), worldUser);
    }

    public void removeUser(final UUID uniqueId) {
        this.users.remove(uniqueId);
    }

}
