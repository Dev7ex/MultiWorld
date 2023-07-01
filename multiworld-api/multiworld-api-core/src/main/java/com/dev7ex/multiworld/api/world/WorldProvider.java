package com.dev7ex.multiworld.api.world;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldProvider<T extends WorldHolder> {

    void register(@NotNull final T worldHolder);

    void unregister(@NotNull final String name);

    boolean isRegistered(@NotNull final String name);

    Optional<T> getWorldHolder(@NotNull final String name);

    Map<String, T> getWorldHolders();

    WorldConfiguration<T> getConfiguration();


}
