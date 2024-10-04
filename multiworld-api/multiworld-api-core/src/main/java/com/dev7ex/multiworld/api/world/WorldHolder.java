package com.dev7ex.multiworld.api.world;

import com.dev7ex.multiworld.api.world.location.WorldLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents an object that holds information about a world instance in the server software.
 * This interface provides methods to access and modify various properties of the world.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldHolder {

    /**
     * Checks if weather is enabled in the world.
     *
     * @return True if weather is enabled, false otherwise.
     */
    boolean isWeatherEnabled();

    /**
     * Sets whether weather is enabled in the world.
     *
     * @param weatherEnabled True to enable weather, false to disable it.
     */
    void setWeatherEnabled(final boolean weatherEnabled);

    /**
     * Checks if redstone is enabled in the world.
     *
     * @return True if redstone is enabled, false otherwise.
     */
    boolean isRedstoneEnabled();

    /**
     * Sets whether redstone is enabled in the world.
     *
     * @param redstoneEnabled True to enable redstone, false to disable it.
     */
    void setRedstoneEnabled(final boolean redstoneEnabled);

    /**
     * Checks if the world is set to automatically unload when inactive.
     *
     * @return True if auto-unloading is enabled, false otherwise.
     */
    boolean isAutoUnloadEnabled();

    /**
     * Sets whether the world should automatically unload when inactive.
     *
     * @param autoUnloadEnabled True to enable auto-unloading, false to disable it.
     */
    void setAutoUnloadEnabled(final boolean autoUnloadEnabled);

    /**
     * Checks if hunger is enabled in the world.
     *
     * @return True if hunger is enabled, false otherwise.
     */
    boolean isHungerEnabled();

    /**
     * Sets whether hunger is enabled in the world.
     *
     * @param hungerEnabled True to enable hunger, false to disable it.
     */
    void setHungerEnabled(final boolean hungerEnabled);

    /**
     * Checks if the world's spawn is kept in memory.
     *
     * @return True if the spawn is kept in memory, false otherwise.
     */
    boolean isKeepSpawnInMemory();

    /**
     * Sets whether the world's spawn is kept in memory.
     *
     * @param keepSpawnInMemory True to keep the spawn in memory, false otherwise.
     */
    void setKeepSpawnInMemory(final boolean keepSpawnInMemory);

    /**
     * Gets the last recorded activity in the world.
     *
     * @return The timestamp of the last activity.
     */
    long getLastActivity();

    /**
     * Sets the last recorded activity in the world.
     *
     * @param lastActivity The timestamp of the last activity.
     */
    void setLastActivity(final long lastActivity);

    long getLoadTimeStamp();

    void setLoadTimeStamp(final long loadTimeStamp);

    /**
     * Gets the name of the world.
     *
     * @return The name of the world.
     */
    String getName();

    /**
     * Gets the name of the creator of the world.
     *
     * @return The name of the creator of the world.
     */
    String getCreatorName();

    /**
     * Gets the timestamp of when the world was created.
     *
     * @return The timestamp of the world creation.
     */
    long getCreationTimeStamp();

    /**
     * Gets the type of the world.
     *
     * @return The type of the world.
     */
    WorldType getType();

    /**
     * Gets the game mode of the world.
     *
     * @param <T> The type of game mode.
     * @return The game mode of the world.
     */
    <T> T getGameMode();

    /**
     * Gets the difficulty level of the world.
     *
     * @param <T> The type of difficulty level.
     * @return The difficulty level of the world.
     */
    <T> T getDifficulty();

    /**
     * Checks if PvP is enabled in the world.
     *
     * @return True if PvP is enabled, false otherwise.
     */
    boolean isPvpEnabled();

    /**
     * Checks if the world is loaded.
     *
     * @return True if the world is loaded, false otherwise.
     */
    boolean isLoaded();

    /**
     * Checks if animals spawn in the world.
     *
     * @return True if animals spawn, false otherwise.
     */
    boolean isSpawnAnimals();

    /**
     * Checks if monsters spawn in the world.
     *
     * @return True if monsters spawn, false otherwise.
     */
    boolean isSpawnMonsters();

    /**
     * Checks if entities spawn in the world.
     *
     * @return True if entities spawn, false otherwise.
     */
    boolean isSpawnEntities();

    /**
     * Checks if the end portal is accessible in the world.
     *
     * @return True if the end portal is accessible, false otherwise.
     */
    boolean isEndPortalAccessible();

    /**
     * Sets whether the end portal is accessible in the world.
     *
     * @param accessible True to make the end portal accessible, false otherwise.
     */
    void setEndPortalAccessible(final boolean accessible);

    /**
     * Checks if the nether portal is accessible in the world.
     *
     * @return True if the nether portal is accessible, false otherwise.
     */
    boolean isNetherPortalAccessible();

    /**
     * Sets whether the nether portal is accessible in the world.
     *
     * @param accessible True to make the nether portal accessible, false otherwise.
     */
    void setNetherPortalAccessible(final boolean accessible);

    /**
     * Gets the whitelist of players for the world.
     *
     * @return The whitelist of players for the world.
     */
    List<String> getWhitelist();

    /**
     * Checks if the whitelist is enabled for the world.
     *
     * @return True if the whitelist is enabled, false otherwise.
     */
    boolean isWhitelistEnabled();

    /**
     * Sets whether the whitelist is enabled for the world.
     *
     * @param enabled True to enable the whitelist, false to disable it.
     */
    void setWhitelistEnabled(final boolean enabled);

    /**
     * Updates the value of a world flag.
     *
     * @param flag  The flag to update.
     * @param value The new value of the flag.
     */
    void updateFlag(@NotNull final WorldFlag flag, @NotNull final String value);

    /**
     * Checks if the world is auto-loaded.
     *
     * @return True if the world is auto-loaded, false otherwise.
     */
    boolean isAutoLoadEnabled();

    /**
     * Gets the name of the end world associated with this world.
     *
     * @return The name of the end world.
     */
    @Nullable
    String getEndWorldName();

    /**
     * Sets the name of the end world associated with this world.
     *
     * @param endWorldName The name of the end world.
     */
    void setEndWorldName(@NotNull final String endWorldName);

    /**
     * Gets the name of the nether world associated with this world.
     *
     * @return The name of the nether world.
     */
    @Nullable
    String getNetherWorldName();

    /**
     * Sets the name of the nether world associated with this world.
     *
     * @param netherWorldName The name of the nether world.
     */
    void setNetherWorldName(@NotNull final String netherWorldName);

    /**
     * Gets the name of the normal world associated with this world.
     *
     * @return The name of the normal world.
     */
    @Nullable
    String getNormalWorldName();

    /**
     * Sets the name of the normal world associated with this world.
     *
     * @param normalWorldName The name of the normal world.
     */
    void setNormalWorldName(@NotNull final String normalWorldName);

    /**
     * Checks if the world receives achievements.
     *
     * @return True if the world receives achievements, false otherwise.
     */
    boolean isReceiveAchievements();

    /**
     * Sets whether the world receives achievements.
     *
     * @param receiveAchievements True to enable achievements, false to disable them.
     */
    void setReceiveAchievements(final boolean receiveAchievements);

    WorldLocation getSpawnLocation();

}