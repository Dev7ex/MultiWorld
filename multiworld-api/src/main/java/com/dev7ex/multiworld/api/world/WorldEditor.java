
package com.dev7ex.multiworld.api.world;

import com.dev7ex.multiworld.api.util.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.SpawnCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Dev7ex
 * @since 28.10.2026
 */
public final class WorldEditor {

    private final World world;

    private WorldEditor(@NotNull final World world) {
        this.world = Objects.requireNonNull(world, "world");
    }

    @NotNull
    public static WorldEditor of(@NotNull final World world) {
        return new WorldEditor(world);
    }

    @NotNull
    public static WorldEditor of(@NotNull final String worldName) {
        return new WorldEditor(Objects.requireNonNull(Bukkit.getWorld(worldName), "world"));
    }

    @NotNull
    public WorldEditor difficulty(@NotNull final Difficulty difficulty) {
        BukkitUtil.checkPrimaryThread();
        this.world.setDifficulty(Objects.requireNonNull(difficulty));
        return this;
    }

    @NotNull
    public WorldEditor hardcore(final boolean enabled) {
        BukkitUtil.checkPrimaryThread();
        this.world.setHardcore(enabled);
        return this;
    }

    @NotNull
    public WorldEditor pvp(final boolean enabled) {
        BukkitUtil.checkPrimaryThread();
        this.world.setPVP(enabled);
        return this;
    }

    @NotNull
    public WorldEditor autoSave(final boolean enabled) {
        BukkitUtil.checkPrimaryThread();
        this.world.setAutoSave(enabled);
        return this;
    }

    @NotNull
    public WorldEditor spawnFlags(final boolean monsters, final boolean animals) {
        BukkitUtil.checkPrimaryThread();
        this.world.setSpawnFlags(monsters, animals);
        return this;
    }

    @NotNull
    public WorldEditor spawnLimit(@NotNull final SpawnCategory category, @Range(from = 0, to = Integer.MAX_VALUE) final int limit) {
        BukkitUtil.checkPrimaryThread();
        this.world.setSpawnLimit(Objects.requireNonNull(category), limit);
        return this;
    }

    @NotNull
    public WorldEditor ticksPerSpawn(@NotNull final SpawnCategory category, @Range(from = 0, to = Integer.MAX_VALUE) final int ticks) {
        BukkitUtil.checkPrimaryThread();
        this.world.setTicksPerSpawns(Objects.requireNonNull(category), ticks);
        return this;
    }

    @NotNull
    public WorldEditor viewDistance(@Range(from = 2, to = 32) final int distance) {
        BukkitUtil.checkPrimaryThread();
        this.world.setViewDistance(distance);
        return this;
    }

    @NotNull
    public WorldEditor simulationDistance(@Range(from = 2, to = 32) final int distance) {
        BukkitUtil.checkPrimaryThread();
        this.world.setSimulationDistance(distance);
        return this;
    }

    @NotNull
    public WorldEditor sendViewDistance(@Range(from = 2, to = 32) final int distance) {
        BukkitUtil.checkPrimaryThread();
        this.world.setSendViewDistance(distance);
        return this;
    }

    @NotNull
    public WorldEditor time(@Range(from = 0, to = Long.MAX_VALUE) final long ticks) {
        BukkitUtil.checkPrimaryThread();
        this.world.setTime(ticks);
        return this;
    }

    @NotNull
    public WorldEditor fullTime(final long ticks) {
        BukkitUtil.checkPrimaryThread();
        this.world.setFullTime(ticks);
        return this;
    }

    @NotNull
    public WorldEditor storm(final boolean enabled) {
        BukkitUtil.checkPrimaryThread();
        this.world.setStorm(enabled);
        return this;
    }

    @NotNull
    public WorldEditor thundering(final boolean enabled) {
        BukkitUtil.checkPrimaryThread();
        this.world.setThundering(enabled);
        return this;
    }

    @NotNull
    public WorldEditor weatherDuration(@Range(from = 0, to = Integer.MAX_VALUE) final int ticks) {
        BukkitUtil.checkPrimaryThread();
        this.world.setWeatherDuration(ticks);
        return this;
    }

    @NotNull
    public WorldEditor clearWeatherDuration(@Range(from = 0, to = Integer.MAX_VALUE) final int ticks) {
        BukkitUtil.checkPrimaryThread();
        this.world.setClearWeatherDuration(ticks);
        return this;
    }

    @NotNull
    public WorldEditor thunderDuration(@Range(from = 0, to = Integer.MAX_VALUE) final int ticks) {
        BukkitUtil.checkPrimaryThread();
        this.world.setThunderDuration(ticks);
        return this;
    }

    @NotNull
    public WorldEditor spawn(final int x, final int y, final int z) {
        return this.spawn(x, y, z, 0.0F);
    }

    @NotNull
    public WorldEditor spawn(final int x, final int y, final int z, final float yaw) {
        BukkitUtil.checkPrimaryThread();
        this.world.setSpawnLocation(x, y, z, yaw);
        return this;
    }

    @NotNull
    public WorldEditor borderSize(@Range(from = 1, to = 59999968) final double size) {
        BukkitUtil.checkPrimaryThread();
        this.world.getWorldBorder().setSize(size);
        return this;
    }

    @NotNull
    public WorldEditor borderCenter(final double x, final double z) {
        BukkitUtil.checkPrimaryThread();
        this.world.getWorldBorder().setCenter(x, z);
        return this;
    }

    @NotNull
    public WorldEditor borderDamageAmount(@Range(from = 0, to = Long.MAX_VALUE) final double amount) {
        BukkitUtil.checkPrimaryThread();
        this.world.getWorldBorder().setDamageAmount(amount);
        return this;
    }

    @NotNull
    public WorldEditor borderDamageBuffer(@Range(from = 0, to = Long.MAX_VALUE) final double buffer) {
        BukkitUtil.checkPrimaryThread();
        this.world.getWorldBorder().setDamageBuffer(buffer);
        return this;
    }

    @NotNull
    public WorldEditor borderWarningDistance(@Range(from = 0, to = Integer.MAX_VALUE) final int distance) {
        BukkitUtil.checkPrimaryThread();
        this.world.getWorldBorder().setWarningDistance(distance);
        return this;
    }

    @NotNull
    public WorldEditor borderWarningTime(@Range(from = 0, to = Integer.MAX_VALUE) final int seconds) {
        BukkitUtil.checkPrimaryThread();
        this.world.getWorldBorder().setWarningTime(seconds);
        return this;
    }

    @NotNull
    public <T> WorldEditor gameRule(@NotNull final GameRule<T> rule, @NotNull final T value) {
        BukkitUtil.checkPrimaryThread();
        this.world.setGameRule(Objects.requireNonNull(rule), Objects.requireNonNull(value));
        return this;
    }

    @NotNull
    public WorldEditor customize(@NotNull final Consumer<World> customizer) {
        BukkitUtil.checkPrimaryThread();
        Objects.requireNonNull(customizer, "customizer").accept(this.world);
        return this;
    }

    @NotNull
    public World apply() {
        return this.world;
    }

    @NotNull
    public World getWorld() {
        return this.world;
    }
}
