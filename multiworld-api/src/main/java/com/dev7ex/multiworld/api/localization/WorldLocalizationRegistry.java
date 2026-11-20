package com.dev7ex.multiworld.api.localization;

import com.dev7ex.multicommon.api.localization.LocalizationRegistry;
import com.dev7ex.multiworld.api.module.WorldModule;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface WorldLocalizationRegistry extends WorldModule, LocalizationRegistry {

    int getMaxAvailablePages(@NotNull final Audience audience, @NotNull final String path);

    int getMaxAvailablePages(@NotNull final Audience audience, @NotNull final WorldLocalizationEntry property);

    Component getComponent(@NotNull final Audience audience, @NotNull final String path);

    Component getComponent(@NotNull final Audience audience, @NotNull final String path, @NotNull final Consumer<TextReplacementConfig.Builder> builderConsumer);

    Component getComponent(@NotNull final Audience audience, @NotNull final WorldLocalizationEntry property);

    Component getComponent(@NotNull final Audience audience, @NotNull final WorldLocalizationEntry property, @NotNull final Consumer<TextReplacementConfig.Builder> builderConsumer);

    List<Component> getComponentList(@NotNull final Audience audience, @NotNull final String path);

    List<Component> getComponentList(@NotNull final Audience audience, @NotNull final String path, @NotNull final Map<String, String> replacements);

    List<Component> getComponentList(@NotNull final Audience audience, @NotNull final WorldLocalizationEntry property);

    List<Component> getComponentList(@NotNull final Audience audience, @NotNull final WorldLocalizationEntry property, @NotNull final Map<String, String> replacements);

    Component getNavigablePaginationComponent(@NotNull final Audience audience,
                                              final int currentPage,
                                              @NotNull final String command,
                                              @NotNull final String path);

    Component getNavigablePaginationComponent(@NotNull final Audience audience,
                                              final int currentPage,
                                              @NotNull final String command,
                                              @NotNull final WorldLocalizationEntry property);

    Component getHeader(@NotNull final Audience audience, @NotNull final Component replacement);

    Component getFooter(@NotNull final Audience audience, @NotNull final Component replacement);

    @NotNull
    DateTimeFormatter getDateTimeFormat(@NotNull final Audience audience);

}
