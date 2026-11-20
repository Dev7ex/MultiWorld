package com.dev7ex.multiworld.api.user;

import com.dev7ex.multiworld.api.user.offline.WorldOfflineUser;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldUser extends WorldOfflineUser {

    long getFirstLogin();

    void setFirstLogin(final long firstLogin);

    long getLastLogin();

    void setLastLogin(final long lastLogin);

    void sendMessage(@NotNull final Component component);

    void openInventory(@NotNull final InventoryHolder holder);

    Player getEntity();

}
