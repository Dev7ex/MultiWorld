package com.dev7ex.multiworld.inventory;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.inventory.WorldInventoryHolder;
import com.dev7ex.multiworld.api.inventory.WorldInventoryRegistry;
import com.dev7ex.multiworld.module.DefaultWorldModule;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dev7ex
 * @since 12.06.2025
 */
@Getter(AccessLevel.PUBLIC)
public class DefaultWorldInventoryRegistry extends DefaultWorldModule implements WorldInventoryRegistry {

    private final Map<Class<? extends WorldInventoryHolder>, List<WorldInventoryHolder>> inventories = new ConcurrentHashMap<>();

    public DefaultWorldInventoryRegistry(@NotNull final MultiWorldApi multiWorldApi) {
        super(multiWorldApi);
    }

    @Override
    public void onEnable() {
        // TODO document why this method is empty
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.getOpenInventory().getTopInventory().getHolder(false) instanceof WorldInventoryHolder)
                .forEach(player -> player.closeInventory(InventoryCloseEvent.Reason.PLUGIN));
    }

    @Override
    public <T extends WorldInventoryHolder> void register(@NonNull final T inventory) {
        this.inventories.computeIfAbsent(inventory.getClass(), k -> new ArrayList<>()).add(inventory);
    }

    @Override
    public <T extends WorldInventoryHolder> void unregister(@NonNull final T inventory) {
        final List<WorldInventoryHolder> inventoryHolders = this.inventories.get(inventory.getClass());

        if (inventoryHolders == null) {
            return;
        }
        inventoryHolders.remove(inventory);

        if (inventoryHolders.isEmpty()) {
            this.inventories.remove(inventory.getClass());
        }
    }

    @Override
    public void updateInventories(@NotNull Class<? extends WorldInventoryHolder> inventoryClazz) {
        this.inventories.entrySet().stream()
                .filter(entry -> inventoryClazz.isAssignableFrom(entry.getKey()))
                .flatMap(entry -> entry.getValue().stream())
                .forEach(WorldInventoryHolder::onUpdate);
    }

    @Override
    public List<WorldInventoryHolder> getInventories(@NotNull Class<? extends WorldInventoryHolder> inventoryClazz) {
        return this.inventories.get(inventoryClazz);
    }

}
