package com.dev7ex.multiworld.world;

import com.dev7ex.common.bukkit.configuration.ConfigurationBase;
import com.dev7ex.common.bukkit.configuration.ConfigurationProperties;
import com.dev7ex.common.map.ParsedMap;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldConfiguration;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.world.WorldDefaultProperty;
import com.dev7ex.multiworld.api.world.WorldFlag;
import com.dev7ex.multiworld.api.world.WorldProperty;
import com.dev7ex.multiworld.api.world.WorldType;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@ConfigurationProperties(fileName = "world.yml")
public class DefaultWorldConfiguration extends ConfigurationBase implements BukkitWorldConfiguration {

    public DefaultWorldConfiguration(@NotNull final Plugin plugin) {
        super(plugin);
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

        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.END_WORLD.getStoragePath(), MultiWorldPlugin.getInstance()
                .getConfiguration()
                .getDefaultProperties()
                .getString(WorldDefaultProperty.END_WORLD));

        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.NETHER_WORLD.getStoragePath(), MultiWorldPlugin.getInstance()
                .getConfiguration()
                .getDefaultProperties()
                .getString(WorldDefaultProperty.NETHER_WORLD));

        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.NORMAL_WORLD.getStoragePath(), MultiWorldPlugin.getInstance()
                .getConfiguration()
                .getDefaultProperties()
                .getString(WorldDefaultProperty.NORMAL_WORLD));

        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.WHITELIST.getStoragePath(), Collections.emptyList());
        super.getFileConfiguration().set(worldHolder.getName() + "." + WorldProperty.WHITELIST_ENABLED.getStoragePath(), MultiWorldPlugin.getInstance()
                .getConfiguration()
                .getDefaultProperties()
                .getBoolean(WorldDefaultProperty.WHITELIST_ENABLED));

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
                case CREATOR_NAME:
                case TYPE:
                case GAME_MODE:
                case DIFFICULTY:
                case END_WORLD:
                case NETHER_WORLD:
                case NORMAL_WORLD:
                    super.getFileConfiguration().set(worldHolder.getName() + "." + property.getStoragePath(), worldData.getString(property));
                    break;

                case CREATION_TIMESTAMP:
                    super.getFileConfiguration().set(worldHolder.getName() + "." + property.getStoragePath(), worldData.getLong(property));
                    break;

                case PVP_ENABLED:
                case SPAWN_ANIMALS:
                case SPAWN_MONSTERS:
                case WHITELIST_ENABLED:
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
                .setType(WorldType.fromString(super.getFileConfiguration().getString(name + "." + WorldProperty.TYPE.getStoragePath())).orElseThrow())
                .setGameMode(GameMode.valueOf(super.getFileConfiguration().getString(name + "." + WorldProperty.GAME_MODE.getStoragePath())))
                .setDifficulty(Difficulty.valueOf(super.getFileConfiguration().getString(name + "." + WorldProperty.DIFFICULTY.getStoragePath())))
                .setPvpEnabled(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.PVP_ENABLED.getStoragePath()))
                .setSpawnAnimals(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.SPAWN_ANIMALS.getStoragePath()))
                .setSpawnMonsters(super.getFileConfiguration().getBoolean(name + "." + WorldProperty.SPAWN_MONSTERS.getStoragePath()))
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

        for (final String entry : super.getFileConfiguration().getKeys(false)) {
            worlds.put(entry, this.getWorldHolder(entry));
        }
        return worlds;
    }

}
