package com.dev7ex.multiworld.api.user.offline;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Dev7ex
 * @since 15.06.2025
 */
public interface WorldOfflineUser {

    @NotNull
    UUID getUniqueId();

    @NotNull
    String getName();

}
