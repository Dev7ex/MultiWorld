package com.dev7ex.multiworld.api.bukkit.world.location;

import com.dev7ex.multiworld.api.world.location.WorldLocation;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dev7ex
 * @since 30.06.2023
 */
@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Builder(setterPrefix = "set")
@NoArgsConstructor
@SerializableAs("BukkitWorldLocation")
public class BukkitWorldLocation implements WorldLocation, ConfigurationSerializable {

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

    private BukkitWorldLocation(@NotNull final Map<String, Object> serializeData) {
        this.worldName = (String) serializeData.get("world-name");
        this.x = (double) serializeData.get("x");
        this.y = (double) serializeData.get("y");
        this.z = (double) serializeData.get("z");

        if (serializeData.containsKey("yaw")) {
            this.yaw = (double) serializeData.get("yaw");
        }

        if (serializeData.containsKey("pitch")) {
            this.pitch = (double) serializeData.get("pitch");
        }
    }

    public static BukkitWorldLocation deserialize(@NotNull final Map<String, Object> serializeData) {
        return new BukkitWorldLocation(serializeData);
    }

    public static BukkitWorldLocation valueOf(@NotNull final Map<String, Object> serializeData) {
        return new BukkitWorldLocation(serializeData);
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

    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> serializeData = new HashMap<>();

        serializeData.put("world-name", this.worldName);
        serializeData.put("x", this.x);
        serializeData.put("y", this.y);
        serializeData.put("z", this.z);
        serializeData.put("yaw", this.yaw);
        serializeData.put("pitch", this.pitch);
        return serializeData;
    }

}
