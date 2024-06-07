package com.dev7ex.multiworld.api.user;

import com.dev7ex.multiworld.api.world.WorldHolder;
import com.dev7ex.multiworld.api.world.location.WorldLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Represents a user within the multi-world system.
 * This interface provides methods to access user-related information and settings.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldUser {

    /**
     * Gets the unique identifier of the user.
     *
     * @return The UUID of the user.
     */
    UUID getUniqueId();

    /**
     * Gets the name of the user.
     *
     * @return The name of the user.
     */
    String getName();

    /**
     * Gets the configuration of the user.
     *
     * @return The configuration of the user.
     */
    WorldUserConfiguration getConfiguration();

    /**
     * Sets the configuration of the user.
     *
     * @param configuration The new configuration of the user.
     */
    void setConfiguration(@NotNull final WorldUserConfiguration configuration);

    /**
     * Gets the last known location of the user.
     *
     * @return The last known location of the user, or null if not available.
     */
    @Nullable
    WorldLocation getLastLocation();

    /**
     * Sets the last known location of the user.
     *
     * @param location The last known location of the user.
     */
    void setLastLocation(@Nullable final WorldLocation location);

    /**
     * Gets the timestamp of the user's last login.
     *
     * @return The timestamp of the user's last login.
     */
    long getLastLogin();

    /**
     * Sets the timestamp of the user's last login.
     *
     * @param lastLogin The timestamp of the user's last login.
     */
    void setLastLogin(final long lastLogin);

    /**
     * Sends a message to the user.
     *
     * @param message The message to send.
     */
    void sendMessage(@NotNull final String message);

    /**
     * Teleports the user to the specified world.
     *
     * @param worldHolder The WorldHolder representing the destination world.
     */
    void teleport(@NotNull final WorldHolder worldHolder);

}
