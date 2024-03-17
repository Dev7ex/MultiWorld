package com.dev7ex.multiworld.api.bukkit.world;

import com.dev7ex.multiworld.api.world.WorldFlag;
import com.dev7ex.multiworld.api.world.WorldHolder;
import com.dev7ex.multiworld.api.world.WorldType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dev7ex
 * @since 29.05.2023
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Builder(setterPrefix = "set")
public class BukkitWorldHolder implements WorldHolder {

    private String name;
    private String creatorName;
    private long creationTimeStamp;
    private WorldType type;
    private GameMode gameMode;
    private Difficulty difficulty;
    private boolean pvpEnabled;
    private boolean loaded;
    private boolean spawnAnimals;
    private boolean spawnMonsters;
    private boolean spawnEntities;
    private boolean endPortalAccessible;
    private boolean netherPortalAccessible;
    private String endWorldName;
    private String netherWorldName;
    private String normalWorldName;
    private List<String> whitelist = new ArrayList<>();
    private boolean whitelistEnabled;
    private boolean autoLoaded = false;

    public World getWorld() {
        return Bukkit.getWorld(this.name);
    }

    @Override
    public void updateFlag(@NotNull final WorldFlag flag, @NotNull final String value) {
        switch (flag) {
            case PVP_ENABLED:
                this.pvpEnabled = Boolean.parseBoolean(value);
                break;

            case GAME_MODE:
                this.gameMode = GameMode.valueOf(value);
                break;

            case DIFFICULTY:
                this.difficulty = Difficulty.valueOf(value);
                if (this.loaded) {
                    this.getWorld().setDifficulty(this.difficulty);
                }
                break;

            case SPAWN_ANIMALS:
                this.spawnAnimals = Boolean.parseBoolean(value);
                if (this.loaded) {
                    this.getWorld().setSpawnFlags(this.spawnMonsters, this.spawnAnimals);
                }
                break;

            case SPAWN_MONSTERS:
                this.spawnMonsters = Boolean.parseBoolean(value);
                if (this.loaded) {
                    this.getWorld().setSpawnFlags(this.spawnMonsters, this.spawnAnimals);
                }
                break;

            case SPAWN_ENTITIES:
                this.spawnEntities = Boolean.parseBoolean(value);
                break;

            case END_PORTAL_ACCESSIBLE:
                this.endPortalAccessible = Boolean.parseBoolean(value);
                break;

            case NETHER_PORTAL_ACCESSIBLE:
                this.netherPortalAccessible = Boolean.parseBoolean(value);
                break;

            case WORLD_TYPE:
                this.type = WorldType.valueOf(value);
                break;
        }
    }

}
