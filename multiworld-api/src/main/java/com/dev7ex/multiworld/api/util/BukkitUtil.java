package com.dev7ex.multiworld.api.util;

import org.bukkit.Bukkit;

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
}