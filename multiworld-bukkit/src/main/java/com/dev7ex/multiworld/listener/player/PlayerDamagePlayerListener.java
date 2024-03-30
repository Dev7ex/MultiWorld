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
 * @author Dev7ex
 * @since 29.06.2023
 */
public class PlayerDamagePlayerListener extends MultiWorldListener {

    public PlayerDamagePlayerListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        super(multiWorldApi);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerDamagePlayer(final EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        final Player player = (Player) event.getEntity();

        if (super.getWorldProvider().getWorldHolder(player.getWorld().getName()).isEmpty()) {
            return;
        }
        final BukkitWorldHolder worldHolder = super.getWorldProvider().getWorldHolder(player.getWorld().getName()).orElseThrow();

        if (worldHolder.isPvpEnabled()) {
            return;
        }

        if (event.getDamager() instanceof Projectile) {
            final Projectile projectile = (Projectile) event.getDamager();

            if (projectile.getShooter() == null) {
                return;
            }

            if (projectile.getShooter() instanceof Player) {
                event.setCancelled(true);
            }
        }

        if (event.getDamager().getType() == EntityType.PLAYER) {
            event.setCancelled(true);
        }

    }

}
