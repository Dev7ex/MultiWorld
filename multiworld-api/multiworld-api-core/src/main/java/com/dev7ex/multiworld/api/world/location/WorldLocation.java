package com.dev7ex.multiworld.api.world.location;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a location in a world.
 * This interface provides methods for accessing and modifying the coordinates and orientation of the location.
 *
 * @author Dev7ex
 * @since 30.06.2023
 */
public interface WorldLocation {

    /**
     * Gets the name of the world associated with this location.
     *
     * @return The name of the world.
     */
    String getWorldName();

    /**
     * Sets the name of the world associated with this location.
     *
     * @param worldName The name of the world.
     */
    void setWorldName(@NotNull final String worldName);

    /**
     * Gets the x-coordinate of this location.
     *
     * @return The x-coordinate.
     */
    double getX();

    /**
     * Sets the x-coordinate of this location.
     *
     * @param x The x-coordinate.
     */
    void setX(final double x);

    /**
     * Gets the y-coordinate of this location.
     *
     * @return The y-coordinate.
     */
    double getY();

    /**
     * Sets the y-coordinate of this location.
     *
     * @param y The y-coordinate.
     */
    void setY(final double y);

    /**
     * Gets the z-coordinate of this location.
     *
     * @return The z-coordinate.
     */
    double getZ();

    /**
     * Sets the z-coordinate of this location.
     *
     * @param z The z-coordinate.
     */
    void setZ(final double z);

    /**
     * Gets the yaw of this location.
     *
     * @return The yaw.
     */
    double getYaw();

    /**
     * Sets the yaw of this location.
     *
     * @param yaw The yaw.
     */
    void setYaw(final double yaw);

    /**
     * Gets the pitch of this location.
     *
     * @return The pitch.
     */
    double getPitch();

    /**
     * Sets the pitch of this location.
     *
     * @param pitch The pitch.
     */
    void setPitch(final double pitch);

}