package com.dev7ex.multiworld.api;

import com.dev7ex.common.collect.map.ParsedMap;
import com.dev7ex.multiworld.api.world.WorldDefaultProperty;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface MultiWorldApiConfiguration {

    String getString(@NotNull final String path);

    boolean getBoolean(@NotNull final String path);

    int getInteger(@NotNull final String path);

    String getMessage(@NotNull final String path);

    String getPrefix();

    SimpleDateFormat getTimeFormat();

    boolean isReceiveUpdateMessage();

    boolean isAutoGameModeEnabled();

    boolean isWorldLinkEnabled();

    boolean canNetherWorldAccessViaCommand();

    boolean canEndWorldAccessViaCommand();

    ParsedMap<WorldDefaultProperty, Object> getDefaultProperties();

    List<String> getStringList(@NotNull final String path);

}
