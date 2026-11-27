package com.dev7ex.multiworld.api.util;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class BukkitUtil {

    private BukkitUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void checkPrimaryThread() {
        if (!Bukkit.isPrimaryThread()) {
            throw new IllegalStateException(
                    "This operation must run on the primary server thread"
            );
        }
    }

    public static boolean isClassPresent(@NotNull final String className) {
        try {
            Class.forName(className, false, BukkitUtil.class.getClassLoader());
            return true;

        } catch (final ClassNotFoundException exception) {
            return false;
        }
    }

}
