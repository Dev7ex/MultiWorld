package com.dev7ex.multiworld.api.world;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter(AccessLevel.PUBLIC)
public enum WorldType {

    NORMAL("Normal"),
    FLAT("Flat"),
    LARGE_BIOMES("Large Biomes"),
    AMPLIFIED("Amplified");

    private final String displayName;

    WorldType(@NotNull final String displayName) {
        this.displayName = displayName;
    }

    public org.bukkit.WorldType asBukkit() {
        return switch (this) {
            case LARGE_BIOMES -> org.bukkit.WorldType.LARGE_BIOMES;
            case FLAT -> org.bukkit.WorldType.FLAT;
            case AMPLIFIED -> org.bukkit.WorldType.AMPLIFIED;
            default -> org.bukkit.WorldType.NORMAL;
        };
    }

    public static WorldType ofBukkit(@NotNull final org.bukkit.WorldType worldType) {
        return switch (worldType) {
            case NORMAL -> WorldType.NORMAL;
            case FLAT -> FLAT;
            case LARGE_BIOMES -> LARGE_BIOMES;
            case AMPLIFIED -> AMPLIFIED;
        };
    }

    public static List<String> names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .toList();
    }

    public static Optional<WorldType> fromString(@NotNull final String name) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst();
    }

}
