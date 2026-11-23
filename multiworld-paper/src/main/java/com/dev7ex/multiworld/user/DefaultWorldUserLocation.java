package com.dev7ex.multiworld.user;

import com.dev7ex.multiworld.api.user.WorldUserLocation;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author MGG
 * @since 23.11.2026
 */
@Getter(AccessLevel.PUBLIC)
public class DefaultWorldUserLocation implements WorldUserLocation {

    private final NamespacedKey worldKey;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public DefaultWorldUserLocation(@NotNull final NamespacedKey worldKey, final double x, final double y, final double z, final float yaw, final float pitch) {
        this.worldKey = worldKey;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public DefaultWorldUserLocation(@NotNull final Location location) {
        this(Objects.requireNonNull(location.getWorld()).getKey(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

}
