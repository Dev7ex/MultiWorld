
package com.dev7ex.multiworld.api.world;

import com.dev7ex.multiworld.api.module.WorldModule;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 28.10.2026
 */
public interface WorldRegistry extends WorldModule {

    void register(@NotNull final WorldContext context);

    void unregister(@NotNull final WorldContext context);

    void unregister(@NotNull final NamespacedKey key);

    @NotNull
    Optional<WorldContext> find(@NotNull final String worldName);

    @NotNull
    Optional<WorldContext> find(@NotNull final NamespacedKey key);

    @NotNull
    Optional<WorldContext> find(@NotNull final World world);

    @NotNull
    Optional<WorldContext> findDefaultWorld();

    @NotNull
    List<WorldContext> getWorlds();

    @NotNull
    List<WorldContext> getLoadedWorlds();

    @NotNull
    List<WorldContext> getUnloadedWorlds();

    @NotNull
    List<WorldContext> getWorldsWithCreator(@NotNull final String creatorName);

}
