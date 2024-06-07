package com.dev7ex.multiworld.api.world;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Represents flags that can be set for a world, such as whether achievements are received or monsters spawn.
 * This enum provides methods to access the storage path and possible values for each flag.
 *
 * @author Dev7ex
 * @since 23.05.2021
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldFlag {

    DIFFICULTY("difficulty", List.of("EASY", "HARD", "NORMAL", "PEACEFUL")),
    END_PORTAL_ACCESSIBLE("end-portal-accessible", List.of("false", "true")),
    GAME_MODE("game-mode", List.of("ADVENTURE", "CREATIVE", "SURVIVAL", "SPECTATOR")),
    LOAD_AUTO("load-auto", List.of("false", "true")),
    NETHER_PORTAL_ACCESSIBLE("nether-portal-accessible", List.of("false", "true")),
    PVP_ENABLED("pvp-enabled", List.of("false", "true")),
    RECEIVE_ACHIEVEMENTS("receive-achievements", List.of("false", "true")),
    SPAWN_ANIMALS("spawn-animals", List.of("false", "true")),
    SPAWN_ENTITIES("spawn-entities", List.of("false", "true")),
    SPAWN_MONSTERS("spawn-monsters", List.of("false", "true")),
    WORLD_TYPE("world-type", WorldType.toStringList());

    private final String storagePath;
    private final List<String> values;

    /**
     * Constructs a WorldFlag with the given storage path and possible values.
     *
     * @param storagePath The storage path of the flag.
     * @param values      The possible values of the flag.
     */
    WorldFlag(@NotNull final String storagePath, @NotNull final List<String> values) {
        this.storagePath = storagePath;
        this.values = values;
    }

    /**
     * Returns a list of string representations of all available WorldFlag values.
     *
     * @return A list of string representations of WorldFlag values.
     */
    public static List<String> toStringList() {
        return Arrays.stream(WorldFlag.values()).map(Enum::name).toList();
    }

    /**
     * Returns an Optional containing the WorldFlag enum constant with the specified name,
     * or an empty Optional if no such constant exists.
     *
     * @param name The name of the WorldFlag constant to find.
     * @return An Optional containing the found WorldFlag constant, or an empty Optional if not found.
     */
    public static Optional<WorldFlag> fromString(final String name) {
        return Arrays.stream(WorldFlag.values()).filter(worldFlag -> worldFlag.name().equalsIgnoreCase(name)).findFirst();
    }

}