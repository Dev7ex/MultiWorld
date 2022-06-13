package com.dev7ex.multiworld.listener;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.event.MultiWorldListener;
import com.dev7ex.multiworld.world.WorldProperties;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;

/**
 * @author Dev7ex
 * @since 24.05.2021
 */
public final class PlayerChangeWorldListener extends MultiWorldListener {

    public PlayerChangeWorldListener(final MultiWorldPlugin multiWorldPlugin) {
        super(multiWorldPlugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerChangeWorld(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        final WorldProperties worldProperties = super.getWorldManager().getWorldProperties().get(player.getWorld().getName());

        if (worldProperties == null) {
            return;
        }
        player.setGameMode(worldProperties.getGameMode());
    }

}
