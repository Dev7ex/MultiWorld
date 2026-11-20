package com.dev7ex.multiworld.api.world;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public enum WorldState {

    UNLOADED,
    LOADING,
    LOADED,
    UNLOADING,
    CREATING,
    DELETING,
    FAILED;

    public String getKey() {
        return name().toLowerCase(Locale.ROOT);
    }

    public boolean is(WorldState state) {
        return this == state;
    }

    public boolean isAny(WorldState... states) {
        for (WorldState state : states) {
            if (this == state) {
                return true;
            }
        }
        return false;
    }

    public static Optional<WorldState> fromString(String value) {
        if (value == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(
                    valueOf(value.trim().toUpperCase(Locale.ROOT))
            );
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    public static boolean exists(String value) {
        return fromString(value).isPresent();
    }

    public static List<String> names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .toList();
    }

    public static List<String> keys() {
        return Arrays.stream(values())
                .map(WorldState::getKey)
                .toList();
    }
}