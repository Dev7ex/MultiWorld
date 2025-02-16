package com.dev7ex.multiworld.listener.world;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.event.MultiWorldListener;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 27.06.2024
 */
public class WorldFlagListener extends MultiWorldListener {

    /**
     * Constructs a MultiWorldListener with the given MultiWorldBukkitApi.
     *
     * @param multiWorldApi The MultiWorldBukkitApi instance.
     */
    public WorldFlagListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        super(multiWorldApi);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleWeatherChange(final WeatherChangeEvent event) {
        if (super.getWorldProvider().getWorldHolder(event.getWorld().getName()).isEmpty()) {
            return;
        }
        final BukkitWorldHolder worldHolder = super.getWorldProvider()
                .getWorldHolder(event.getWorld().getName())
                .get();

        if (!worldHolder.isWeatherEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleFoodLevelChange(final FoodLevelChangeEvent event) {
        if (super.getWorldProvider().getWorldHolder(event.getEntity().getWorld().getName()).isEmpty()) {
            return;
        }
        final BukkitWorldHolder worldHolder = super.getWorldProvider()
                .getWorldHolder(event.getEntity().getWorld().getName())
                .get();

        if (!worldHolder.isHungerEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePistonRetractEvent(final BlockPistonRetractEvent event) {
        if (super.getWorldProvider().getWorldHolder(event.getBlock().getWorld().getName()).isEmpty()) {
            return;
        }
        final BukkitWorldHolder worldHolder = super.getWorldProvider()
                .getWorldHolder(event.getBlock().getWorld().getName())
                .get();

        if (!worldHolder.isRedstoneEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleBlockPistonExtend(final BlockPistonExtendEvent event) {
        if (super.getWorldProvider().getWorldHolder(event.getBlock().getWorld().getName()).isEmpty()) {
            return;
        }
        final BukkitWorldHolder worldHolder = super.getWorldProvider()
                .getWorldHolder(event.getBlock().getWorld().getName())
                .get();

        if (!worldHolder.isRedstoneEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleBlockPhysic(final BlockPhysicsEvent event) {
        if (super.getWorldProvider().getWorldHolder(event.getBlock().getWorld().getName()).isEmpty()) {
            return;
        }
        final BukkitWorldHolder worldHolder = super.getWorldProvider()
                .getWorldHolder(event.getBlock().getWorld().getName())
                .get();

        if (event.getBlock().getType() != Material.REDSTONE_WIRE) {
            return;
        }
        if (!worldHolder.isRedstoneEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleBlockRedstone(final BlockRedstoneEvent event) {
        if (super.getWorldProvider().getWorldHolder(event.getBlock().getWorld().getName()).isEmpty()) {
            return;
        }
        final BukkitWorldHolder worldHolder = super.getWorldProvider()
                .getWorldHolder(event.getBlock().getWorld().getName())
                .get();

        if (worldHolder.isRedstoneEnabled()) {
            return;
        }
        event.setNewCurrent(event.getOldCurrent());
    }

}
