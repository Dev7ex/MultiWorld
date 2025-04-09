package com.dev7ex.multiworld.api.world;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents properties associated with a world.
 * Each property has a storage path and can be modifiable.
 * Provides methods to retrieve properties based on their storage path.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldProperty {

    AUTO_LOAD_ENABLED("flag.auto-load-enabled", true),
    AUTO_UNLOAD_ENABLED("flag.auto-unload-enabled", true),
    CREATION_TIMESTAMP("creation-timestamp", true),
    CREATOR_NAME("creator-name", true),
    DIFFICULTY("flag.difficulty", true),
    END_PORTAL_ACCESSIBLE("flag.end-portal-accessible", true),
    ENVIRONMENT("generation.environment", false),
    GAME_MODE("flag.game-mode", true),
    FORCE_GAME_MODE("flag.force-game-mode", true),
    GENERATOR("generation.generator", false),
    HUNGER_ENABLED("flag.hunger-enabled", true),
    KEEP_SPAWN_IN_MEMORY("flag.keep-spawn-in-memory", true),
    LINKED_END_WORLD("end-world", true),
    LINKED_NETHER_WORLD("nether-world", true),
    LINKED_OVERWORLD("overworld", true),
    NETHER_PORTAL_ACCESSIBLE("flag.nether-portal-accessible", true),
    PVP_ENABLED("flag.pvp-enabled", true),
    RECEIVE_ACHIEVEMENTS("flag.receive-achievements", true),
    REDSTONE_ENABLED("flag.redstone-enabled", true),
    SPAWN_ANIMALS("flag.spawn-animals", true),
    SPAWN_ENTITIES("flag.spawn-entities", true),
    SPAWN_MONSTERS("flag.spawn-monsters", true),
    TYPE("generation.type", true),
    WEATHER_ENABLED("flag.weather-enabled", true),
    WHITELIST("whitelist.entries", true),
    WHITELIST_ENABLED("whitelist.enabled", true);

    private final String storagePath;
    private final boolean modifiable;

    WorldProperty(@NotNull final String storagePath, final boolean modifiable) {
        this.storagePath = storagePath;
        this.modifiable = modifiable;
    }

    /**
     * Retrieves a WorldProperty enum constant based on its storage path.
     *
     * @param storagePath The storage path of the property.
     * @return An Optional containing the matching WorldProperty, or empty if not found.
     */
    public static Optional<WorldProperty> fromStoragePath(@NotNull final String storagePath) {
        return Arrays.stream(WorldProperty.values())
                .filter(property -> property.getStoragePath().equalsIgnoreCase(storagePath))
                .findFirst();
    }

}