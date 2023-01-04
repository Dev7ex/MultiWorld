package com.dev7ex.multiworld.world;

import com.dev7ex.common.bukkit.configuration.ConfigurationBase;
import com.dev7ex.common.bukkit.configuration.ConfigurationProperties;
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
@ConfigurationProperties(fileName = "worlds.yml")
public final class WorldConfiguration extends ConfigurationBase {

    public WorldConfiguration(final Plugin plugin) {
        super(plugin);
    }

    public void registerWorld(final String worldName, final WorldProperties worldProperties) {
        super.getFileConfiguration().set(worldName + ".creator", worldProperties.getWorldCreator());
        super.getFileConfiguration().set(worldName + ".creation-time", worldProperties.getCreationTime());
        super.getFileConfiguration().set(worldName + ".last-world-interaction", worldProperties.getLastWorldInteraction());
        super.getFileConfiguration().set(worldName + ".world-type", worldProperties.getWorldType().toString());
        super.getFileConfiguration().set(worldName + ".difficulty", worldProperties.getDifficulty().toString());
        super.getFileConfiguration().set(worldName + ".gamemode", worldProperties.getGameMode().toString());
        super.getFileConfiguration().set(worldName + ".pvp-enabled", worldProperties.isPvpEnabled());
        super.saveFile();
    }

    public void unregisterWorld(final String worldName) {
        super.getFileConfiguration().set(worldName, null);
        super.saveFile();
    }

    public boolean isWorldRegistered(final String worldName) {
        return super.getFileConfiguration().contains(worldName);
    }

    public WorldProperties getWorldProperties(final String worldName) {
        final String creator = super.getFileConfiguration().getString(worldName + ".creator");
        final long creationTime = super.getFileConfiguration().getLong(worldName + ".creation-time");
        final long lastWorldInteraction = super.getFileConfiguration().getLong(worldName + ".last-world-interaction");
        final WorldType worldType = WorldType.valueOf(super.getFileConfiguration().getString(worldName + ".world-type"));
        final Difficulty difficulty = Difficulty.valueOf(super.getFileConfiguration().getString(worldName + ".difficulty"));
        final GameMode gameMode = GameMode.valueOf(super.getFileConfiguration().getString(worldName + ".gamemode"));
        final boolean pvpEnabled = super.getFileConfiguration().getBoolean(worldName + ".pvp-enabled");
        final boolean spawnAnimals = super.getFileConfiguration().getBoolean(worldName + "spawn-animals", false);
        final boolean spawnMonsters = super.getFileConfiguration().getBoolean(worldName + "spawn-monsters", false);
        return new WorldProperties(worldName, creator, creationTime,
                lastWorldInteraction, worldType, difficulty,
                gameMode, pvpEnabled, spawnAnimals, spawnMonsters);
    }

    public void updateWorldOption(final String worldName, final WorldOption worldOption, final String value) {
        super.getFileConfiguration().set(worldName + "." + worldOption.getStoragePath(), value);
        super.saveFile();
    }

    public void updateLastWorldInteraction(final WorldProperties worldProperties) {
        super.getFileConfiguration().set(worldProperties.getWorldName() + ".last-world-interaction", worldProperties.getLastWorldInteraction());
        super.saveFile();
    }

    public Map<String, WorldProperties> getWorldProperties() {
        final Map<String, WorldProperties> worlds = Maps.newHashMap();

        for (final String world : super.getFileConfiguration().getKeys(false)) {
            final ConfigurationSection section = super.getFileConfiguration().getConfigurationSection(world);
            final String creator = section.getString("creator");
            final long creationTime = section.getLong("creation-time");
            final long lastWorldInteraction = section.getLong("last-world-interaction");
            final WorldType worldType = WorldType.valueOf(section.getString("world-type"));
            final Difficulty difficulty = Difficulty.valueOf(section.getString("difficulty"));
            final GameMode gameMode = GameMode.valueOf(section.getString("gamemode"));
            final boolean pvpEnabled = section.getBoolean("pvp-enabled");
            final boolean spawnAnimals = section.getBoolean("spawn-animals", false);
            final boolean spawnMonsters = section.getBoolean("spawn-monsters", false);
            worlds.put(world, new WorldProperties(world, creator, creationTime, lastWorldInteraction, worldType, difficulty, gameMode, pvpEnabled, spawnAnimals, spawnMonsters));
        }
        return worlds;
    }

    public Set<String> getWorlds() {
        return super.getFileConfiguration().getKeys(false);
    }

}
