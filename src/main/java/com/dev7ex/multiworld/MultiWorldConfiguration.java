package com.dev7ex.multiworld;

import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.common.bukkit.plugin.configuration.PluginConfiguration;
import com.dev7ex.common.java.map.ParsedMap;

/**
 * @author Dev7ex
 * @since 19.05.2021
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
        super.values.put("only-player-command", super.fileConfiguration.getString("only-player-command"));
        super.values.put("usage", super.fileConfiguration.getString("usage"));
        super.values.put("defaults.world", super.fileConfiguration.getString("defaults.world"));
        super.values.put("defaults.difficulty", super.fileConfiguration.getString("defaults.difficulty"));
        super.values.put("defaults.gameMode", super.fileConfiguration.getString("defaults.gameMode"));
        super.values.put("defaults.pvp-enabled", super.fileConfiguration.getBoolean("defaults.pvp-enabled"));

        super.values.put("messages.back.world-already-there", super.fileConfiguration.getString("messages.back.world-already-there"));
        super.values.put("messages.back.world-not-loaded", super.fileConfiguration.getString("messages.back.world-not-loaded"));
        super.values.put("messages.back.world-not-found", super.fileConfiguration.getString("messages.back.world-not-found"));

        super.values.put("messages.world.general.already-exists", super.fileConfiguration.getString("messages.world.general.already-exists"));
        super.values.put("messages.world.general.type-not-available", super.fileConfiguration.getString("messages.world.general.type-not-available"));
        super.values.put("messages.world.general.waiting", super.fileConfiguration.getString("messages.world.general.waiting"));
        super.values.put("messages.world.general.cannot-deleted", super.fileConfiguration.getString("messages.world.general.cannot-deleted"));
        super.values.put("messages.world.general.cannot-unloaded", super.fileConfiguration.getString("messages.world.general.cannot-unloaded"));
        super.values.put("messages.world.general.not-exists", super.fileConfiguration.getString("messages.world.general.not-exists"));
        super.values.put("messages.world.general.error-message", super.fileConfiguration.getString("messages.world.general.error-message"));

        super.values.put("messages.world.create.starting", super.fileConfiguration.getString("messages.world.create.starting"));
        super.values.put("messages.world.create.finished", super.fileConfiguration.getString("messages.world.create.finished"));
        super.values.put("messages.world.delete.starting", super.fileConfiguration.getString("messages.world.delete.starting"));
        super.values.put("messages.world.delete.finished", super.fileConfiguration.getString("messages.world.delete.finished"));
        super.values.put("messages.world.list.message", super.fileConfiguration.getString("messages.world.list.message"));
        super.values.put("messages.world.unloading.starting", super.fileConfiguration.getString("messages.world.unloading.starting"));
        super.values.put("messages.world.unloading.finished", super.fileConfiguration.getString("messages.world.unloading.finished"));
        super.values.put("messages.world.unloading.chunk-starting", super.fileConfiguration.getString("messages.world.unloading.chunk-starting"));
        super.values.put("messages.world.unloading.chunk-finished", super.fileConfiguration.getString("messages.world.unloading.chunk-finished"));
        super.values.put("messages.world.unloading.chunk-teleport", super.fileConfiguration.getString("messages.world.unloading.chunk-teleport"));
        super.values.put("messages.world.loading.starting", super.fileConfiguration.getString("messages.world.loading.starting"));
        super.values.put("messages.world.loading.finished", super.fileConfiguration.getString("messages.world.loading.finished"));
        super.values.put("messages.world.loading.already-loaded", super.fileConfiguration.getString("messages.world.loading.already-loaded"));
        super.values.put("messages.world.loading.not-loaded", super.fileConfiguration.getString("messages.world.loading.not-loaded"));
        super.values.put("messages.world.options.updating", super.fileConfiguration.getString("messages.world.options.updating"));
        super.values.put("messages.world.options.value-wrong", super.fileConfiguration.getString("messages.world.options.value-wrong"));
        super.values.put("messages.world.options.not-available", super.fileConfiguration.getString("messages.world.options.not-available"));
        super.values.put("messages.world.teleport.message", super.fileConfiguration.getString("messages.world.teleport.message"));
        super.values.put("messages.world.teleport.component-message", super.fileConfiguration.getString("messages.world.teleport.component-message"));
        super.values.put("messages.world.teleport.component-hover-text", super.fileConfiguration.getString("messages.world.teleport.component-hover-text"));
        super.values.put("messages.world.teleport.target-already-in-world", super.fileConfiguration.getString("messages.world.teleport.target-already-in-world"));
        super.values.put("messages.world.teleport.sender-already-in-world", super.fileConfiguration.getString("messages.world.teleport.sender-already-in-world"));
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

    public final String getUsage() {
        return super.getMessageSafe("usage").replaceAll("%prefix%", this.getPrefix());
    }

    public final String getDefaultWorldName() {
        return super.values.getString("defaults.world");
    }

    public final String getWorldMessage(final String key) {
        return this.getMessage("world." + key);
    }

    public final String getMessage(final String key) {
        return super.getMessageSafe("messages." + key).replaceAll("%prefix%", this.getPrefix());
    }

    public final ParsedMap<String, Object> getValues() {
        return super.values;
    }

}
