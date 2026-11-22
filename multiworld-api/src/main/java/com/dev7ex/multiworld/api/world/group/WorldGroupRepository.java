package com.dev7ex.multiworld.api.world.group;

import com.dev7ex.multicommon.api.configuration.ConfigurationBase;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 09.07.2025
 */
public interface WorldGroupRepository extends ConfigurationBase {

    void insert(@NotNull final WorldGroup group);

    void delete(@NotNull final WorldGroup group);

    void delete(final int id);

    boolean exists(@NotNull final WorldGroup group);

    boolean exists(final int id);

    void clear();

    void update(@NotNull final WorldGroup group, @NotNull final WorldGroupProperty property, @NotNull final Object value);

    @NotNull
    Optional<WorldGroup> find(final int id);

    @NotNull
    Map<Integer, WorldGroup> findAll();

}