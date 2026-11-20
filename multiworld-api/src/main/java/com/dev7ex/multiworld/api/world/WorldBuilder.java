
package com.dev7ex.multiworld.api.world;

import com.dev7ex.multiworld.api.util.BukkitUtil;
import net.kyori.adventure.util.TriState;
import org.bukkit.*;
import org.bukkit.WorldType;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Dev7ex
 * @since 28.10.2026
 */
public final class WorldBuilder extends WorldCreator {

    public WorldBuilder(@NotNull final String name) {
        super(Objects.requireNonNull(name, "name"));

        if (name.isBlank()) {
            throw new IllegalArgumentException("World name must not be blank");
        }
    }

    public WorldBuilder(@NotNull final String levelName, @NotNull final NamespacedKey worldKey) {
        super(levelName, worldKey);
    }

    public WorldBuilder(@NotNull final NamespacedKey worldKey) {
        super(worldKey);
    }

    @NotNull
    public static WorldBuilder of(@NotNull final String name) {
        return new WorldBuilder(name);
    }

    @Override
    @NotNull
    public WorldBuilder environment(@NotNull final World.Environment environment) {
        super.environment(Objects.requireNonNull(environment, "environment"));
        return this;
    }

    @NotNull
    public WorldBuilder environment(@NotNull final WorldEnvironment environment) {
        super.environment(Objects.requireNonNull(environment.asBukkit(), "environment"));
        return this;
    }

    @Override
    @NotNull
    public WorldBuilder type(@NotNull final WorldType type) {
        super.type(Objects.requireNonNull(type, "type"));
        return this;
    }

    @NotNull
    public WorldBuilder type(@NotNull final com.dev7ex.multiworld.api.world.WorldType type) {
        super.type(Objects.requireNonNull(type.asBukkit(), "type"));
        return this;
    }

    @NotNull
    public WorldBuilder hardcore(final boolean hardcore) {
        super.hardcore(hardcore);
        return this;
    }

    @Override
    @NotNull
    public WorldBuilder seed(@Range(from = Long.MIN_VALUE, to = Long.MAX_VALUE) final long seed) {
        super.seed(seed);
        return this;
    }

    @NotNull
    public WorldBuilder randomSeed() {
        super.seed(ThreadLocalRandom.current().nextLong());
        return this;
    }

    @Override
    @NotNull
    public WorldBuilder generateStructures(final boolean enabled) {
        super.generateStructures(enabled);
        return this;
    }

    @Override
    @NotNull
    public WorldBuilder generator(@Nullable final ChunkGenerator generator) {
        super.generator(generator);
        return this;
    }

    @Override
    @NotNull
    public WorldBuilder generator(@Nullable final String generator) {
        super.generator(generator);
        return this;
    }

    @Override
    @NotNull
    public WorldBuilder biomeProvider(@Nullable final BiomeProvider provider) {
        super.biomeProvider(provider);
        return this;
    }

    @Override
    @NotNull
    public WorldBuilder biomeProvider(@Nullable final String provider) {
        super.biomeProvider(provider);
        return this;
    }

    @NotNull
    public WorldBuilder generatorSettings(@NotNull final String generatorSettings) {
        super.generatorSettings(generatorSettings);
        return this;
    }

    @NotNull
    public WorldBuilder keepSpawnLoaded(@NotNull final TriState keepSpawnLoaded) {
        super.keepSpawnLoaded(keepSpawnLoaded);
        return this;
    }

    @NotNull
    public World build() {
        BukkitUtil.checkPrimaryThread();
        World world = Bukkit.createWorld(this);

        if (world == null) {
            throw new IllegalStateException("Failed to create or load world: " + name());
        }
        return world;
    }

    @Override
    public World createWorld() {
        return this.build();
    }

}
