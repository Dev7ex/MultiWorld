package com.dev7ex.multiworld.world;

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

@Setter
@Getter
public final class WorldProperties {

    private final String worldName;
    private String worldCreator;
    private long creationTime;
    private long lastWorldInteraction;
    private boolean loaded;
    private WorldType worldType;
    private GameMode gameMode;
    private Difficulty difficulty;
    private boolean pvpEnabled;

    public WorldProperties(final String worldName, final String worldCreator, final long creationTime, final long lastWorldInteraction, final WorldType worldType, final Difficulty difficulty, final GameMode gameMode, final boolean pvpEnabled) {
        this.worldName = worldName;
        this.worldCreator = worldCreator;
        this.creationTime = creationTime;
        this.lastWorldInteraction = lastWorldInteraction;
        this.worldType = worldType;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.pvpEnabled = pvpEnabled;
    }

    public final void updateWorldOption(final WorldOption worldOption, final String value) {
        switch (worldOption) {
            case PVP:
                this.pvpEnabled = Boolean.parseBoolean(value);
                break;

            case GAME_MODE:
                this.gameMode = GameMode.valueOf(String.valueOf(value));
                break;

            case DIFFICULTY:
                this.difficulty = Difficulty.valueOf(String.valueOf(value));
                if (this.loaded) {
                    Objects.requireNonNull(Bukkit.getWorld(this.worldName)).setDifficulty(this.difficulty);
                }
                break;

            case WORLD_TYPE:
                this.worldType = WorldType.valueOf(String.valueOf(value));
                break;
        }
    }

    public final String formatCreationDate() {
        return new SimpleDateFormat("dd.MM.yyyy").format(new Date());
    }

}
