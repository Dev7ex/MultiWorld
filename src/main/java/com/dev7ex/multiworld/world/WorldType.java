package com.dev7ex.multiworld.world;

import com.google.common.collect.Lists;

import lombok.Getter;

import org.bukkit.World;

import java.util.List;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@Getter
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
        switch (environment) {
            case NETHER:
                return WorldType.NETHER;
            case THE_END:
                return WorldType.END;
            default:
                return WorldType.NORMAL;
        }
    }

    public static List<String> toStringList() {
        final List<String> constants = Lists.newArrayList();

        for (final WorldType type : WorldType.values()) {
            constants.add(type.name());
        }
        return constants;
    }

}
