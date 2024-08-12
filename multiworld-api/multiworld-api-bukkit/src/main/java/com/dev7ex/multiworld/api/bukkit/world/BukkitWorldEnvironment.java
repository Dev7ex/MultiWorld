package com.dev7ex.multiworld.api.bukkit.world;

import com.dev7ex.multiworld.api.world.WorldEnvironment;
import com.dev7ex.multiworld.api.world.WorldType;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 29.06.2024
 */
public class BukkitWorldEnvironment {

    public static World.Environment from(@NotNull final WorldEnvironment environment) {
        return switch (environment) {
            case NORMAL -> World.Environment.NORMAL;
            case NETHER -> World.Environment.NETHER;
            case THE_END -> World.Environment.THE_END;
            default -> World.Environment.CUSTOM;
        };
    }

    public static World.Environment from(@NotNull final WorldType type) {
        return switch (type) {
            case NORMAL -> World.Environment.NORMAL;
            case NETHER -> World.Environment.NETHER;
            case END -> World.Environment.THE_END;
            default -> World.Environment.CUSTOM;
        };
    }

}
