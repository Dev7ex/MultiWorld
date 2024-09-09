package com.dev7ex.multiworld.translation;

import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.common.bukkit.plugin.module.PluginModule;
import com.dev7ex.common.io.file.Files;
import com.dev7ex.common.io.file.configuration.ConfigurationProvider;
import com.dev7ex.common.io.file.configuration.FileConfiguration;
import com.dev7ex.common.io.file.configuration.JsonConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.translation.BukkitTranslationProvider;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Dev7ex
 * @since 10.07.2024
 */
@Getter(AccessLevel.PUBLIC)
public class DefaultTranslationProvider implements PluginModule, BukkitTranslationProvider {

    private final BukkitPlugin plugin;
    private final Map<Locale, FileConfiguration> translationConfigurations = new HashMap<>();
    private final Locale defaultLocale = new Locale("en", "us");

    public DefaultTranslationProvider(@NotNull final BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        this.plugin.saveResource("language/de_DE.json", true);
        this.plugin.saveResource("language/en_US.json", true);
        this.plugin.saveResource("language/fr_FR.json", true);
        this.plugin.saveResource("language/zh_CN.json", true);
        this.plugin.saveResource("language/tr_TR.json", true);

        for (final File file : Files.getFiles(this.plugin.getSubFolder("language"))) {
            if ((file.isFile()) && (file.getName().endsWith(".json"))) {
                final String[] parts = file.getName().replaceAll(".json", "").split("_");
                final Locale locale = new Locale(parts[0], parts[1]);

                try {
                    final FileConfiguration nextConfiguration = ConfigurationProvider.getProvider(JsonConfiguration.class).load(file);
                    this.translationConfigurations.put(locale, nextConfiguration);

                } catch (final IOException exception) {
                    this.plugin.getLogger().warning("Can't load configuration File for Locale " + locale);
                    exception.printStackTrace();
                }
            }
        }
        MultiWorldPlugin.getInstance().getLogger().info("Found: [" + this.translationConfigurations.size() + "] Translations");
    }

    @Override
    public void onDisable() {
        this.translationConfigurations.clear();
    }

    @Override
    public void register(@NotNull final Locale locale, @NotNull final File file) {
        try {
            final FileConfiguration configuration = ConfigurationProvider.getProvider(JsonConfiguration.class).load(file);
            this.translationConfigurations.put(locale, configuration);

        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void unregister(@NotNull final Locale locale) {
        this.translationConfigurations.remove(locale);
    }

    @Override
    public String getMessage(@NotNull final CommandSender entity, @NotNull final String path) {
        if (entity instanceof Player) {
            final Player player = (Player) entity;
            final String[] parts = player.getLocale().split("_");
            final Locale locale = new Locale(parts[0], parts[1]);

            return this.translationConfigurations.getOrDefault(locale, this.translationConfigurations.get(this.defaultLocale)).getString(path);
        }
        return this.translationConfigurations.get(this.defaultLocale).getString(path);
    }

    @Override
    public List<String> getMessageList(@NotNull final CommandSender entity, @NotNull final String path) {
        if (entity instanceof Player) {
            final Player player = (Player) entity;
            final String[] parts = player.getLocale().split("_");
            final Locale locale = new Locale(parts[0], parts[1]);

            return this.translationConfigurations.getOrDefault(locale, this.translationConfigurations.get(this.defaultLocale)).getStringList(path);
        }
        return this.translationConfigurations.get(this.defaultLocale).getStringList(path);
    }

}
