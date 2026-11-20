package com.dev7ex.multiworld.api.world;

import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dev7ex
 * @since 26.01.2025
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldEnvironment {

    NORMAL("normal", "Normal", TextColor.color(0, 255, 0)),
    NETHER("nether", "Nether", TextColor.color(255, 69, 0)),
    THE_END("the_end", "The End", TextColor.color(128, 0, 128));

    private final String key;
    private final String displayName;
    private final TextColor color;

    WorldEnvironment(@NotNull final String key, @NotNull final String displayName, @NotNull final TextColor color) {
        this.key = key;
        this.displayName = displayName;
        this.color = color;
    }

    public World.Environment asBukkit() {
        return switch (this) {
            case NORMAL -> World.Environment.NORMAL;
            case NETHER -> World.Environment.NETHER;
            case THE_END -> World.Environment.THE_END;
        };
    }

    public static WorldEnvironment ofBukkit(@NotNull final World.Environment environment) {
        return switch (environment) {
            case THE_END -> WorldEnvironment.THE_END;
            case NETHER -> WorldEnvironment.NETHER;
            default -> WorldEnvironment.NORMAL;
        };
    }

    public static List<String> names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .toList();
    }

}
