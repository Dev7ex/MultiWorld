package com.dev7ex.multiworld.listener;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.event.MultiWorldListener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Dev7ex
 * @since 20.05.2021
 *
 */

public final class PlayerQuitListener extends MultiWorldListener {

    public PlayerQuitListener(final MultiWorldPlugin multiWorldPlugin) {
        super(multiWorldPlugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public final void handlePlayerQuit(final PlayerQuitEvent event) {
        super.getWorldUserService().removeUser(event.getPlayer().getUniqueId());
    }

}
