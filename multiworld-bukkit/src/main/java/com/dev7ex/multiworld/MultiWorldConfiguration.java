package com.dev7ex.multiworld;

import com.dev7ex.common.bukkit.configuration.ConfigurationProperties;
import com.dev7ex.common.bukkit.plugin.configuration.DefaultPluginConfiguration;
import com.dev7ex.common.map.ParsedMap;
import com.dev7ex.multiworld.api.MultiWorldApiConfiguration;
import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApiConfiguration;
import com.dev7ex.multiworld.api.world.WorldDefaultProperty;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 25.01.2023
 */
@Getter(AccessLevel.PUBLIC)
@ConfigurationProperties(fileName = "config.yml")
public final class MultiWorldConfiguration extends MultiWorldBukkitApiConfiguration implements MultiWorldApiConfiguration {

    private final ParsedMap<WorldDefaultProperty, Object> defaultProperties = new ParsedMap<>();

    public MultiWorldConfiguration(@NotNull final Plugin plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        super.load();

        for (final MultiWorldBukkitApiConfiguration.Entry entry : MultiWorldBukkitApiConfiguration.Entry.values()) {
            if ((entry.isRemoved()) && (super.getFileConfiguration().contains(entry.getPath()))) {
                super.getFileConfiguration().set(entry.getPath(), null);
                super.getPlugin().getLogger().info("Remove unnecessary config entry: " + entry.getPath());
            }

            if ((entry.isRemoved()) || (super.getFileConfiguration().contains(entry.getPath()))) {
                continue;
            }
            super.getPlugin().getLogger().info("Adding missing config entry: " + entry.getPath());
            super.getFileConfiguration().set(entry.getPath(), entry.getDefaultValue());
        }

        super.getFileConfiguration().getConfigurationSection("settings.defaults").getKeys(false)
                .stream()
                .forEach(entry -> this.defaultProperties.put(WorldDefaultProperty.valueOf(entry.replaceAll("-", "_").toUpperCase()), super.getFileConfiguration().get("settings.defaults." + entry)));
        super.saveFile();
    }

    @Override
    public List<String> getAutoLoadableWorlds() {
        return super.getStringList("settings.auto-load");
    }

    @Override
    public boolean isAutoGameModeEnabled() {
        return super.getBoolean("settings.auto-game-mode-enabled");
    }

    @Deprecated
    @Override
    public boolean isWorldLinkEnabled() {
        return super.getBoolean("settings.world-link-enabled");
    }

}
