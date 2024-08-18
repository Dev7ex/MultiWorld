package com.dev7ex.multiworld.api.bukkit;

import com.dev7ex.common.bukkit.plugin.configuration.DefaultPluginConfiguration;
import com.dev7ex.common.io.file.configuration.ConfigurationHolder;
import com.dev7ex.multiworld.api.MultiWorldApiConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 16.08.2023
 */
public abstract class MultiWorldBukkitApiConfiguration extends DefaultPluginConfiguration implements MultiWorldApiConfiguration {

    public MultiWorldBukkitApiConfiguration(@NotNull final ConfigurationHolder configurationHolder) {
        super(configurationHolder);
    }

    @Getter(AccessLevel.PUBLIC)
    public enum Entry {

        PREFIX("prefix", "§8[§bMultiWorld§8]§r", false),

        SETTINGS_ACCESS_NETHER_WORLD_VIA_COMMAND("settings.access-nether-world-via-command", true, false),
        SETTINGS_ACCESS_END_WORLD_VIA_COMMAND("settings.access-end-world-via-command", true, false),
        SETTINGS_AUTO_GAME_MODE_ENABLED("settings.auto-game-mode-enabled", true, false),

        SETTINGS_DEFAULTS_AUTO_LOAD_ENABLED("settings.defaults.auto-load-enabled", false, false),
        SETTINGS_DEFAULTS_AUTO_UNLOAD_ENABLED("settings.defaults.auto-unload-enabled", false, false),
        SETTINGS_DEFAULTS_DIFFICULTY("settings.defaults.difficulty", "PEACEFUL", false),
        SETTINGS_DEFAULTS_END_PORTAL_ACCESSIBLE("settings.defaults.end-portal-accessible", true, false),
        SETTINGS_DEFAULTS_END_WORLD("settings.defaults.end-world", "world_the_end", false),
        SETTINGS_DEFAULTS_GAME_MODE("settings.defaults.game-mode", "SURVIVAL", false),
        SETTINGS_DEFAULTS_HUNGER_ENABLED_ENABLED("settings.defaults.hunger-enabled", true, false),
        SETTINGS_DEFAULTS_KEEP_SPAWN_IN_MEMORY("settings.defaults.keep-spawn-in-memory", false, false),
        SETTINGS_DEFAULTS_NETHER_PORTAL_ACCESSIBLE("settings.defaults.nether-portal-accessible", true, false),
        SETTINGS_DEFAULTS_NETHER_WORLD("settings.defaults.nether-world", "world_nether", false),
        SETTINGS_DEFAULTS_NORMAL_WORLD("settings.defaults.normal-world", "world", false),
        SETTINGS_DEFAULTS_PVP_ENABLED("settings.defaults.pvp-enabled", true, false),
        SETTINGS_DEFAULTS_RECEIVE_ACHIEVEMENTS("settings.defaults.receive-achievements", true, false),
        SETTINGS_DEFAULTS_REDSTONE_ENABLED("settings.defaults.redstone-enabled", true, false),
        SETTINGS_DEFAULTS_SPAWN_ANIMALS("settings.defaults.spawn-animals", true, false),
        SETTINGS_DEFAULTS_SPAWN_ENTITIES("settings.defaults.spawn-entities", true, false),
        SETTINGS_DEFAULTS_SPAWN_MONSTERS("settings.defaults.spawn-monsters", true, false),
        SETTINGS_DEFAULTS_WEATHER_ENABLED("settings.defaults.weather-enabled", true, false),
        SETTINGS_DEFAULTS_WHITELIST_ENABLED("settings.defaults.whitelist-enabled", false, false),

        SETTINGS_TIME_FORMAT("settings.time-format", "dd.MM.yyyy HH:mm:ss", false),
        SETTINGS_WORLD_LINK_ENABLED("settings.world-link-enabled", true, false);

        private final String path;
        private final Object defaultValue;
        private final boolean removed;

        Entry(@NotNull final String path, @NotNull final Object defaultValue, final boolean removed) {
            this.path = path;
            this.defaultValue = defaultValue;
            this.removed = removed;
        }
    }

}
