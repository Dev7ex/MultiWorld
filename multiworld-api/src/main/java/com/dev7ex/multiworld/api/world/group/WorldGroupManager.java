package com.dev7ex.multiworld.api.world.group;

import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Dev7ex
 * @since 22.11.2026
 */
public interface WorldGroupManager {

    Optional<WorldGroup> create(@NotNull final Audience audience, final int groupId, @NotNull final String groupName, @NotNull final String creatorName);

    void delete(final int id);

}
