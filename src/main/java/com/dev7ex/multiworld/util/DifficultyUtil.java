package com.dev7ex.multiworld.util;

import org.bukkit.Difficulty;

/**
 * @author Dev7ex
 * @since 24.06.21, 20:52
 */
public final class DifficultyUtil {

    public static boolean isAllowSpawnMonsters(final Difficulty difficulty) {
        if (difficulty == Difficulty.PEACEFUL) {
            return false;
        }
        return true;
    }

}
