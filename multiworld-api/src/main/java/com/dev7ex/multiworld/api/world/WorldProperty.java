package com.dev7ex.multiworld.api.world;

import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldProperty {

    KEY("key", String.class),
    NAME("name", String.class),
    DISPLAY_NAME("display-name", String.class),
    COLOR("color", TextColor.class),
    ICON("icon", Material.class),
    CREATOR_NAME("creator-name", String.class),
    REGISTRATION_TIMESTAMP("registration-timestamp", Long.class),
    GROUP_ID("group-id", Integer.class),

    ENVIRONMENT("meta.generation.environment", WorldEnvironment.class),
    SEED("meta.generation.seed", Long.class),
    TYPE("meta.generation.type", WorldType.class),
    GENERATOR_NAME("meta.generation.generator-name", String.class),
    DIFFICULTY("meta.game.difficulty", Difficulty.class);

    private final String key;
    private final Class<?> type;

    WorldProperty(@NotNull final String key, @NotNull final Class<?> type) {
        this.key = key;
        this.type = type;
    }

}
