package com.dev7ex.multiworld.listener;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.event.MultiWorldListener;
import com.dev7ex.multiworld.user.WorldUser;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Dev7ex
 * @since 19.05.2021
 */
public final class PlayerConnectionListener extends MultiWorldListener {

    public PlayerConnectionListener(final MultiWorldPlugin multiWorldPlugin) {
        super(multiWorldPlugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerLogin(final PlayerLoginEvent event) {
        super.getWorldUserService().registerUser(new WorldUser(event.getPlayer().getUniqueId()));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerQuit(final PlayerQuitEvent event) {
        super.getWorldUserService().removeUser(event.getPlayer().getUniqueId());
    }

}
