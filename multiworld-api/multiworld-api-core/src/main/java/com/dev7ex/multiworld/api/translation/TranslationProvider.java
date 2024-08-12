package com.dev7ex.multiworld.api.translation;

import com.dev7ex.common.io.file.configuration.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Dev7ex
 * @since 10.07.2024
 */
public interface TranslationProvider<E> {

    Map<Locale, FileConfiguration> getTranslationConfigurations();

    void register(@NotNull final Locale locale, @NotNull final File file);

    void unregister(@NotNull final Locale locale);

    Locale getDefaultLocale();

    String getMessage(@NotNull final E entity, @NotNull final String path);

    List<String> getMessageList(@NotNull final E entity, @NotNull final String path);

}
