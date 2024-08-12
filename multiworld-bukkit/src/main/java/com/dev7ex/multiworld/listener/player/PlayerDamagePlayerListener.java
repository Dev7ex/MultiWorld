package com.dev7ex.multiworld.listener.player;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.event.MultiWorldListener;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Listener for player damage events.
 * This listener handles the event when a player is damaged by another entity.
 * It cancels the event if PvP is disabled in the world settings.
 *
 * @since 29.06.2023
 */
public class PlayerDamagePlayerListener extends MultiWorldListener {

    /**
     * Constructs a new PlayerDamagePlayerListener.
     *
     * @param multiWorldApi The MultiWorldBukkitApi instance.
     */
    public PlayerDamagePlayerListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        super(multiWorldApi);
    }

    /**
     * Handles the EntityDamageByEntityEvent.
     * If the entity damaged is a player and PvP is disabled in the world, the event is cancelled.
     *
     * @param event The EntityDamageByEntityEvent.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerDamagePlayer(final EntityDamageByEntityEvent event) {
        // Check if the damaged entity is a player
        if (event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        final Player player = (Player) event.getEntity();

        // Get the world holder for the current world
        final BukkitWorldHolder worldHolder = super.getWorldProvider()
                .getWorldHolder(player.getWorld().getName())
                .orElse(null);

        // If the worldHolder is null or PvP is enabled in the world, return
        if ((worldHolder == null) || (worldHolder.isPvpEnabled())) {
            return;
        }

        // Check if the damager is a projectile
        if (event.getDamager() instanceof Projectile) {
            final Projectile projectile = (Projectile) event.getDamager();

            // If the projectile has a shooter and the shooter is a player, cancel the event
            if (projectile.getShooter() instanceof Player) {
                event.setCancelled(true);
            }
            return;
        }

        // If the damager is a player, cancel the event
        if (event.getDamager().getType() == EntityType.PLAYER) {
            event.setCancelled(true);
        }
    }

}
