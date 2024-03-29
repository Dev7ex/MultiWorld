package com.dev7ex.multiworld.api.world;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 26.06.2023
 */
public enum WorldEnvironment {

    CUSTOM,
    NORMAL,
    NETHER,
    THE_END;

    public static WorldEnvironment fromType(final WorldType worldType) {
        switch (worldType) {
            case END:
                return WorldEnvironment.THE_END;

            case NETHER:
                return WorldEnvironment.NETHER;

            case NORMAL:
                return WorldEnvironment.NORMAL;

            default:
                return WorldEnvironment.CUSTOM;
        }
    }

    public static List<String> toStringList() {
        return Arrays.stream(WorldEnvironment.values()).map(Enum::name).toList();
    }

    public static Optional<WorldEnvironment> fromString(@NotNull final String name) {
        return Arrays.stream(WorldEnvironment.values()).filter(worldType -> worldType.name().equalsIgnoreCase(name)).findFirst();
    }

}
