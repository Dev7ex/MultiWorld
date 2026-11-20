package com.dev7ex.multiworld.api.world.group;

import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 09.07.2025
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldGroupProperty {

    NAME("name", String.class),
    DISPLAY_NAME("display-name", String.class),
    COLOR("color", TextColor.class),
    ICON("icon", Material.class),
    CREATOR_NAME("creator-name", String.class),
    REGISTRATION_TIMESTAMP("registration-timestamp", Long.class);

    private final String key;
    private final Class<?> type;

    WorldGroupProperty(@NotNull final String key, @NotNull final Class<?> type) {
        this.key = key;
        this.type = type;
    }

    public boolean isValidType(@NotNull final Object value) {
        return this.type.isInstance(value);
    }

    @NotNull
    public static Optional<WorldGroupProperty> find(@NotNull final String key) {
        return Arrays.stream(values())
                .filter(property -> property.getKey().equalsIgnoreCase(key))
                .findFirst();
    }

    public static boolean isValidKey(@NotNull final String key) {
        return find(key).isPresent();
    }

}