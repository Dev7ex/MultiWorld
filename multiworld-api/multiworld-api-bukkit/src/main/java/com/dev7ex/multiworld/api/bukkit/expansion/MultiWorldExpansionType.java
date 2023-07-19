package com.dev7ex.multiworld.api.bukkit.expansion;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Dev7ex
 * @since 18.07.2023
 */
@Getter(AccessLevel.PUBLIC)
public enum MultiWorldExpansionType {

    PLAYER_COUNT("_player_count", true),
    GAME_MODE("_game_mode", false),
    DIFFICULTY("_difficulty", false),
    SPAWN_ANIMALS("_spawn_monsters", false),
    SPAWN_MONSTERS("_spawn_animals", false),
    PVP_ENABLED("_pvp-enabled", false),
    TYPE("_type", false);

    private final String parameter;
    private final boolean requiresLoadedWorld;

    MultiWorldExpansionType(@NotNull final String parameter, final boolean requiresLoadedWorld) {
        this.parameter = parameter;
        this.requiresLoadedWorld = requiresLoadedWorld;
    }

    public static Optional<MultiWorldExpansionType> getByParameter(@NotNull final String parameter) {
        for (final MultiWorldExpansionType type : MultiWorldExpansionType.values()) {
            if (parameter.contains(type.getParameter())) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

}
