package com.dev7ex.multiworld.api.world;

import com.dev7ex.common.collect.map.ParsedMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldConfiguration<T extends WorldHolder> {

    void add(@NotNull final T worldHolder);

    void remove(@NotNull final T worldHolder);

    void remove(@NotNull final String name);

    boolean contains(@NotNull final String name);

    boolean hasProperty(@NotNull final String name, @NotNull final WorldProperty property);

    void addMissingProperty(@NotNull final T worldHolder, @NotNull final WorldProperty property);

    void updateFlag(@NotNull final T worldHolder, @NotNull final WorldFlag worldFlag, @NotNull final String value);

    void write(@NotNull final T worldHolder, @NotNull final ParsedMap<WorldProperty, Object> worldData);

    void write(@NotNull final T worldHolder, @NotNull final WorldProperty property, @NotNull final Object data);

    T getWorldHolder(@NotNull final String name);

    Map<String, T> getWorldHolders();

}
