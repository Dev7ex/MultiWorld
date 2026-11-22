package com.dev7ex.multiworld.api.world.group;

import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 22.11.2026
 */
public interface WorldGroupManager {

    WorldGroup create(final int id, @NotNull final String groupName, @NotNull final String creatorName);

}
