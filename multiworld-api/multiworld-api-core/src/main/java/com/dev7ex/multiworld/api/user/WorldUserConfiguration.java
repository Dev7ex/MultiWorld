package com.dev7ex.multiworld.api.user;

import com.dev7ex.common.map.ParsedMap;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldUserConfiguration {

    ParsedMap<WorldUserProperty, Object> read();

    ParsedMap<WorldUserProperty, Object> read(@NotNull final WorldUserProperty... properties);

    void write(@NotNull final ParsedMap<WorldUserProperty, Object> userData);

    void saveFile();

}
