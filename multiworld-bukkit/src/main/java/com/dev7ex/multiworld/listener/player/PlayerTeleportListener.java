package com.dev7ex.multiworld.listener.player;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.event.MultiWorldListener;
import com.dev7ex.multiworld.api.bukkit.event.user.WorldUserTeleportWorldEvent;
import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUser;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Listener for player teleport events.
 * This listener handles the event when a player teleports between worlds.
 * It triggers a custom event if the player teleports from one registered world to another.
 *
 * @since 07.06.2024
 */
public class PlayerTeleportListener extends MultiWorldListener {

    /**
     * Constructs a PlayerTeleportListener with the given MultiWorldBukkitApi.
     *
     * @param multiWorldApi The MultiWorldBukkitApi instance.
     */
    public PlayerTeleportListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        super(multiWorldApi);
    }

    /**
     * Handles the PlayerTeleportEvent.
     * If the player teleports from one registered world to another, it triggers a WorldUserTeleportWorldEvent.
     *
     * @param event The PlayerTeleportEvent.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerTeleport(final PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        final World to = event.getTo() != null ? event.getTo().getWorld() : null;
        final World from = event.getFrom().getWorld();

        // Ensure the teleport destination world and source world are valid
        if (to == null || from == null) {
            return;
        }

        // Ensure both the source and destination worlds are registered
        if (super.getWorldProvider().getWorldHolder(to.getName()).isEmpty() || super.getWorldProvider().getWorldHolder(from.getName()).isEmpty()) {
            return;
        }
        final BukkitWorldHolder lastWorldHolder = super.getWorldProvider().getWorldHolder(from.getName()).get();
        final BukkitWorldHolder nextWorldHolder = super.getWorldProvider().getWorldHolder(to.getName()).get();

        // Ensure the user is registered
        if (super.getUserProvider().getUser(player.getUniqueId()).isEmpty()) {
            return;
        }
        final BukkitWorldUser user = super.getUserProvider().getUser(player.getUniqueId()).get();

        // Trigger the custom WorldUserTeleportWorldEvent
        Bukkit.getPluginManager().callEvent(new WorldUserTeleportWorldEvent(user, lastWorldHolder, nextWorldHolder));
    }

}
