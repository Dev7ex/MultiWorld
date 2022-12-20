package com.dev7ex.multiworld.listener;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.event.user.UserLoginEvent;
import com.dev7ex.multiworld.api.event.user.UserLogoutEvent;
import com.dev7ex.multiworld.event.MultiWorldListener;
import com.dev7ex.multiworld.user.WorldUser;

import org.bukkit.Bukkit;
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
        final WorldUser user = new WorldUser(event.getPlayer().getUniqueId());

        Bukkit.getPluginManager().callEvent(new UserLoginEvent(user));

        super.getWorldUserService().registerUser(user);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerQuit(final PlayerQuitEvent event) {
        final WorldUser user = super.getWorldUser(event.getPlayer().getUniqueId());

        Bukkit.getPluginManager().callEvent(new UserLogoutEvent(user));

        super.getWorldUserService().removeUser(event.getPlayer().getUniqueId());
    }

}
