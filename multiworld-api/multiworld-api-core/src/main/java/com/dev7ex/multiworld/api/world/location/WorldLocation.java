package com.dev7ex.multiworld.api.world.location;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author Dev7ex
 * @since 30.06.2023
 */
public interface WorldLocation {

    String getWorldName();

    void setWorldName(@NotNull final String worldName);

    double getX();

    void setX(final double x);

    double getY();

    void setY(final double y);

    double getZ();

    void setZ(final double z);

    double getYaw();

    void setYaw(final double yaw);

    double getPitch();

    void setPitch(final double pitch);

    Map<String, Object> serialize();

}
