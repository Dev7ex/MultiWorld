package com.dev7ex.multiworld;

import com.dev7ex.common.collect.map.ParsedMap;
import com.dev7ex.common.io.file.configuration.ConfigurationHolder;
import com.dev7ex.common.io.file.configuration.ConfigurationProperties;
import com.dev7ex.common.io.file.configuration.YamlConfiguration;
import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApiConfiguration;
import com.dev7ex.multiworld.api.world.WorldDefaultProperty;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

/**
 * Configuration class for MultiWorld plugin.
 * This class manages the configuration settings for the MultiWorld plugin.
 *
 * @author Dev7ex
 * @since 25.01.2023
 */
@Getter(AccessLevel.PUBLIC)
@ConfigurationProperties(fileName = "config.yml", provider = YamlConfiguration.class)
public final class MultiWorldConfiguration extends MultiWorldBukkitApiConfiguration {

    private final ParsedMap<WorldDefaultProperty, Object> defaultProperties = new ParsedMap<>();

    /**
     * Constructs a MultiWorldConfiguration with the given ConfigurationHolder.
     *
     * @param configurationHolder The ConfigurationHolder instance.
     */
    public MultiWorldConfiguration(@NotNull final ConfigurationHolder configurationHolder) {
        super(configurationHolder);
    }

    /**
     * Loads the configuration.
     * This method loads the configuration settings and handles missing or unnecessary entries.
     */
    @Override
    public void load() {
        super.load();

        boolean hasChanges = false;

        for (final MultiWorldBukkitApiConfiguration.Entry entry : MultiWorldBukkitApiConfiguration.Entry.values()) {
            if ((entry.isRemoved()) && (super.getFileConfiguration().contains(entry.getPath()))) {
                super.getFileConfiguration().set(entry.getPath(), null);
                MultiWorldPlugin.getInstance().getLogger().info("Removed unnecessary config entry: " + entry.getPath());
                hasChanges = true;
            }

            if ((entry.isRemoved()) || (super.getFileConfiguration().contains(entry.getPath()))) {
                continue;
            }
            MultiWorldPlugin.getInstance().getLogger().info("Adding missing config entry: " + entry.getPath());
            super.getFileConfiguration().set(entry.getPath(), entry.getDefaultValue());
            hasChanges = true;
        }
        if (hasChanges) {
            super.saveFile();
        }
        super.getFileConfiguration().getSection("settings.defaults").getKeys()
                .forEach(entry -> this.defaultProperties.put(WorldDefaultProperty.valueOf(entry.replaceAll("-", "_").toUpperCase()),
                        super.getFileConfiguration().get("settings.defaults." + entry)));
    }

    @Override
    public String getMessage(@NotNull final String path) {
        return super.getString(path).replaceAll("%prefix%", this.getPrefix());
    }

    @Override
    public SimpleDateFormat getTimeFormat() {
        return new SimpleDateFormat(super.getFileConfiguration().getString(Entry.SETTINGS_TIME_FORMAT.getPath()));
    }

    @Override
    public boolean isAutoGameModeEnabled() {
        return super.getBoolean(Entry.SETTINGS_AUTO_GAME_MODE_ENABLED.getPath());
    }

    @Override
    public boolean isWorldLinkEnabled() {
        return super.getBoolean(Entry.SETTINGS_WORLD_LINK_ENABLED.getPath());
    }

    @Override
    public boolean canNetherWorldAccessViaCommand() {
        return super.getBoolean(Entry.SETTINGS_ACCESS_NETHER_WORLD_VIA_COMMAND.getPath());
    }

    @Override
    public boolean canEndWorldAccessViaCommand() {
        return super.getBoolean(Entry.SETTINGS_ACCESS_END_WORLD_VIA_COMMAND.getPath());
    }

}
