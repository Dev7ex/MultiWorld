package com.dev7ex.multiworld;

import com.dev7ex.common.bukkit.configuration.ConfigurationProperties;
import com.dev7ex.common.bukkit.plugin.configuration.DefaultPluginConfiguration;
import com.dev7ex.common.bukkit.plugin.configuration.LocalizedConfiguration;
import com.dev7ex.common.map.ParsedMap;
import com.dev7ex.multiworld.api.MultiWorldApiConfiguration;
import com.dev7ex.multiworld.api.world.WorldDefaultProperty;
import com.dev7ex.multiworld.api.world.WorldEnvironment;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang.LocaleUtils;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

/**
 * @author Dev7ex
 * @since 25.01.2023
 */
@Getter(AccessLevel.PUBLIC)
@ConfigurationProperties(fileName = "config.yml")
public final class MultiWorldConfiguration extends DefaultPluginConfiguration implements MultiWorldApiConfiguration {

    private final ParsedMap<WorldDefaultProperty, Object> defaultProperties = new ParsedMap<>();

    public MultiWorldConfiguration(@NotNull final Plugin plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        super.load();

        super.getFileConfiguration().getConfigurationSection("settings.defaults").getKeys(false)
                .stream()
                .forEach(entry -> this.defaultProperties.put(WorldDefaultProperty.valueOf(entry.replaceAll("-", "_").toUpperCase()), super.getFileConfiguration().get("settings.defaults." + entry)));

    }

    @Override
    public List<String> getAutoLoadableWorlds() {
        return super.getStringList("settings.auto-load");
    }

    @Override
    public boolean isAutoGameModeEnabled() {
        return super.getBoolean("settings.auto-game-mode-enabled");
    }

}