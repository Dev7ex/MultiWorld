package com.dev7ex.multiworld.inventory.menu.main;

import com.dev7ex.multiworld.api.inventory.menu.WorldInventoryMenu;
import com.dev7ex.multiworld.api.inventory.menu.main.WorldGuestMainMenu;
import com.dev7ex.multiworld.api.world.WorldContext;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Dev7ex
 * @since 22.11.2026
 */
public class DefaultWorldGuestMainMenu extends WorldInventoryMenu implements WorldGuestMainMenu {

    protected DefaultWorldGuestMainMenu(@NotNull final Player player, @NotNull final Component title, final int rows) {
        super(player, title, rows);
    }

    @Override
    public void onClick(@NotNull final InventoryClickEvent inventoryClickEvent) {

    }

    @Override
    public void onClose(@NotNull final InventoryCloseEvent inventoryCloseEvent) {

    }

    @Override
    public void onDrag(@NotNull final InventoryDragEvent inventoryDragEvent) {

    }

    @Override
    public void onOpen(@NotNull final InventoryOpenEvent inventoryOpenEvent) {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void populateWorlds(@NotNull final Player player) {

    }

    @Override
    public void populateNavigationBar(@NotNull final Player player) {

    }

    @Override
    public List<WorldContext> getWorlds() {
        return List.of();
    }

}
