package com.dev7ex.multiworld.api.world.group;

import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 16.10.2025
 */
public interface WorldGroupFactory {

    WorldGroup create(final int id, @NotNull final String groupName);

    void delete(final int id);

}
