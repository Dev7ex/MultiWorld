package com.dev7ex.multiworld.api.world;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 26.06.2023
 */
public enum WorldEnvironment {

    NORMAL,
    NETHER,
    THE_END;

    public static List<String> toStringList() {
        return Arrays.stream(WorldEnvironment.values()).map(Enum::name).toList();
    }

    public static Optional<WorldEnvironment> fromString(final String name) {
        return Arrays.stream(WorldEnvironment.values()).filter(worldType -> worldType.name().equalsIgnoreCase(name)).findFirst();
    }

}
