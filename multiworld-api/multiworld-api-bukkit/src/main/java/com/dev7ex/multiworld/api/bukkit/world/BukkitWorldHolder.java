package com.dev7ex.multiworld.api.bukkit.world;

import com.dev7ex.multiworld.api.MultiWorldApiProvider;
import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApiConfiguration;
import com.dev7ex.multiworld.api.bukkit.world.location.BukkitWorldLocation;
import com.dev7ex.multiworld.api.world.WorldFlag;
import com.dev7ex.multiworld.api.world.WorldHolder;
import com.dev7ex.multiworld.api.world.WorldType;
import com.dev7ex.multiworld.api.world.location.WorldLocation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a holder for Bukkit worlds in a multi-world environment.
 * Implements WorldHolder interface.
 * This class provides properties and methods for managing Bukkit worlds.
 *
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
    private boolean autoLoadEnabled;
    private boolean autoUnloadEnabled;
    private Difficulty difficulty;
    private boolean endPortalAccessible;
    private World.Environment environment;
    private GameMode gameMode;
    private String forceGameMode;
    private String generator;
    private boolean hungerEnabled;
    private boolean keepSpawnInMemory;
    private String endWorldName;
    private String netherWorldName;
    private String normalWorldName;
    private boolean netherPortalAccessible;
    private boolean pvpEnabled;
    private boolean receiveAchievements;
    private boolean redstoneEnabled;
    private boolean spawnAnimals;
    private boolean spawnMonsters;
    private boolean spawnEntities;
    private WorldType type;
    private boolean weatherEnabled;
    private List<String> whitelist = new ArrayList<>();
    private boolean whitelistEnabled;
    private boolean loaded;
    private long loadTimeStamp;
    private long lastActivity;

    /**
     * Gets the Bukkit world associated with this world holder.
     *
     * @return The Bukkit world, or null if not found.
     */
    public World getWorld() {
        return Bukkit.getWorld(this.name);
    }

    @Override
    public WorldLocation getSpawnLocation() {
        return BukkitWorldLocation.of(this.getWorld().getSpawnLocation());
    }

    /**
     * Updates the value of the specified world flag.
     *
     * @param flag  The world flag to update.
     * @param value The new value for the flag.
     */
    @Override
    public void updateFlag(@NotNull final WorldFlag flag, @NotNull final String value) {
        switch (flag) {
            case AUTO_LOAD_ENABLED:
                this.autoLoadEnabled = Boolean.parseBoolean(value);
                break;

            case AUTO_UNLOAD_ENABLED:
                this.autoUnloadEnabled = Boolean.parseBoolean(value);
                break;

            case DIFFICULTY:
                this.difficulty = Difficulty.valueOf(value);
                if (this.loaded) {
                    this.getWorld().setDifficulty(this.difficulty);
                }
                break;

            case END_PORTAL_ACCESSIBLE:
                this.endPortalAccessible = Boolean.parseBoolean(value);
                break;

            case GAME_MODE:
                this.gameMode = GameMode.valueOf(value);
                this.getWorld().getPlayers().forEach(player -> player.setGameMode(this.gameMode));
                break;

            case FORCE_GAME_MODE:
                GameMode gameMode = MultiWorldApiProvider.getMultiWorldApi().getWorldConfiguration().getWorldHolder(this.getWorld().getName()).getGameMode();
                switch (value) {
                    case "true" -> {
                        this.forceGameMode = "true";
                        this.getWorld().getPlayers().forEach(player -> player.setGameMode(gameMode));
                    }
                    case "false" -> this.forceGameMode = "false";
                    case "false_with_permission" -> {
                        this.forceGameMode = "false_with_permission";
                        for (Player player : this.getWorld().getPlayers()) {
                            if (!player.hasPermission("multiworld.bypass.forcegamemode")) {
                                player.setGameMode(gameMode);
                            }
                        }
                    }
                    default -> this.forceGameMode = MultiWorldApiProvider.getMultiWorldApi().getConfiguration().getString(MultiWorldBukkitApiConfiguration.Entry.SETTINGS_DEFAULTS_FORCE_GAME_MODE.getPath());
                }
                break;

            case HUNGER_ENABLED:
                this.hungerEnabled = Boolean.parseBoolean(value);
                if (!this.hungerEnabled) {
                    this.getWorld().getPlayers().forEach(player -> player.setSaturation(20.00F));
                }
                break;

            case KEEP_SPAWN_IN_MEMORY:
                this.keepSpawnInMemory = Boolean.parseBoolean(value);
                this.getWorld().setKeepSpawnInMemory(this.keepSpawnInMemory);
                break;

            case NETHER_PORTAL_ACCESSIBLE:
                this.netherPortalAccessible = Boolean.parseBoolean(value);
                break;

            case PVP_ENABLED:
                this.pvpEnabled = Boolean.parseBoolean(value);
                break;

            case RECEIVE_ACHIEVEMENTS:
                this.receiveAchievements = Boolean.parseBoolean(value);
                break;

            case REDSTONE_ENABLED:
                this.redstoneEnabled = Boolean.parseBoolean(value);
                break;

            case SPAWN_ANIMALS:
                this.spawnAnimals = Boolean.parseBoolean(value);
                if (this.loaded) {
                    this.getWorld().setSpawnFlags(this.spawnMonsters, this.spawnAnimals);
                    this.getWorld().getEntities().stream().filter(entity -> entity instanceof Animals).forEach(Entity::remove);
                }
                break;

            case SPAWN_MONSTERS:
                this.spawnMonsters = Boolean.parseBoolean(value);
                if (this.loaded) {
                    this.getWorld().setSpawnFlags(this.spawnMonsters, this.spawnAnimals);
                    this.getWorld().getEntities().stream().filter(entity -> entity instanceof Monster).forEach(Entity::remove);
                }
                break;

            case SPAWN_ENTITIES:
                this.spawnEntities = Boolean.parseBoolean(value);
                break;

            case WEATHER_ENABLED:
                this.weatherEnabled = Boolean.parseBoolean(value);
                break;
        }
    }

    @Override
    public BukkitWorldHolder clone() {
        try {
            return (BukkitWorldHolder) super.clone();
        } catch (final CloneNotSupportedException exception) {
            throw new AssertionError();
        }

    }

}
