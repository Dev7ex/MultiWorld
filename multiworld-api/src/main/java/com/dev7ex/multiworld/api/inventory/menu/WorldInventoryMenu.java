package com.dev7ex.multiworld.api.inventory.menu;

import com.dev7ex.multicommon.api.inventory.InventoryMenu;
import com.dev7ex.multicommon.api.inventory.InventoryMenuContext;
import com.dev7ex.multiworld.api.inventory.WorldInventoryHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Dev7ex
 * @since 19.11.2026
 */
public abstract class WorldInventoryMenu extends InventoryMenu implements WorldInventoryHolder {

    protected WorldInventoryMenu(@NotNull Player player, @NotNull Component title, int rows) {
        super(player, title, rows);
    }

    protected WorldInventoryMenu(@NotNull final Player player, @NotNull final Component title, final int rows, @Nullable final InventoryMenuContext parent) {
        super(player, title, rows, parent);
    }

}
