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

    CREATION_TIMESTAMP("creation-timestamp", true),
    CREATOR_NAME("creator-name", true),
    DIFFICULTY("difficulty", true),
    END_PORTAL_ACCESSIBLE("end-portal-accessible", true),
    END_WORLD("end-world", true),
    GAME_MODE("game-mode", true),
    LOAD_AUTO("load-auto", true),
    NORMAL_WORLD("normal-world", true),
    NETHER_PORTAL_ACCESSIBLE("nether-portal-accessible", true),
    NETHER_WORLD("nether-world", true),
    PVP_ENABLED("pvp-enabled", true),
    RECEIVE_ACHIEVEMENTS("receive-achievements", true),
    SPAWN_ANIMALS("spawn-animals", true),
    SPAWN_ENTITIES("spawn-entities", true),
    SPAWN_MONSTERS("spawn-monsters", true),
    TYPE("type", true),
    WHITELIST("whitelist", true),
    WHITELIST_ENABLED("whitelist-enabled", true);

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