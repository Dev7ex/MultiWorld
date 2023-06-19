package com.dev7ex.multiworld.world;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class WorldProperties {

    private String worldName;
    private String worldCreator;
    private long creationTime;
    private long lastWorldInteraction;
    private boolean loaded;
    private WorldType worldType;
    private GameMode gameMode;
    private Difficulty difficulty;
    private boolean pvpEnabled;
    private boolean spawnAnimals;
    private boolean spawnMonsters;

    public WorldProperties(final String worldName, final String worldCreator, final long creationTime,
                           final long lastWorldInteraction, final WorldType worldType, final Difficulty difficulty,
                           final GameMode gameMode, final boolean pvpEnabled, final boolean spawnAnimals, final boolean spawnMonsters) {
        this.worldName = worldName;
        this.worldCreator = worldCreator;
        this.creationTime = creationTime;
        this.lastWorldInteraction = lastWorldInteraction;
        this.worldType = worldType;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.pvpEnabled = pvpEnabled;
        this.spawnAnimals = spawnAnimals;
        this.spawnMonsters = spawnMonsters;
    }

    public void updateWorldOption(final WorldOption worldOption, final String value) {
        switch (worldOption) {
            case PVP_ENABLED:
                this.pvpEnabled = Boolean.parseBoolean(value);
                break;

            case GAMEMODE:
                this.gameMode = GameMode.valueOf(String.valueOf(value));
                break;

            case DIFFICULTY:
                this.difficulty = Difficulty.valueOf(String.valueOf(value));
                if (this.loaded) {
                    Objects.requireNonNull(Bukkit.getWorld(this.worldName)).setDifficulty(this.difficulty);
                }
                break;

            case SPAWN_ANIMALS:
                this.spawnAnimals = Boolean.parseBoolean(value);
                if (this.loaded) {
                    Objects.requireNonNull(Bukkit.getWorld(this.worldName)).setSpawnFlags(this.spawnMonsters, this.spawnAnimals);
                }
                break;

            case SPAWN_MONSTERS:
                this.spawnMonsters = Boolean.parseBoolean(value);
                if (this.loaded) {
                    Objects.requireNonNull(Bukkit.getWorld(this.worldName)).setSpawnFlags(this.spawnMonsters, this.spawnAnimals);
                }
                break;

            case WORLD_TYPE:
                this.worldType = WorldType.valueOf(String.valueOf(value));
                break;
        }
    }

    public String formatCreationDate(final long value) {
        return new SimpleDateFormat("dd.MM.yyyy").format(new Date(value));
    }

}
