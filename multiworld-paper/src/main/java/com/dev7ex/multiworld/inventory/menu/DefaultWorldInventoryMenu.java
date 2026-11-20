package com.dev7ex.multiworld.inventory.menu;

import com.dev7ex.multicommon.api.inventory.InventoryMenuContext;
import com.dev7ex.multiworld.api.inventory.menu.WorldInventoryMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Dev7ex
 * @since 06.07.2025
 */
public abstract class DefaultWorldInventoryMenu extends WorldInventoryMenu {

    public DefaultWorldInventoryMenu(@NotNull final Player player, @NotNull final Component title, final int rows) {
        super(player, title, rows);
    }

    public DefaultWorldInventoryMenu(@NotNull final Player player, @NotNull final Component title, final int rows, @Nullable final InventoryMenuContext parent) {
        super(player, title, rows, parent);
    }

}
