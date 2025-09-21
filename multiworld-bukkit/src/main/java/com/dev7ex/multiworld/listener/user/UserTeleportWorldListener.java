package com.dev7ex.multiworld.listener.user;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.event.MultiWorldListener;
import com.dev7ex.multiworld.api.bukkit.event.user.WorldUserTeleportWorldEvent;
import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUser;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.bukkit.world.location.BukkitWorldLocation;
import com.dev7ex.multiworld.api.user.WorldUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

/**
 * Listens for events related to user teleportation between worlds.
 */
public class UserTeleportWorldListener extends MultiWorldListener {

    /**
     * Constructs a new UserTeleportWorldListener.
     *
     * @param multiWorldApi The MultiWorldBukkitApi instance.
     */
    public UserTeleportWorldListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        super(multiWorldApi);
    }

    /**
     * Handles user game mode change upon world teleportation.
     *
     * @param event The WorldUserTeleportWorldEvent.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onUserGameModeChange(final WorldUserTeleportWorldEvent event) {
        final Player player = event.getUser().getEntity();
        if (!super.getConfiguration().isAutoGameModeEnabled()) {
            return;
        }
        if (event.getNextWorldHolder().getForceGameMode().equalsIgnoreCase("false")) {
            return;
        }
        if (event.getNextWorldHolder().getForceGameMode().equalsIgnoreCase("false-with-permission")) {
            if (player.hasPermission("multiworld.bypass.forcegamemode")) {
                return;
            }
        }
        player.setGameMode(event.getNextWorldHolder().getGameMode());
    }

    /**
     * Handles user entering a world upon teleportation.
     *
     * @param event The WorldUserTeleportWorldEvent.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void handleUserEnterWorld(final WorldUserTeleportWorldEvent event) {
        final BukkitWorldUser user = event.getUser();
        final Player player = user.getEntity();
        final BukkitWorldHolder nextWorldHolder = event.getNextWorldHolder();

        user.setLastLocation(BukkitWorldLocation.of(player.getLocation()));

        if (!nextWorldHolder.isWhitelistEnabled()) {
            return;
        }

        if (nextWorldHolder.getWhitelist().contains(user.getName())) {
            return;
        }

        if (player.hasPermission("multiworld.whitelist.ignore")) {
            return;
        }
        user.setLastLocation(null);
        player.sendMessage(super.getConfiguration().getString("messages.general.world-whitelist-block-trespassing")
                .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                .replaceAll("%world_name%", nextWorldHolder.getName()));
        event.setCancelled(true);
    }
}
