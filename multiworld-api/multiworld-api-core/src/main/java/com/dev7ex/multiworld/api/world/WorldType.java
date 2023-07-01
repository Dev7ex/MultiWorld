package com.dev7ex.multiworld.api.world;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldType {

    CUSTOM(false),
    END(false),
    FLAT(true),
    NETHER(false),
    NORMAL(true),
    VOID(true);

    private final boolean overWorld;

    WorldType(final boolean overWorld) {
        this.overWorld = overWorld;
    }

    public static List<String> toStringList() {
        return Arrays.stream(WorldType.values()).map(Enum::name).toList();
    }

    public static Optional<WorldType> fromString(final String name) {
        return Arrays.stream(WorldType.values()).filter(worldType -> worldType.name().equalsIgnoreCase(name)).findFirst();
    }

}
