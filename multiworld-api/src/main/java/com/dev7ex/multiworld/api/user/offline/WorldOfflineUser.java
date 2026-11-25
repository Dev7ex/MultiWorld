package com.dev7ex.multiworld.api.user.offline;

import com.dev7ex.multiworld.api.user.WorldUserSkin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    WorldUserSkin getSkin();

}
