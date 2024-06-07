package com.dev7ex.multiworld.api.bukkit.world;

import com.dev7ex.multiworld.api.world.WorldType;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for converting Bukkit world environments to corresponding WorldType.
 * This class provides static methods for mapping Bukkit world environments to WorldType enum values.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
public class BukkitWorldType {

    // Private constructor to prevent instantiation of this utility class
    private BukkitWorldType() {}

    /**
     * Converts a Bukkit world environment to a corresponding WorldType enum value.
     *
     * @param environment The Bukkit world environment.
     * @return The corresponding WorldType.
     */
    public static WorldType fromEnvironment(@NotNull final World.Environment environment) {
        return switch (environment) {
            case THE_END -> WorldType.END;
            case NETHER -> WorldType.NETHER;
            default -> WorldType.NORMAL;
        };
    }

}
