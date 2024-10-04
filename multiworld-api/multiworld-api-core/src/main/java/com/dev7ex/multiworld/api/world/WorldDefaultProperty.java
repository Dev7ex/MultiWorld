package com.dev7ex.multiworld.api.world;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents default properties for worlds.
 * This enum provides methods to obtain default property values and their storage paths.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldDefaultProperty {

    AUTO_LOAD_ENABLED("auto-load-enabled", "settings.defaults.auto-load-enabled"),
    AUTO_UNLOAD_ENABLED("auto-unload-enabled", "settings.defaults.auto-unload-enabled"),
    DIFFICULTY("difficulty", "settings.defaults.difficulty"),
    END_PORTAL_ACCESSIBLE("end-portal-accessible", "settings.defaults.end-portal-accessible"),
    END_WORLD("end-world", "settings.defaults.end-world"),
    GAME_MODE("game-mode", "settings.defaults.game-mode"),
    HUNGER_ENABLED("hunger-enabled", "settings.defaults.hunger-enabled"),
    KEEP_SPAWN_IN_MEMORY("keep-spawn-in-memory", "settings.defaults.keep-spawn-in-memory"),
    NETHER_PORTAL_ACCESSIBLE("nether-portal-accessible", "settings.defaults.nether-portal-accessible"),
    NETHER_WORLD("nether-world", "settings.defaults.nether-world"),
    NORMAL_WORLD("world", "settings.defaults.normal-world"),
    PVP_ENABLED("pvp-enabled", "settings.defaults.pvp-enabled"),
    RECEIVE_ACHIEVEMENTS("receive-achievements", "settings.defaults.receive-achievements"),
    REDSTONE_ENABLED("redstone-enabled", "settings.defaults.redstone-enabled"),
    SPAWN_ANIMALS("spawn-animals", "settings.defaults.spawn-animals"),
    SPAWN_ENTITIES("spawn-entities", "settings.defaults.spawn-entities"),
    SPAWN_MONSTERS("spawn-monsters", "settings.defaults.spawn-monsters"),
    WEATHER_ENABLED("weather-enabled", "settings.defaults.weather-enabled"),
    WHITELIST_ENABLED("whitelist-enabled", "settings.defaults.whitelist-enabled"),
    WORLD("world", "settings.defaults.normal-world");



    private final String name;
    private final String storagePath;

    WorldDefaultProperty(@NotNull final String name, @NotNull final String storagePath) {
        this.name = name;
        this.storagePath = storagePath;
    }

    /**
     * Returns an Optional containing the WorldDefaultProperty enum constant with the specified name,
     * or an empty Optional if no such constant exists.
     *
     * @param name The name of the WorldDefaultProperty constant to find.
     * @return An Optional containing the found WorldDefaultProperty constant, or an empty Optional if not found.
     */
    public static Optional<WorldDefaultProperty> fromString(final String name) {
        return Arrays.stream(WorldDefaultProperty.values()).filter(property -> property.name.equalsIgnoreCase(name)).findFirst();
    }

}