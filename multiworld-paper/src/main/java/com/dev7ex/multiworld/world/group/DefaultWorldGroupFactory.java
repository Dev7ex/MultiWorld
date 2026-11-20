package com.dev7ex.multiworld.world.group;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.world.group.WorldGroup;
import com.dev7ex.multiworld.api.world.group.WorldGroupFactory;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 16.10.2025
 */
@Getter(AccessLevel.PUBLIC)
public class DefaultWorldGroupFactory implements WorldGroupFactory {


    public DefaultWorldGroupFactory(@NotNull final MultiWorldApi multiWorldApi) {

    }

    @Override
    public WorldGroup create(final int id, @NotNull final String groupName) {
        final WorldGroup group = DefaultWorldGroup.builder()
                .build();

        return group;
    }

    @Override
    public void delete(int id) {

    }

}
