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
public enum WorldProperty {

    RECEIVE_ACHIEVEMENTS("receive-achievements", true),
    LOAD_AUTO("load-auto", true),
    CREATOR_NAME("creator-name", true),
    CREATION_TIMESTAMP("creation-timestamp", true),
    TYPE("type", true),
    GAME_MODE("game-mode", true),
    DIFFICULTY("difficulty", true),
    PVP_ENABLED("pvp-enabled", true),
    SPAWN_ANIMALS("spawn-animals", true),
    SPAWN_MONSTERS("spawn-monsters", true),
    SPAWN_ENTITIES("spawn-entities", true),
    END_PORTAL_ACCESSIBLE("end-portal-accessible", true),
    NETHER_PORTAL_ACCESSIBLE("nether-portal-accessible", true),
    END_WORLD("end-world", true),
    NETHER_WORLD("nether-world", true),
    NORMAL_WORLD("normal-world", true),
    WHITELIST("whitelist", true),
    WHITELIST_ENABLED("whitelist-enabled", true);

    private final String storagePath;
    private final boolean modifiable;

    WorldProperty(@NotNull final String storagePath, final boolean modifiable) {
        this.storagePath = storagePath;
        this.modifiable = modifiable;
    }

    public static Optional<WorldProperty> fromStoragePath(@NotNull final String storagePath) {
        return Arrays.stream(WorldProperty.values()).filter(property -> property.getStoragePath().equalsIgnoreCase(storagePath)).findFirst();
    }

}
