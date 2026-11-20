package com.dev7ex.multiworld.api.world;

import com.dev7ex.multiworld.api.world.result.WorldLifecycleResult;
import net.kyori.adventure.audience.Audience;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 02.11.2026
 */
public interface WorldManager {

    @NotNull
    WorldLifecycleResult load(@NotNull final Audience audience, @NotNull final NamespacedKey key);

    @NotNull
    WorldLifecycleResult unload(@NotNull final Audience audience, @NotNull final NamespacedKey key, final boolean save);

}