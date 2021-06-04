package com.dev7ex.multiworld.user;

import com.dev7ex.multiworld.MultiWorldPlugin;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@Getter @Setter
public final class WorldUser {

    private final UUID uniqueId;
    private WorldUserProperties properties;

    public WorldUser(final UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.properties = new WorldUserProperties();
    }

    public final void sendMessage(final String message) {
        this.getPlayer().sendMessage(MultiWorldPlugin.getInstance().getConfiguration().getPrefix() + message);
    }

    public final Player getPlayer() {
        return Bukkit.getPlayer(this.uniqueId);
    }

}
