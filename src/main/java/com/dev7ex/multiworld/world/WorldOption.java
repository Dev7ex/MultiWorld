package com.dev7ex.multiworld.world;

import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 23.05.2021
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldOption {

    SPAWN_MONSTERS("spawn-monsters", Lists.newArrayList("false", "true")),
    SPAWN_ANIMALS("spawn-animals", Lists.newArrayList("false", "true")),
    GAMEMODE("gamemode", Lists.newArrayList("ADVENTURE", "CREATIVE", "SURVIVAL", "SPECTATOR")),
    PVP_ENABLED("pvp", Lists.newArrayList("false", "true")),
    DIFFICULTY("difficulty", Lists.newArrayList("EASY", "HARD", "NORMAL", "PEACEFUL")),
    WORLD_TYPE("world-type", WorldType.toStringList());

    private final String storagePath;
    private final List<String> values;

    WorldOption(final String storagePath, final List<String> values) {
        this.storagePath = storagePath;
        this.values = values;
    }

    public static List<String> toStringList() {
        final List<String> constants = Lists.newArrayList();

        for (final WorldOption option : WorldOption.values()) {
            constants.add(option.name());
        }
        return constants;
    }

    public static Optional<WorldOption> fromString(final String name) {
        return Arrays.stream(WorldOption.values()).filter(worldOption -> worldOption.name().equalsIgnoreCase(name)).findFirst();
    }

}
