package com.dev7ex.multiworld.world;

import com.dev7ex.common.bukkit.configuration.ConfigurationBase;
import com.dev7ex.common.bukkit.configuration.ConfigurationProperties;
import com.dev7ex.common.java.io.FileExtension;
import com.google.common.collect.Maps;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Set;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@ConfigurationProperties(fileName = "worlds", fileExtension = FileExtension.YAML)
public final class WorldConfiguration extends ConfigurationBase {

    public WorldConfiguration(final Plugin plugin) {
        super(plugin);
    }

    public void registerWorld(final String worldName, final WorldProperties worldProperties) {
        super.yamlConfiguration.set(worldName + ".creator", worldProperties.getWorldCreator());
        super.yamlConfiguration.set(worldName + ".creation-time", worldProperties.getCreationTime());
        super.yamlConfiguration.set(worldName + ".last-world-interaction", worldProperties.getLastWorldInteraction());
        super.yamlConfiguration.set(worldName + ".world-type", worldProperties.getWorldType().toString());
        super.yamlConfiguration.set(worldName + ".difficulty", worldProperties.getDifficulty().toString());
        super.yamlConfiguration.set(worldName + ".gameMode", worldProperties.getGameMode().toString());
        super.yamlConfiguration.set(worldName + ".pvp-enabled", worldProperties.isPvpEnabled());
        super.saveFile();
    }

    public void unregisterWorld(final String worldName) {
        super.yamlConfiguration.set(worldName, null);
        super.saveFile();
    }

    public boolean isWorldRegistered(final String worldName) {
        return super.yamlConfiguration.contains(worldName);
    }

    public WorldProperties getWorldProperties(final String worldName) {
        final String creator = super.yamlConfiguration.getString(worldName + ".creator");
        final long creationTime = super.yamlConfiguration.getLong(worldName + ".creation-time");
        final long lastWorldInteraction = super.yamlConfiguration.getLong(worldName + ".last-world-interaction");
        final WorldType worldType = WorldType.valueOf(super.yamlConfiguration.getString(worldName + ".world-type"));
        final Difficulty difficulty = Difficulty.valueOf(super.yamlConfiguration.getString(worldName + ".difficulty"));
        final GameMode gameMode = GameMode.valueOf(super.yamlConfiguration.getString(worldName + ".gameMode"));
        final boolean pvpEnabled = super.yamlConfiguration.getBoolean(worldName + ".pvp-enabled");
        final boolean spawnAnimals = super.yamlConfiguration.getBoolean(worldName + "spawn-animals", false);
        final boolean spawnMonsters = super.yamlConfiguration.getBoolean(worldName + "spawn-monsters", false);
        return new WorldProperties(worldName, creator, creationTime,
                lastWorldInteraction, worldType, difficulty,
                gameMode, pvpEnabled, spawnAnimals, spawnMonsters);
    }

    public void updateWorldOption(final String worldName, final WorldOption worldOption, final String value) {
        super.yamlConfiguration.set(worldName + "." + worldOption.getConfigEntry(), value);
        super.saveFile();
    }

    public void updateLastWorldInteraction(final WorldProperties worldProperties) {
        super.yamlConfiguration.set(worldProperties.getWorldName() + ".last-world-interaction", worldProperties.getLastWorldInteraction());
        super.saveFile();
    }

    public Map<String, WorldProperties> getWorldProperties() {
        final Map<String, WorldProperties> worlds = Maps.newHashMap();

        for (final String world : super.yamlConfiguration.getKeys(false)) {
            final ConfigurationSection section = super.yamlConfiguration.getConfigurationSection(world);
            final String creator = section.getString("creator");
            final long creationTime = section.getLong("creation-time");
            final long lastWorldInteraction = section.getLong("last-world-interaction");
            final WorldType worldType = WorldType.valueOf(section.getString("world-type"));
            final Difficulty difficulty = Difficulty.valueOf(section.getString("difficulty"));
            final GameMode gameMode = GameMode.valueOf(section.getString("gameMode"));
            final boolean pvpEnabled = section.getBoolean("pvp-enabled");
            final boolean spawnAnimals = section.getBoolean("spawn-animals", false);
            final boolean spawnMonsters = section.getBoolean("spawn-monsters", false);
            worlds.put(world, new WorldProperties(world, creator, creationTime, lastWorldInteraction, worldType, difficulty, gameMode, pvpEnabled, spawnAnimals, spawnMonsters));
        }
        return worlds;
    }

    public Set<String> getWorlds() {
        return super.yamlConfiguration.getKeys(false);
    }

}
