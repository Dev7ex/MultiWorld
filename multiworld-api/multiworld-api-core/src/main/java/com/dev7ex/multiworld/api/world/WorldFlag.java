package com.dev7ex.multiworld.api.world;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 23.05.2021
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldFlag {

    DIFFICULTY("difficulty", List.of("EASY", "HARD", "NORMAL", "PEACEFUL")),
    GAMEMODE("game-mode", List.of("ADVENTURE", "CREATIVE", "SURVIVAL", "SPECTATOR")),
    SPAWN_MONSTERS("spawn-monsters", List.of("false", "true")),
    SPAWN_ANIMALS("spawn-animals", List.of("false", "true")),
    END_PORTAL_ACCESSIBLE("end-portal-accessible", List.of("false", "true")),
    NETHER_PORTAL_ACCESSIBLE("nether-portal-accessible", List.of("false", "true")),
    PVP_ENABLED("pvp-enabled", List.of("false", "true")),
    WORLD_TYPE("world-type", WorldType.toStringList());

    private final String storagePath;
    private final List<String> values;

    WorldFlag(@NotNull final String storagePath, @NotNull final List<String> values) {
        this.storagePath = storagePath;
        this.values = values;
    }

    public static List<String> toStringList() {
        return Arrays.stream(WorldFlag.values()).map(Enum::name).toList();
    }

    public static Optional<WorldFlag> fromString(final String name) {
        return Arrays.stream(WorldFlag.values()).filter(worldType -> worldType.name().equalsIgnoreCase(name)).findFirst();
    }

}
