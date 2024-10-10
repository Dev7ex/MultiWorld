package com.dev7ex.multiworld.api.bukkit.world.location;

import com.dev7ex.multiworld.api.world.location.WorldLocation;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 30.06.2023
 */
@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Builder(setterPrefix = "set")
@NoArgsConstructor
public class BukkitWorldLocation implements WorldLocation {

    private String worldName;
    private double x;
    private double z;
    private double y;
    private double yaw;
    private double pitch;

    public BukkitWorldLocation(@NotNull final String worldName, final double x, final double y, final double z, final double yaw, final double pitch) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static BukkitWorldLocation of(@NotNull final Location location) {
        final BukkitWorldLocation worldLocation = new BukkitWorldLocation();
        worldLocation.setWorldName(location.getWorld().getName());
        worldLocation.setX(location.getX());
        worldLocation.setY(location.getY());
        worldLocation.setZ(location.getZ());
        worldLocation.setYaw(location.getYaw());
        worldLocation.setPitch(location.getPitch());
        return worldLocation;
    }

    public static Location to(@NotNull final WorldLocation location) {
        return new Location(Bukkit.getWorld(location.getWorldName()), location.getX(), location.getY(), location.getZ(), (float) location.getYaw(), (float) location.getPitch());
    }

}
