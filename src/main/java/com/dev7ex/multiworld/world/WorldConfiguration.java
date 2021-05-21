package com.dev7ex.multiworld.world;

import com.dev7ex.common.bukkit.configuration.SimpleConfiguration;

import com.google.common.collect.Maps;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author Dev7ex
 * @since 20.05.2021
 *
 */

public final class WorldConfiguration extends SimpleConfiguration {

    public WorldConfiguration(final Plugin plugin, final String fileName) {
        super(plugin, fileName);
    }

    public final void registerWorld(final String worldName, final WorldProperties worldProperties) {
        if(Bukkit.getWorld(worldName) == null) {
            return;
        }
        super.yamlConfiguration.set(worldName + ".creator", worldProperties.getWorldCreator());
        super.yamlConfiguration.set(worldName + ".creation-time", worldProperties.getCreationTime());
        super.yamlConfiguration.set(worldName + ".last-world-interaction", worldProperties.getLastWorldInteraction());
        super.saveFile();
    }

    public final void unregisterWorld(final String worldName) {
        super.yamlConfiguration.set(worldName, null);
        super.saveFile();
    }

    public final boolean isWorldRegistered(final String worldName) {
        return super.yamlConfiguration.contains(worldName);
    }

    public final WorldProperties getWorldProperties(final String worldName) {
        final String creator = super.yamlConfiguration.getString(worldName + ".creator");
        final long creationTime = super.yamlConfiguration.getLong(worldName + ".creation-time");
        final long lastWorldInteraction = super.yamlConfiguration.getLong(worldName + ".last-world-interaction");
        return new WorldProperties(worldName, creator, creationTime, lastWorldInteraction);
    }

    public final void updateLastWorldInteraction(final WorldProperties worldProperties) {
        super.yamlConfiguration.set(worldProperties.getWorldName() + ".last-world-interaction", worldProperties.getLastWorldInteraction());
        super.saveFile();
    }

    public final void updateWorldProperties(final WorldProperties worldProperties) {
        super.yamlConfiguration.set(worldProperties.getWorldName() + ".creator", worldProperties.getWorldCreator());
        super.yamlConfiguration.set(worldProperties.getWorldName() + ".creation-time", worldProperties.getCreationTime());
        super.yamlConfiguration.set(worldProperties.getWorldName() + ".last-world-interaction", worldProperties.getLastWorldInteraction());
        super.saveFile();
    }

    public final Map<String, WorldProperties> getWorldProperties() {
        final Map<String, WorldProperties> worlds = Maps.newHashMap();

        for(final String world : super.yamlConfiguration.getKeys(false)) {
            final ConfigurationSection section = super.yamlConfiguration.getConfigurationSection(world);
            final String creator = section.getString("creator");
            final long creationTime = section.getLong("creation-time");
            final long lastWorldInteraction = section.getLong("last-world-interaction");
            worlds.put(world, new WorldProperties(world, creator, creationTime, lastWorldInteraction));
        }
        return worlds;
    }

    public final Set<String> getWorlds() {
        return super.yamlConfiguration.getKeys(false);
    }

}
