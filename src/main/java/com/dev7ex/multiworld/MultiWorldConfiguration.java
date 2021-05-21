package com.dev7ex.multiworld;

import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.common.bukkit.plugin.configuration.PluginConfiguration;
import com.dev7ex.common.java.map.ParsedMap;

/**
 *
 * @author Dev7ex
 * @since 19.05.2021
 *
 */

public final class MultiWorldConfiguration extends PluginConfiguration {

    public MultiWorldConfiguration(final BukkitPlugin bukkitPlugin) {
        super(bukkitPlugin);
    }

    @Override
    public final void load() {
        super.values.put("prefix", super.fileConfiguration.getString("prefix"));
        super.values.put("no-permission", super.fileConfiguration.getString("no-permission"));
        super.values.put("player-not-found", super.fileConfiguration.getString("player-not-found"));
        super.values.put("default-world", super.fileConfiguration.getString("default-world"));
    }

    @Override
    public final String getPrefix() {
        return super.getMessageSafe("prefix");
    }

    @Override
    public final String getNoPermissionMessage() {
        return super.getMessageSafe("no-permission").replaceAll("%prefix%", this.getPrefix());
    }

    @Override
    public final String getPlayerNotFoundMessage() {
        return super.getMessageSafe("player-not-found").replaceAll("%prefix%", this.getPrefix());
    }

    public final String getDefaultWorldName() {
        return super.values.getString("default-world");
    }

    public final ParsedMap<String, Object> getValues() {
        return super.values;
    }

}
