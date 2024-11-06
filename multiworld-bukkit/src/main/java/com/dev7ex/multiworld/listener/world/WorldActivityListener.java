package com.dev7ex.multiworld.listener.world;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.event.MultiWorldListener;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 19.09.2024
 */
public class WorldActivityListener extends MultiWorldListener {

    /**
     * Constructs a MultiWorldListener with the given MultiWorldBukkitApi.
     *
     * @param multiWorldApi The MultiWorldBukkitApi instance.
     */
    public WorldActivityListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        super(multiWorldApi);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerInteract(final PlayerInteractEvent event) {
        final String worldName = event.getPlayer().getWorld().getName();

        if (super.getWorldProvider().getWorldHolder(worldName).isEmpty()) {
            return;
        }
        final BukkitWorldHolder worldHolder = super.getWorldProvider()
                .getWorldHolder(worldName)
                .get();

        worldHolder.setLastActivity(System.currentTimeMillis());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleWorldBlockBreak(final BlockBreakEvent event) {
        final String worldName = event.getBlock().getWorld().getName();

        if (super.getWorldProvider().getWorldHolder(worldName).isEmpty()) {
            return;
        }
        final BukkitWorldHolder worldHolder = super.getWorldProvider()
                .getWorldHolder(worldName)
                .get();

        worldHolder.setLastActivity(System.currentTimeMillis());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleWorldBlockBreak(final BlockPlaceEvent event) {
        final String worldName = event.getBlock().getWorld().getName();

        if (super.getWorldProvider().getWorldHolder(worldName).isEmpty()) {
            return;
        }
        final BukkitWorldHolder worldHolder = super.getWorldProvider()
                .getWorldHolder(worldName)
                .get();

        worldHolder.setLastActivity(System.currentTimeMillis());
    }

}
