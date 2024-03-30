package com.dev7ex.multiworld.listener.user;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.event.MultiWorldListener;
import com.dev7ex.multiworld.api.bukkit.event.user.WorldUserTeleportWorldEvent;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.bukkit.world.location.BukkitWorldLocation;
import com.dev7ex.multiworld.api.user.WorldUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 29.06.2023
 */
public class UserTeleportWorldListener extends MultiWorldListener {

    public UserTeleportWorldListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        super(multiWorldApi);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleUserGameModeChange(final WorldUserTeleportWorldEvent event) {
        if (!super.getConfiguration().isAutoGameModeEnabled()) {
            return;
        }

        final Player player = Bukkit.getPlayer(event.getUser().getUniqueId());
        player.setGameMode(event.getNextWorldHolder().getGameMode());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleUserEnterWorld(final WorldUserTeleportWorldEvent event) {
        final WorldUser user = event.getUser();
        final Player player = Bukkit.getPlayer(user.getUniqueId());
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
