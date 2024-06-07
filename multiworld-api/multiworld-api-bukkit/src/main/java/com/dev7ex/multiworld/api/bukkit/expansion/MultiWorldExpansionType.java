package com.dev7ex.multiworld.api.bukkit.expansion;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Enum representing different types of placeholders in the MultiWorld expansion.
 * Each type corresponds to a specific piece of world-related information.
 *
 * @author Dev7ex
 * @since 18.07.2023
 */
@Getter(AccessLevel.PUBLIC)
public enum MultiWorldExpansionType {

    PLAYER_COUNT("_player_count", true),
    GAME_MODE("_game_mode", false),
    DIFFICULTY("_difficulty", false),
    SPAWN_ANIMALS("_spawn_animals", false),
    SPAWN_MONSTERS("_spawn_monsters", false),
    PVP_ENABLED("_pvp-enabled", false),
    TYPE("_type", false);

    private final String parameter;
    private final boolean requiresLoadedWorld;

    /**
     * Constructs a MultiWorldExpansionType with the given parameter and requirement for loaded world.
     *
     * @param parameter           The parameter associated with this type of placeholder.
     * @param requiresLoadedWorld Whether this type requires the world to be loaded.
     */
    MultiWorldExpansionType(@NotNull final String parameter, final boolean requiresLoadedWorld) {
        this.parameter = parameter;
        this.requiresLoadedWorld = requiresLoadedWorld;
    }

    /**
     * Retrieves the MultiWorldExpansionType associated with the given parameter.
     *
     * @param parameter The parameter to search for.
     * @return An Optional containing the MultiWorldExpansionType if found, empty otherwise.
     */
    public static Optional<MultiWorldExpansionType> getByParameter(@NotNull final String parameter) {
        for (final MultiWorldExpansionType type : MultiWorldExpansionType.values()) {
            if (parameter.contains(type.getParameter())) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

}