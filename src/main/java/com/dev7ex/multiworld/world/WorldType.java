package com.dev7ex.multiworld.world;

import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.World;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldType {

    NORMAL(World.Environment.NORMAL, true),
    FLAT(World.Environment.CUSTOM, true),
    END(World.Environment.THE_END, false),
    NETHER(World.Environment.NETHER, false),
    VOID(World.Environment.CUSTOM, true);

    private final World.Environment environment;
    private final boolean overWorld;

    WorldType(final World.Environment environment, final boolean overWorld) {
        this.environment = environment;
        this.overWorld = overWorld;
    }

    public static WorldType getByEnvironment(final World.Environment environment) {
        return switch (environment) {
            case NETHER -> WorldType.NETHER;
            case THE_END -> WorldType.END;
            default -> WorldType.NORMAL;
        };
    }

    public static List<String> toStringList() {
        return Arrays.stream(WorldType.values()).map(Enum::name).collect(Collectors.toList());
    }

    public static Optional<WorldType> fromString(final String name) {
        return Arrays.stream(WorldType.values()).filter(worldType -> worldType.name().equalsIgnoreCase(name)).findFirst();
    }

}
