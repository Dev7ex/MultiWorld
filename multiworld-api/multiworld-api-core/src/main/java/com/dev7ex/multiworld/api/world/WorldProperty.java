package com.dev7ex.multiworld.api.world;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldProperty {

    CREATOR_NAME("creator-name", true),
    CREATION_TIMESTAMP("creation-timestamp", true),
    TYPE("type", true),
    GAME_MODE("game-mode", true),
    DIFFICULTY("difficulty", true),
    PVP_ENABLED("pvp-enabled", true),
    SPAWN_ANIMALS("spawn-animals", true),
    SPAWN_MONSTERS("spawn-monsters", true),
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

}
