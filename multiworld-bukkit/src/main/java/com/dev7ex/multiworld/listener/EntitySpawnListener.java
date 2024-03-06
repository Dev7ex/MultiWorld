package com.dev7ex.multiworld.listener;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.event.MultiWorldListener;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 06.03.2024
 */
public class EntitySpawnListener extends MultiWorldListener {

    public EntitySpawnListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        super(multiWorldApi);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleEntitySpawn(final EntitySpawnEvent event) {
        if (event.getLocation().getWorld() == null) {
            return;
        }
        final String worldName = event.getLocation().getWorld().getName();

        if (super.getWorldProvider().getWorldHolder(worldName).isEmpty()) {
            return;
        }
        final BukkitWorldHolder worldHolder = super.getWorldProvider().getWorldHolder(worldName).get();

        event.setCancelled(!worldHolder.isSpawnEntities());
    }

}
