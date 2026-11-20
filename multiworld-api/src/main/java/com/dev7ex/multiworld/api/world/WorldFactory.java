package com.dev7ex.multiworld.api.world;

import com.dev7ex.multiworld.api.world.result.WorldDeletionResult;
import net.kyori.adventure.audience.Audience;
import org.bukkit.NamespacedKey;
import org.bukkit.WorldCreator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface WorldFactory {

    @Nullable
    WorldContext create(@NotNull final Audience audience, @NotNull final NamespacedKey worldKey);

    @Nullable
    WorldContext create(@NotNull final Audience audience, @NotNull final String worldName);

    @Nullable
    WorldContext create(@NotNull final Audience audience, @NotNull final WorldContext context);

    @Nullable
    WorldContext create(@NotNull final Audience audience, @NotNull final WorldCreator worldCreator);

    @NotNull
    WorldDeletionResult delete(@NotNull final Audience audience, @NotNull final WorldContext context);

}