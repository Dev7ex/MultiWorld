/**
 * Manages the default configuration for Bukkit worlds.
 * This class handles adding, removing, and checking properties for Bukkit worlds.
 * It also provides methods to retrieve and manipulate world properties.
 */
package com.dev7ex.multiworld.world;

import com.dev7ex.common.collect.map.ParsedMap;
import com.dev7ex.common.io.file.configuration.Configuration;
import com.dev7ex.common.io.file.configuration.ConfigurationProperties;
import com.dev7ex.common.io.file.configuration.JsonConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldConfiguration;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.world.*;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the default configuration for Bukkit worlds.
 * This class handles adding, removing, and checking properties for Bukkit worlds.
 * It also provides methods to retrieve and manipulate world properties.
 */
@ConfigurationProperties(fileName = "world.json", provider = JsonConfiguration.class)
public class DefaultWorldConfiguration extends Configuration implements BukkitWorldConfiguration {

    /**
     * The default properties for worlds.
     */
    private final ParsedMap<WorldDefaultProperty, Object> defaultProperties;

    /**
     * Constructs a new DefaultWorldConfiguration.
     *
     * @param plugin The MultiWorldPlugin instance.
     */
    public DefaultWorldConfiguration(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.defaultProperties = plugin.getConfiguration().getDefaultProperties();
    }

