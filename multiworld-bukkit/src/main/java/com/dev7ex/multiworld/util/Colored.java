package com.dev7ex.multiworld.util;

import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 22.10.2024
 */
public class Colored {

    public static String getColoredBoolean(final boolean value) {
        if (value) {
            return ChatColor.GREEN + "true";
        }
        return ChatColor.RED + "false";
    }

    public static String getColoredGameMode(@NotNull final GameMode gameMode) {
        return switch (gameMode) {
            case ADVENTURE, SURVIVAL -> ChatColor.GREEN + gameMode.toString();
            case CREATIVE -> ChatColor.RED + gameMode.toString();
            case SPECTATOR -> ChatColor.DARK_GREEN + gameMode.toString();
        };
    }

    public static String getColoredBooleanWithOption(@NotNull final String value) {
        if (value.equalsIgnoreCase("true")) {
            return ChatColor.GREEN + "true";
        } else if (value.equalsIgnoreCase("false")) {
            return ChatColor.RED + "false";
        } else {
            return ChatColor.YELLOW + value;
        }
    }

    public static String getColoredDifficulty(@NotNull final Difficulty difficulty) {
        return switch (difficulty) {
            case PEACEFUL -> ChatColor.GREEN + difficulty.toString();
            case EASY -> ChatColor.DARK_GREEN + difficulty.toString();
            case NORMAL -> ChatColor.YELLOW + difficulty.toString();
            case HARD -> ChatColor.RED + difficulty.toString();
        };
    }

}
