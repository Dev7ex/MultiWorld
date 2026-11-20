package com.dev7ex.multiworld.api.inventory.menu.main;

import com.dev7ex.multiworld.api.inventory.WorldInventoryHolder;
import com.dev7ex.multiworld.api.world.WorldContext;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Dev7ex
 * @since 06.07.2025
 */
public interface WorldMainMenu extends WorldInventoryHolder {

    void populateWorlds(@NotNull final Player player);

    void populateNavigationBar(@NotNull final Player player);

    List<WorldContext> getWorlds();

}
