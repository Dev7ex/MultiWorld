package com.dev7ex.multiworld.listener;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.event.MultiWorldListener;
import com.dev7ex.multiworld.world.WorldProperties;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @author Dev7ex
 * @since 24.05.2021
 */

public final class EntityDamageByEntityListener extends MultiWorldListener {

    public EntityDamageByEntityListener(final MultiWorldPlugin multiWorldPlugin) {
        super(multiWorldPlugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public final void handleEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        final Player player = (Player) event.getEntity();
        final WorldProperties worldProperties = super.getWorldManager().getWorldProperties().get(player.getWorld().getName());

        if (worldProperties == null) {
            return;
        }

        if (worldProperties.isPvpEnabled()) {
            return;
        }

        if (event.getDamager().getType() == EntityType.PLAYER) {
            event.setCancelled(true);
        }

        if (event.getDamager().getType() == EntityType.ARROW) {
            final Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }

}
