
package com.dev7ex.multiworld.api.world.group;

import com.dev7ex.multiworld.api.module.WorldModule;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 09.07.2025
 */
public interface WorldGroupRegistry extends WorldModule {

    void register(@NotNull final WorldGroup group);

    void unregister(@NotNull final WorldGroup group);

    void unregister(final int id);

    @NotNull
    Optional<WorldGroup> find(final int id);

    @NotNull
    Optional<WorldGroup> find(@NotNull final String groupName);

    @NotNull
    WorldGroup getDefaultGroup();

    @NotNull
    List<WorldGroup> getGroups();

}
