package com.dev7ex.multiworld.user;

import com.dev7ex.multiworld.api.user.WorldUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Dev7ex
 * @since 13.04.2025
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class DefaultWorldUser implements WorldUser {

    private final UUID uniqueId;
    private final String name;
    private volatile long firstLogin;
    private volatile long lastLogin;

    public DefaultWorldUser(@NotNull final UUID uniqueId, @NotNull final String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }

    @Override
    public void sendMessage(@NotNull final Component component) {
        this.getEntity().sendMessage(component);
    }

    @Override
    public void openInventory(@NotNull final InventoryHolder holder) {
        this.getEntity().getOpenInventory().close();
        this.getEntity().openInventory(holder.getInventory());
    }

    @Override
    public Player getEntity() {
        return Bukkit.getPlayer(this.uniqueId);
    }

}
