package com.dev7ex.multiworld.world;

import com.dev7ex.common.collect.map.ParsedMap;
import com.dev7ex.common.io.file.configuration.Configuration;
import com.dev7ex.common.io.file.configuration.ConfigurationProperties;
import com.dev7ex.common.io.file.configuration.YamlConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldConfiguration;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.world.WorldDefaultProperty;
import com.dev7ex.multiworld.api.world.WorldFlag;
import com.dev7ex.multiworld.api.world.WorldProperty;
import com.dev7ex.multiworld.api.world.WorldType;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@ConfigurationProperties(fileName = "world.yml", provider = YamlConfiguration.class)
public class DefaultWorldConfiguration extends Configuration implements BukkitWorldConfiguration {

    private final ParsedMap<WorldDefaultProperty, Object> defaultProperties;

    public DefaultWorldConfiguration(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.defaultProperties = plugin.getConfiguration().getDefaultProperties();
    }

    @Override
    public void add(@NotNull final BukkitWorldHolder worldHolder) {
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.CREATOR_NAME.getStoragePath(), worldHolder.getCreatorName());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.CREATION_TIMESTAMP.getStoragePath(), worldHolder.getCreationTimeStamp());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.TYPE.getStoragePath(), worldHolder.getType().toString());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.GAME_MODE.getStoragePath(), worldHolder.getGameMode().toString());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.DIFFICULTY.getStoragePath(), worldHolder.getDifficulty().toString());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.PVP_ENABLED.getStoragePath(), worldHolder.isPvpEnabled());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.SPAWN_ANIMALS.getStoragePath(), worldHolder.isSpawnAnimals());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.SPAWN_MONSTERS.getStoragePath(), worldHolder.isSpawnMonsters());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.SPAWN_ENTITIES.getStoragePath(), worldHolder.isSpawnEntities());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.END_PORTAL_ACCESSIBLE.getStoragePath(), worldHolder.isEndPortalAccessible());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.NETHER_PORTAL_ACCESSIBLE.getStoragePath(), worldHolder.isNetherPortalAccessible());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.END_WORLD.getStoragePath(), worldHolder.getEndWorldName());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.NETHER_WORLD.getStoragePath(), worldHolder.getNetherWorldName());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.NORMAL_WORLD.getStoragePath(), worldHolder.getNormalWorldName());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.WHITELIST.getStoragePath(), Collections.emptyList());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.WHITELIST_ENABLED.getStoragePath(), worldHolder.isWhitelistEnabled());
        super.saveFile();
    }

    @Override
    public void remove(@NotNull final BukkitWorldHolder worldHolder) {
        this.remove(worldHolder.getName());
    }

    @Override
    public void remove(@NotNull final String name) {
        super.getFileConfiguration().set(name, null);
        super.saveFile();
    }

    @Override
    public boolean contains(@NotNull final String name) {
        return super.getFileConfiguration().contains(name);
    }

    @Override
    public boolean hasProperty(@NotNull final String name, @NotNull final WorldProperty property) {
        return super.getFileConfiguration().contains(name + "." + property.getStoragePath());
    }

    @Override
    public void addMissingProperty(@NotNull final BukkitWorldHolder worldHolder, @NotNull final WorldProperty property) {
        switch (property) {
            case LOAD_AUTO:
                this.write(worldHolder, property, false);
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case CREATOR_NAME:
                this.write(worldHolder, property, "CONSOLE");
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case CREATION_TIMESTAMP:
                this.write(worldHolder, property, System.currentTimeMillis());
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case TYPE:
                break;

            case GAME_MODE:
                this.write(worldHolder, property, this.defaultProperties.getString(WorldDefaultProperty.GAME_MODE));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case DIFFICULTY:
                this.write(worldHolder, property, this.defaultProperties.getString(WorldDefaultProperty.DIFFICULTY));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case PVP_ENABLED:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.PVP_ENABLED));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case SPAWN_ANIMALS:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ANIMALS));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case SPAWN_MONSTERS:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_MONSTERS));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case SPAWN_ENTITIES:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ENTITIES));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case END_PORTAL_ACCESSIBLE:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.END_PORTAL_ACCESSIBLE));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case NETHER_PORTAL_ACCESSIBLE:
                this.write(worldHolder, property, this.defaultProperties.getBoolean(WorldDefaultProperty.NETHER_PORTAL_ACCESSIBLE));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case NORMAL_WORLD:
                this.write(worldHolder, property, this.defaultProperties.getString(WorldDefaultProperty.NORMAL_WORLD));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case NETHER_WORLD:
                this.write(worldHolder, property, this.defaultProperties.getString(WorldDefaultProperty.NETHER_WORLD));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case END_WORLD:
                this.write(worldHolder, property, this.defaultProperties.getString(WorldDefaultProperty.END_WORLD));
                MultiWorldPlugin.getInstance().getLogger().info("Adding Missing Property [" + property.name() + "] to " + worldHolder.getName());
                break;

            case WHITELIST:
                this.write(worldHolder, property, Collections.emptyList());
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
                case CREATOR_NAME: case TYPE: case GAME_MODE: case DIFFICULTY: case END_WORLD: case NETHER_WORLD: case NORMAL_WORLD:
                    super.getFileConfiguration().set(worldHolder.getName() + "." + property.getStoragePath(), worldData.getString(property));
                    break;

                case CREATION_TIMESTAMP:
                    super.getFileConfiguration().set(worldHolder.getName() + "." + property.getStoragePath(), worldData.getLong(property));
                    break;

                case PVP_ENABLED: case SPAWN_ANIMALS: case SPAWN_MONSTERS: case SPAWN_ENTITIES:
                    case WHITELIST_ENABLED: case END_PORTAL_ACCESSIBLE: case NETHER_PORTAL_ACCESSIBLE: case LOAD_AUTO:
                    super.getFileConfiguration().set(worldHolder.getName() + "." + property.getStoragePath(), worldData.getBoolean(property));
                    break;

                case WHITELIST:
                    super.getFileConfiguration().set(worldHolder.getName() + "." + property.getStoragePath(), worldData.getStringList(property));
                    break;
            }
        }
        super.saveFile();
    }

    @Override
    public void write(@NotNull final BukkitWorldHolder worldHolder, @NotNull final WorldProperty property, @NotNull final Object value) {
        if (value instanceof Boolean booleanValue) {
            super.getFileConfiguration().set(worldHolder.getName() + "." + property.getStoragePath(), booleanValue);

        } else if ((value instanceof final String stringValue) && (stringValue.equalsIgnoreCase("true") || (stringValue.equalsIgnoreCase("false")))) {
            final boolean booleanValue = (boolean) value;
            super.getFileConfiguration().set(worldHolder.getName() + "." + property.getStoragePath(), booleanValue);

        } else {
            super.getFileConfiguration().set(worldHolder.getName() + "." + property.getStoragePath(), value);
        }
        super.saveFile();
    }

    @Override
    public BukkitWorldHolder getWorldHolder(@NotNull final String name) {
        return BukkitWorldHolder.builder()
                .setName(name)
                .setCreatorName(super.getFileConfiguration().getString(name + "." + WorldProperty.CREATOR_NAME.getStoragePath()))
                .setCreationTimeStamp(super.getFileConfiguration().getLong(name + "." + WorldProperty.CREATION_TIMESTAMP.getStoragePath()))
                .setAutoLoaded(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.LOAD_AUTO.getStoragePath(), false))
                .setType(WorldType.fromString(super.getFileConfiguration().getString(name + "." + WorldProperty.TYPE.getStoragePath())).orElseThrow())
                .setGameMode(GameMode.valueOf(super.getFileConfiguration().getString(name + "." + WorldProperty.GAME_MODE.getStoragePath())))
                .setDifficulty(Difficulty.valueOf(super.getFileConfiguration().getString(name + "." + WorldProperty.DIFFICULTY.getStoragePath())))
                .setPvpEnabled(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.PVP_ENABLED.getStoragePath()))
                .setSpawnAnimals(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.SPAWN_ANIMALS.getStoragePath()))
                .setSpawnMonsters(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.SPAWN_MONSTERS.getStoragePath()))
                .setSpawnEntities(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.SPAWN_ENTITIES.getStoragePath()))
                .setEndPortalAccessible(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.END_PORTAL_ACCESSIBLE.getStoragePath(), true))
                .setNetherPortalAccessible(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.NETHER_PORTAL_ACCESSIBLE.getStoragePath(), true))
                .setEndWorldName(super.getFileConfiguration().getString(name + "." + WorldProperty.END_WORLD.getStoragePath()))
                .setNetherWorldName(super.getFileConfiguration().getString(name + "." + WorldProperty.NETHER_WORLD.getStoragePath()))
                .setNormalWorldName(super.getFileConfiguration().getString(name + "." + WorldProperty.NORMAL_WORLD.getStoragePath()))
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
