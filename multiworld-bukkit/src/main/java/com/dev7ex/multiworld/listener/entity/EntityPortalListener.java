package com.dev7ex.multiworld.listener.entity;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.event.MultiWorldListener;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPortalEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Listens for entity portal events and handles them based on world configurations.
 * This class extends MultiWorldListener to utilize multi-world functionality.
 *
 * @author Dev7ex
 * @since 07.06.2024
 */
public class EntityPortalListener extends MultiWorldListener {

    /**
     * Constructs an EntityPortalListener with the given MultiWorldBukkitApi instance.
     *
     * @param multiWorldApi The MultiWorldBukkitApi instance.
     */
    public EntityPortalListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        super(multiWorldApi);
    }

    /**
     * Handles the entity entering a portal event.
     *
     * @param event The EntityPortalEvent.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void handleEntityEnterPortal(final EntityPortalEvent event) {
        if (!super.getConfiguration().isWorldLinkEnabled()) {
            return;
        }

        if (event.getFrom().getWorld() == null) {
            return;
        }
        final BukkitWorldHolder fromWorldHolder = super.getWorldProvider().getWorldHolder(event.getFrom().getWorld().getName()).orElseThrow();

        if ((event.getTo() == null) || (event.getTo().getWorld() == null)) {
            return;
        }

        switch (event.getTo().getWorld().getEnvironment()) {
            case NETHER:
                if (fromWorldHolder.getNetherWorldName() == null) {
                    event.setCancelled(true);
                    return;
                }

                if (super.getWorldProvider().getWorldHolder(fromWorldHolder.getNetherWorldName()).isEmpty()) {
                    event.setCancelled(true);
                    return;
                }
                final BukkitWorldHolder netherWorldHolder = super.getWorldProvider().getWorldHolder(fromWorldHolder.getNetherWorldName()).get();

                if (!netherWorldHolder.isLoaded()) {
                    event.setCancelled(true);
                    return;
                }
                event.getTo().setWorld(netherWorldHolder.getWorld());
                break;

            case NORMAL:
                if (fromWorldHolder.getNormalWorldName() == null) {
                    event.setCancelled(true);
                    return;
                }

                if (super.getWorldProvider().getWorldHolder(fromWorldHolder.getNormalWorldName()).isEmpty()) {
                    event.setCancelled(true);
                    return;
                }
                final BukkitWorldHolder normalWorldHolder = super.getWorldProvider().getWorldHolder(fromWorldHolder.getNormalWorldName()).get();

                if (!normalWorldHolder.isLoaded()) {
                    event.setCancelled(true);
                    return;
                }
                event.getTo().setWorld(normalWorldHolder.getWorld());
                break;

            case THE_END:
                if (fromWorldHolder.getEndWorldName() == null) {
                    event.setCancelled(true);
                    return;
                }

                if (super.getWorldProvider().getWorldHolder(fromWorldHolder.getEndWorldName()).isEmpty()) {
                    event.setCancelled(true);
                    return;
                }
                final BukkitWorldHolder endWorldHolder = super.getWorldProvider().getWorldHolder(fromWorldHolder.getEndWorldName()).get();

                if (!endWorldHolder.isLoaded()) {
                    event.setCancelled(true);
                    return;
                }
                event.getTo().setWorld(endWorldHolder.getWorld());
                break;
        }
    }

    /**
     * Handles the entity entering a block portal event, such as nether or end portals.
     *
     * @param event The EntityPortalEvent.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerEnterBlockPortal(final EntityPortalEvent event) {
        final BukkitWorldHolder fromWorldHolder = super.getWorldProvider().getWorldHolder(event.getFrom().getWorld().getName()).orElseThrow();

        if (event.getTo() == null) {
            return;
        }

        if (event.getTo().getWorld() == null) {
            return;
        }

        switch (event.getTo().getWorld().getEnvironment()) {
            case NETHER:
                if (fromWorldHolder.isNetherPortalAccessible()) {
                    return;
                }
                event.setCancelled(true);
                break;

            case THE_END:
                if (fromWorldHolder.isEndPortalAccessible()) {
                    return;
                }
                event.setCancelled(true);
                break;
        }
    }

}