    /**
     * Adds a BukkitWorldHolder to the configuration.
     *
     * @param worldHolder The BukkitWorldHolder to add.
     */
    @Override
    public void add(@NotNull final BukkitWorldHolder worldHolder) {
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.CREATOR_NAME.getStoragePath(), worldHolder.getCreatorName());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.CREATION_TIMESTAMP.getStoragePath(), worldHolder.getCreationTimeStamp());

        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.AUTO_LOAD_ENABLED.getStoragePath(), worldHolder.isAutoLoadEnabled());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.AUTO_UNLOAD_ENABLED.getStoragePath(), worldHolder.isAutoUnloadEnabled());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.DIFFICULTY.getStoragePath(), worldHolder.getDifficulty().toString());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.END_PORTAL_ACCESSIBLE.getStoragePath(), worldHolder.isEndPortalAccessible());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.GAME_MODE.getStoragePath(), worldHolder.getGameMode().toString());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.HUNGER_ENABLED.getStoragePath(), worldHolder.isHungerEnabled());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.KEEP_SPAWN_IN_MEMORY.getStoragePath(), worldHolder.isKeepSpawnInMemory());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.NETHER_PORTAL_ACCESSIBLE.getStoragePath(), worldHolder.isNetherPortalAccessible());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.PVP_ENABLED.getStoragePath(), worldHolder.isPvpEnabled());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.RECEIVE_ACHIEVEMENTS.getStoragePath(), worldHolder.isReceiveAchievements());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.REDSTONE_ENABLED.getStoragePath(), worldHolder.isRedstoneEnabled());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.SPAWN_ANIMALS.getStoragePath(), worldHolder.isSpawnAnimals());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.SPAWN_MONSTERS.getStoragePath(), worldHolder.isSpawnMonsters());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.SPAWN_ENTITIES.getStoragePath(), worldHolder.isSpawnEntities());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.WEATHER_ENABLED.getStoragePath(), worldHolder.isWeatherEnabled());

        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.ENVIRONMENT.getStoragePath(), worldHolder.getEnvironment().toString());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.GENERATOR.getStoragePath(), worldHolder.getGenerator());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.TYPE.getStoragePath(), worldHolder.getType().toString());

        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.LINKED_END_WORLD.getStoragePath(), worldHolder.getEndWorldName());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.LINKED_NETHER_WORLD.getStoragePath(), worldHolder.getNetherWorldName());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.LINKED_OVERWORLD.getStoragePath(), worldHolder.getNormalWorldName());

        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.WHITELIST.getStoragePath(), Collections.emptyList());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.WHITELIST_ENABLED.getStoragePath(), worldHolder.isWhitelistEnabled());
        super.saveFile();
    }

    /**
     * Removes a BukkitWorldHolder from the configuration.
     *
     * @param worldHolder The BukkitWorldHolder to remove.
     */
    @Override
    public void remove(@NotNull final BukkitWorldHolder worldHolder) {
        this.remove(worldHolder.getName());
    }

    /**
     * Removes a BukkitWorldHolder from the configuration by name.
     *
     * @param name The name of the BukkitWorldHolder to remove.
     */
    @Override
    public void remove(@NotNull final String name) {
        super.getFileConfiguration().set(name, null);
        super.saveFile();
    }

    /**
     * Checks if the configuration contains a world with the given name.
     *
     * @param name The name of the world to check for.
     * @return True if the world exists in the configuration, false otherwise.
     */
    @Override
    public boolean contains(@NotNull final String name) {
        return super.getFileConfiguration().contains(name);
    }

    /**
     * Retrieves the value of a specific property for a world.
     *
     * @param worldHolder The WorldHolder containing the property.
     * @param property    The property to retrieve.
     * @return The value of the specified property.
     */
    @Override
    public Object getValue(@NotNull final WorldHolder worldHolder, @NotNull final WorldProperty property) {
        return super.getFileConfiguration().get(worldHolder.getName() + "." + property.getStoragePath());
    }

    /**
     * Checks if a world has a specific property.
     *
     * @param name     The name of the world.
     * @param property The property to check for.
     * @return True if the world has the specified property, false otherwise.
     */
    @Override
    public boolean hasProperty(@NotNull final String name, @NotNull final WorldProperty property) {
        return super.getFileConfiguration().contains(name + "." + property.getStoragePath());
    }

    @Override
    public void addMissingProperty(@NotNull final BukkitWorldHolder worldHolder, @NotNull final WorldProperty property) {
        switch (property) {
            case AUTO_LOAD_ENABLED:
                this.write(worldHolder, property, this.defaultProperties.getString(WorldDefaultProperty.AUTO_LOAD_ENABLED));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case AUTO_UNLOAD_ENABLED:
                this.write(worldHolder, property, this.defaultProperties.getString(WorldDefaultProperty.AUTO_UNLOAD_ENABLED));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case CREATION_TIMESTAMP:
                this.write(worldHolder, property, System.currentTimeMillis());
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case CREATOR_NAME:
                this.write(worldHolder, property, "CONSOLE");
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case DIFFICULTY:
                this.write(worldHolder, property, this.defaultProperties.getString(WorldDefaultProperty.DIFFICULTY));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case END_PORTAL_ACCESSIBLE:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.END_PORTAL_ACCESSIBLE));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case ENVIRONMENT:
                break;

            case GAME_MODE:
                this.write(worldHolder, property, this.defaultProperties.getString(WorldDefaultProperty.GAME_MODE));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case GENERATOR:
                break;

            case HUNGER_ENABLED:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.HUNGER_ENABLED));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case KEEP_SPAWN_IN_MEMORY:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.KEEP_SPAWN_IN_MEMORY));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case LINKED_END_WORLD:
                this.write(worldHolder, property, this.defaultProperties.getString(WorldDefaultProperty.END_WORLD));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case LINKED_NETHER_WORLD:
                this.write(worldHolder, property, this.defaultProperties.getString(WorldDefaultProperty.NETHER_WORLD));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case LINKED_OVERWORLD:
                this.write(worldHolder, property, this.defaultProperties.getString(WorldDefaultProperty.NORMAL_WORLD));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case NETHER_PORTAL_ACCESSIBLE:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.NETHER_PORTAL_ACCESSIBLE));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case PVP_ENABLED:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.PVP_ENABLED));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case RECEIVE_ACHIEVEMENTS:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.RECEIVE_ACHIEVEMENTS));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case REDSTONE_ENABLED:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.REDSTONE_ENABLED));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case SPAWN_ANIMALS:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ANIMALS));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case SPAWN_ENTITIES:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ENTITIES));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case SPAWN_MONSTERS:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_MONSTERS));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case TYPE:
                break;

            case WEATHER_ENABLED:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.WEATHER_ENABLED));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case WHITELIST_ENABLED:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.WHITELIST_ENABLED));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;
        }
    }

    public void removeUnusableProperties(@NotNull final String worldName) {
        if (super.getFileConfiguration().getSection(worldName) == null) {
            return;
        }

        for (final String property : super.getFileConfiguration().getSection(worldName).getKeys()) {
            if (WorldProperty.fromStoragePath(property).isPresent()) {
                continue;
            }
            super.getFileConfiguration().set(worldName + "." + property, null);
        }
        super.saveFile();
    }

    @Override
    public void updateFlag(@NotNull final BukkitWorldHolder worldHolder, @NotNull final WorldFlag worldFlag, @NotNull final String value) {
        if ((value.equalsIgnoreCase("true")) || (value.equalsIgnoreCase("false"))) {
            super.getFileConfiguration().set(worldHolder.getName() + "." + worldFlag.getStoragePath(), Boolean.valueOf(value));

        } else {
            super.getFileConfiguration().set(worldHolder.getName() + "." + worldFlag.getStoragePath(), value);
        }
        super.saveFile();
    }

    @Override
    public void write(@NotNull final BukkitWorldHolder worldHolder, @NotNull final ParsedMap<WorldProperty, Object> worldData) {
        for (final WorldProperty property : worldData.keySet()) {
            switch (property) {
                // Strings
                case CREATOR_NAME:
                case DIFFICULTY:
                case ENVIRONMENT:
                case GAME_MODE:
                case GENERATOR:
                case LINKED_END_WORLD:
                case LINKED_NETHER_WORLD:
                case LINKED_OVERWORLD:
                case TYPE:
                    super.getFileConfiguration().set(worldHolder.getName() + "." + property.getStoragePath(), worldData.getString(property));
                    break;

                // Longs
                case CREATION_TIMESTAMP:
                    super.getFileConfiguration().set(worldHolder.getName() + "." + property.getStoragePath(), worldData.getLong(property));
                    break;

                // Booleans
                case AUTO_LOAD_ENABLED:
                case AUTO_UNLOAD_ENABLED:
                case END_PORTAL_ACCESSIBLE:
                case HUNGER_ENABLED:
                case KEEP_SPAWN_IN_MEMORY:
                case NETHER_PORTAL_ACCESSIBLE:
                case PVP_ENABLED:
                case RECEIVE_ACHIEVEMENTS:
                case REDSTONE_ENABLED:
                case SPAWN_ANIMALS:
                case SPAWN_ENTITIES:
                case SPAWN_MONSTERS:
                case WHITELIST_ENABLED:
                    super.getFileConfiguration().set(worldHolder.getName() + "." + property.getStoragePath(), worldData.getBoolean(property));
                    break;

                // StringList
                case WHITELIST:
                    super.getFileConfiguration().set(worldHolder.getName() + "." + property.getStoragePath(), worldData.getStringList(property));
                    break;
            }
        }
        super.saveFile();
    }

    @Override
    public void write(@NotNull final BukkitWorldHolder worldHolder, @NotNull final WorldProperty property, @NotNull final Object value) {
        final String path = worldHolder.getName() + "." + property.getStoragePath();

        if (value instanceof Boolean booleanValue) {
            super.getFileConfiguration().set(path, booleanValue);

        } else if (value instanceof String stringValue) {
            if (stringValue.equalsIgnoreCase("true") || stringValue.equalsIgnoreCase("false")) {
                super.getFileConfiguration().set(path, Boolean.parseBoolean(stringValue));

            } else {
                super.getFileConfiguration().set(path, stringValue);
            }

        } else {
            super.getFileConfiguration().set(path, value);
        }
        super.saveFile();
    }


    @Override
    public BukkitWorldHolder getWorldHolder(@NotNull final String name) {
        return BukkitWorldHolder.builder()
                .setName(name)
                .setCreatorName(super.getFileConfiguration().getString(name + "." + WorldProperty.CREATOR_NAME.getStoragePath()))
                .setCreationTimeStamp(super.getFileConfiguration().getLong(name + "." + WorldProperty.CREATION_TIMESTAMP.getStoragePath()))
                .setAutoLoadEnabled(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.AUTO_LOAD_ENABLED.getStoragePath()))
                .setAutoUnloadEnabled(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.AUTO_UNLOAD_ENABLED.getStoragePath()))
                .setDifficulty(Difficulty.valueOf(super.getFileConfiguration().getString(name + "." + WorldProperty.DIFFICULTY.getStoragePath())))
                .setEndPortalAccessible(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.END_PORTAL_ACCESSIBLE.getStoragePath(), true))
                .setGameMode(GameMode.valueOf(super.getFileConfiguration().getString(name + "." + WorldProperty.GAME_MODE.getStoragePath())))
                .setHungerEnabled(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.HUNGER_ENABLED.getStoragePath()))
                .setKeepSpawnInMemory(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.KEEP_SPAWN_IN_MEMORY.getStoragePath()))
                .setNetherPortalAccessible(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.NETHER_PORTAL_ACCESSIBLE.getStoragePath(), true))
                .setPvpEnabled(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.PVP_ENABLED.getStoragePath(), true))
                .setReceiveAchievements(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.RECEIVE_ACHIEVEMENTS.getStoragePath()))
                .setRedstoneEnabled(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.REDSTONE_ENABLED.getStoragePath()))
                .setSpawnAnimals(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.SPAWN_ANIMALS.getStoragePath()))
                .setSpawnMonsters(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.SPAWN_MONSTERS.getStoragePath()))
                .setSpawnEntities(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.SPAWN_ENTITIES.getStoragePath()))
                .setWeatherEnabled(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.WEATHER_ENABLED.getStoragePath()))
                .setEnvironment(World.Environment.valueOf(super.getFileConfiguration().getString(name + "." + WorldProperty.ENVIRONMENT.getStoragePath())))
                .setGenerator(super.getFileConfiguration().getString(name + "." + WorldProperty.GENERATOR.getStoragePath()))
                .setType(WorldType.fromString(super.getFileConfiguration().getString(name + "." + WorldProperty.TYPE.getStoragePath())).orElseThrow())
                .setEndWorldName(super.getFileConfiguration().getString(name + "." + WorldProperty.LINKED_END_WORLD.getStoragePath()))
                .setNetherWorldName(super.getFileConfiguration().getString(name + "." + WorldProperty.LINKED_NETHER_WORLD.getStoragePath()))
                .setNormalWorldName(super.getFileConfiguration().getString(name + "." + WorldProperty.LINKED_OVERWORLD.getStoragePath()))
                .setWhitelist(super.getFileConfiguration().getStringList(name + "." + WorldProperty.WHITELIST.getStoragePath()))
                .setWhitelistEnabled(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.WHITELIST_ENABLED.getStoragePath()))
                .build();
    }

    @Override
    public Map<String, BukkitWorldHolder> getWorldHolders() {
        final Map<String, BukkitWorldHolder> worlds = new HashMap<>();

        for (final String entry : super.getFileConfiguration().getKeys()) {
            worlds.put(entry, this.getWorldHolder(entry));
        }
        return worlds;
    }

}
