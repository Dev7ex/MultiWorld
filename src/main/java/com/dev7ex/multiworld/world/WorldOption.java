package com.dev7ex.multiworld.world;

import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;

/**
 * @author Dev7ex
 * @since 23.05.2021
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldOption {

    SPAWN_MONSTERS("spawn-monsters", Lists.newArrayList("false", "true")),
    SPAWN_ANIMALS("spawn-animals", Lists.newArrayList("false", "true")),
    GAME_MODE("game-mode", Lists.newArrayList("ADVENTURE", "CREATIVE", "SURVIVAL", "SPECTATOR")),
    PVP("pvp-enabled", Lists.newArrayList("false", "true")),
    DIFFICULTY("difficulty", Lists.newArrayList("EASY", "HARD", "NORMAL", "PEACEFUL")),
    WORLD_TYPE("world-type", WorldType.toStringList());

    private final String configEntry;
    private final List<String> values;

    WorldOption(final String configEntry, final List<String> values) {
        this.configEntry = configEntry;
        this.values = values;
    }

    public static List<String> toStringList() {
        final List<String> constants = Lists.newArrayList();

        for (final WorldOption option : WorldOption.values()) {
            constants.add(option.name());
        }
        return constants;
    }

}
