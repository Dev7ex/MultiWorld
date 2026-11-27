package com.dev7ex.multiworld.listener.player;

import com.dev7ex.multicommon.api.inventory.slot.InventorySlot;
import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.event.MultiWorldListener;
import com.dev7ex.multiworld.api.inventory.WorldInventoryHolder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 12.06.2025
 */
public class PlayerInteractInventoryListener extends MultiWorldListener {

    public PlayerInteractInventoryListener(@NotNull final MultiWorldApi multiWorldApi) {
        super(multiWorldApi);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void handleInventoryClick(final InventoryClickEvent event) {
        if (event.getWhoClicked().getType() != EntityType.PLAYER) {
            return;
        }
        final Player player = (Player) event.getWhoClicked();

        if (!(event.getInventory().getHolder(false) instanceof final WorldInventoryHolder inventoryHolder)) {
            return;
        }
        inventoryHolder.onClick(event);
        final InventorySlot inventorySlot = inventoryHolder.getSlot(event.getSlot());

        if (inventorySlot.getClickConsumer() != null) {
            inventorySlot.getClickConsumer().accept(event);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void handleInventoryClose(final InventoryCloseEvent event) {
        if (event.getPlayer().getType() != EntityType.PLAYER) {
            return;
        }
        if (!(event.getInventory().getHolder(false) instanceof final WorldInventoryHolder inventoryHolder)) {
            return;
        }
        super.getInventoryRegistry().unregister(inventoryHolder);
        inventoryHolder.onClose(event);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void handleInventoryDrag(final InventoryDragEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof final WorldInventoryHolder inventoryHolder)) {
            return;
        }
        inventoryHolder.onDrag(event);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void handleInventoryOpen(final InventoryOpenEvent event) {
        if (event.getPlayer().getType() != EntityType.PLAYER) {
            return;
        }
        if (!(event.getInventory().getHolder(false) instanceof final WorldInventoryHolder inventoryHolder)) {
            return;
        }
        super.getInventoryRegistry().register(inventoryHolder);
        inventoryHolder.onOpen(event);


    }

}
