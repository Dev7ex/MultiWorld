package com.dev7ex.multiworld.listener;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.event.MultiWorldListener;
import com.dev7ex.multiworld.user.WorldUser;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 *
 * @author Dev7ex
 * @since 20.05.2021
 *
 */

public final class PlayerLoginListener extends MultiWorldListener {

    public PlayerLoginListener(final MultiWorldPlugin multiWorldPlugin) {
        super(multiWorldPlugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public final void handlePlayerLogin(final PlayerLoginEvent event) {
        super.getWorldUserService().registerUser(new WorldUser(event.getPlayer().getUniqueId()));
    }

}
