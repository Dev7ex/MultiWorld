package com.dev7ex.multiworld.user;

import com.dev7ex.multiworld.api.user.WorldUser;
import com.dev7ex.multiworld.api.user.WorldUserLocation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
    @Getter(AccessLevel.NONE)
    private final Map<NamespacedKey, WorldUserLocation> lastLocations = new ConcurrentHashMap<>();

    public DefaultWorldUser(@NotNull final UUID uniqueId, @NotNull final String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }

    @Override
    public @NotNull Map<NamespacedKey, WorldUserLocation> getLastLocations() {
        return Collections.unmodifiableMap(this.lastLocations);
    }

    @Override
    public @NotNull Optional<Location> getLastLocation(@NotNull final NamespacedKey worldKey) {
        final WorldUserLocation location = this.lastLocations.get(worldKey);

        if (location == null) {
            return Optional.empty();
        }
        final World world = Bukkit.getWorld(worldKey);

        if (world == null) {
            return Optional.empty();
        }
        return Optional.of(new Location(world, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
    }

    @Override
    public void setLastLocation(@NotNull final Location location) {
        if (location.getWorld() == null) {
            return;
        }
        this.setLastLocation(new DefaultWorldUserLocation(location));
    }

    @Override
    public void setLastLocation(@NotNull final WorldUserLocation location) {
        if ((!Double.isFinite(location.getX())) || (!Double.isFinite(location.getY())) || (!Double.isFinite(location.getZ()))
                || (!Float.isFinite(location.getYaw())) || (!Float.isFinite(location.getPitch()))) {
            return;
        }
        this.lastLocations.put(location.getWorldKey(), location);
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
