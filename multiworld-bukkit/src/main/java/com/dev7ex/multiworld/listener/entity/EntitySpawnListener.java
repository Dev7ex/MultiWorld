package com.dev7ex.multiworld.listener.entity;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.event.MultiWorldListener;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Listens for entity spawn events and cancels them based on world configurations.
 * This class extends MultiWorldListener to utilize multi-world functionality.
 *
 * @author Dev7ex
 * @since 06.03.2024
 */
public class EntitySpawnListener extends MultiWorldListener {

    /**
     * Constructs an EntitySpawnListener with the given MultiWorldBukkitApi instance.
     *
     * @param multiWorldApi The MultiWorldBukkitApi instance.
     */
    public EntitySpawnListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        super(multiWorldApi);
    }

    /**
     * Handles the entity spawn event and cancels it if configured to disallow entity spawning.
     *
     * @param event The EntitySpawnEvent.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void handleEntitySpawn(final EntitySpawnEvent event) {
        if (event.getLocation().getWorld() == null) {
            return;
        }
        final String worldName = event.getLocation().getWorld().getName();

        if (super.getWorldProvider().getWorldHolder(worldName).isEmpty()) {
            return;
        }
        final BukkitWorldHolder worldHolder = super.getWorldProvider()
                .getWorldHolder(worldName)
                .get();

        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (!worldHolder.isSpawnEntities()) {
            event.setCancelled(true);
        }
    }

}
