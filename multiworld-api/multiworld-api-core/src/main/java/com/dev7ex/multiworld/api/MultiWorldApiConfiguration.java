package com.dev7ex.multiworld.api;

import com.dev7ex.common.map.ParsedMap;
import com.dev7ex.multiworld.api.world.WorldDefaultProperty;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface MultiWorldApiConfiguration {

    String getPrefix();

    ParsedMap<WorldDefaultProperty, Object> getDefaultProperties();

    List<String> getAutoLoadableWorlds();

    boolean isAutoGameModeEnabled();

    String getString(@NotNull final String path);

    boolean getBoolean(@NotNull final String path);

}
