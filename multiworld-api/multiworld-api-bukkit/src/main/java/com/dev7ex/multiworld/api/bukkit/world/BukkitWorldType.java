package com.dev7ex.multiworld.api.bukkit.world;

import com.dev7ex.multiworld.api.world.WorldType;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public class BukkitWorldType {

    private BukkitWorldType() {
    }

    public static WorldType fromEnvironment(@NotNull final World.Environment environment) {
        return switch (environment) {
            case THE_END -> WorldType.END;
            case NETHER -> WorldType.NETHER;
            default -> WorldType.NORMAL;
        };
    }

}
