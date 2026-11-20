
package com.dev7ex.multiworld.api.world;

import com.dev7ex.multicommon.api.configuration.ConfigurationBase;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 *
 * @author Dev7ex
 * @since 13.04.2025
 */
public interface WorldRepository extends ConfigurationBase {

    void insert(@NotNull final WorldContext context);

    void delete(@NotNull final WorldContext context);

    void delete(@NotNull final NamespacedKey key);

    boolean exists(@NotNull final WorldContext context);

    boolean exists(@NotNull final NamespacedKey key);

    void deleteAll();

    void update(@NotNull final WorldContext context, @NotNull final WorldProperty property, @NotNull final Object value);

    Optional<WorldContext> find(@NotNull final NamespacedKey key);

    Map<NamespacedKey, WorldContext> findAll();

    void cleanup();

}
