package com.dev7ex.multiworld.api.world;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldDefaultProperty {

    DIFFICULTY("difficulty", "settings.defaults.difficulty"),
    GAME_MODE("game-mode", "settings.defaults.game-mode"),
    PVP_ENABLED("pvp-enabled", "settings.defaults.pvp-enabled"),
    SPAWN_ANIMALS("spawn-animals", "settings.defaults.spawn-animals"),
    SPAWN_MONSTERS("spawn-monsters", "settings.defaults.spawn-monsters"),
    WORLD("world", "settings.defaults.world"),
    END_WORLD("end-world", "settings.defaults.end-world"),
    NETHER_WORLD("nether-world", "settings.defaults.nether-world"),
    NORMAL_WORLD("world", "settings.defaults.world"),
    WHITELIST_ENABLED("whitelist-enabled", "settings.defaults.whitelist-enabled");

    private final String name;
    private final String storagePath;

    WorldDefaultProperty(@NotNull final String name, @NotNull final String storagePath) {
        this.name = name;
        this.storagePath = storagePath;
    }

    public static Optional<WorldDefaultProperty> fromString(final String name) {
        return Arrays.stream(WorldDefaultProperty.values()).filter(worldType -> worldType.name().equalsIgnoreCase(name)).findFirst();
    }

}
