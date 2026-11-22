package com.dev7ex.multiworld.world.group;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.world.group.WorldGroup;
import com.dev7ex.multiworld.api.world.group.WorldGroupManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 22.11.2026
 */
public class DefaultWorldGroupManager implements WorldGroupManager {

    private final MultiWorldApi multiWorldApi;

    public DefaultWorldGroupManager(@NotNull final MultiWorldApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }

    @Override
    public WorldGroup create(final int id, @NotNull final String groupName, @NotNull final String creatorName) {
        return multiWorldApi.getGroupFactory().create(id, groupName, creatorName);
    }

}
