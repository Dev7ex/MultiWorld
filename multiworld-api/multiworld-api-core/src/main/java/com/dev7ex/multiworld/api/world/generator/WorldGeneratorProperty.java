package com.dev7ex.multiworld.api.world.generator;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 07.06.2024
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldGeneratorProperty {

    NAME("name"),
    AUTHOR("author"),
    SETTINGS_SPAWN_X("settings.spawn.x"),
    SETTINGS_SPAWN_Y("settings.spawn.y"),
    SETTINGS_SPAWN_Z("settings.spawn.z"),
    ENVIRONMENT("environment"),
    SEED("seed"),
    TYPE("type"),
    GENERATOR("generator"),
    HARDCORE("hardcore"),
    GENERATE_STRUCTURES("generate-structures"),
    SINGLE_BIOME("single-biome"),
    BIOME("biome"),
    BIOMES("biomes");

    private final String storagePath;

    WorldGeneratorProperty(@NotNull final String storagePath) {
        this.storagePath = storagePath;
    }

}
