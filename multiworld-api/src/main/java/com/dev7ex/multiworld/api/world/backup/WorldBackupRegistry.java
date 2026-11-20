package com.dev7ex.multiworld.api.world.backup;

import com.dev7ex.multiworld.api.module.WorldModule;
import com.dev7ex.multiworld.api.world.WorldContext;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Dev7ex
 * @since 13.04.2025
 */
public interface WorldBackupRegistry extends WorldModule {

    void register(@NotNull final WorldContext context, @NotNull final WorldBackup backup);

    void unregister(@NotNull final WorldContext context, @NotNull final WorldBackup backup);

    void unregisterAll(@NotNull final WorldContext context);

    boolean hasBackups(@NotNull final WorldContext context);

    int getBackupCount(@NotNull final WorldContext context);

    @NotNull
    Optional<WorldBackup> find(@NotNull final Audience audience, @NotNull final WorldContext context, @NotNull final String formattedName);

    @NotNull
    Optional<WorldBackup> findLatest(@NotNull final WorldContext context);

    @NotNull
    NavigableSet<WorldBackup> getBackups(@NotNull final WorldContext context);

    @NotNull
    Set<WorldContext> getWorldsWithBackups();

    @NotNull
    Map<WorldContext, NavigableSet<WorldBackup>> getWorldBackups();

    boolean lock(@NotNull final WorldContext context);

    void unlock(@NotNull final WorldContext context);

    boolean isLocked(@NotNull final WorldContext context);

}
