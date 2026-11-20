package com.dev7ex.multiworld.world;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.world.WorldManager;
import com.dev7ex.multiworld.api.world.result.WorldLifecycleResult;
import net.kyori.adventure.audience.Audience;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class DefaultWorldManager implements WorldManager {

    private final MultiWorldApi multiWorldApi;

    public DefaultWorldManager(@NotNull final MultiWorldApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }

    @Override
    public @NotNull WorldLifecycleResult load(@NotNull final Audience audience, @NotNull final NamespacedKey key) {
        return null;
    }

    @Override
    public @NotNull WorldLifecycleResult unload(@NotNull final Audience audience, @NotNull final NamespacedKey key, final boolean save) {
        return null;
    }
}
