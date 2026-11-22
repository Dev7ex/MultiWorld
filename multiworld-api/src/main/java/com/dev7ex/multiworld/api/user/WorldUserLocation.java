package com.dev7ex.multiworld.api.user;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * @author MGG
 * @since 23.11.2026
 */
public interface WorldUserLocation {

    @NotNull
    NamespacedKey getWorldKey();

    double getX();

    double getY();

    double getZ();

    float getYaw();

    float getPitch();

}
