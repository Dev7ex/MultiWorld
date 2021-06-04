package com.dev7ex.multiworld.user;

import com.dev7ex.common.bukkit.plugin.service.PluginService;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Dev7ex
 * @since 19.05.2021
 *
 */

@Getter
public final class WorldUserService implements PluginService {

    private final Map<UUID, WorldUser> users = Maps.newHashMap();

    @Override
    public final void onEnable() {
        for(final Player player : Bukkit.getOnlinePlayers()) {
            final WorldUser worldUser = new WorldUser(player.getUniqueId());
            this.users.put(player.getUniqueId(), worldUser);
        }
    }

    @Override
    public final void onDisable() {
        this.users.clear();
    }

    public final void registerUser(final WorldUser worldUser) {
        this.users.put(worldUser.getUniqueId(), worldUser);
    }

    public final void removeUser(final UUID uniqueId) {
        this.users.remove(uniqueId);
    }

}
