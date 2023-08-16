package com.dev7ex.multiworld.api.user;

import com.dev7ex.multiworld.api.world.location.WorldLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author Dev7ex
 * @since 18.06.2023
 *
 * Represents a User
 *
 */
public interface WorldUser {

    UUID getUniqueId();

    String getName();

    WorldUserConfiguration getConfiguration();

    void setConfiguration(@NotNull final WorldUserConfiguration configuration);

    @Nullable
    WorldLocation getLastLocation();

    void setLastLocation(@Nullable final WorldLocation location);

    void sendMessage(@NotNull final String message);

}
