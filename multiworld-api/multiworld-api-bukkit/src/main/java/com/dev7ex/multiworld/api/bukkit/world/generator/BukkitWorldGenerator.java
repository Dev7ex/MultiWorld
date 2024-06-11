package com.dev7ex.multiworld.api.bukkit.world.generator;

import com.dev7ex.multiworld.api.world.generator.WorldGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Bukkit implementation of a custom world generator.
 * Extends ChunkGenerator and implements WorldGenerator.
 * This class can be used to generate custom terrain and structures for Bukkit worlds.
 *
 * @author Dev7ex
 * @since 26.03.2024
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class BukkitWorldGenerator extends ChunkGenerator implements WorldGenerator {

    private Plugin plugin;

    /**
     * Constructs a new BukkitWorldGenerator with the given plugin.
     *
     * @param plugin The Bukkit plugin associated with this world generator.
     */
    public BukkitWorldGenerator(@NotNull final Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Default constructor for BukkitWorldGenerator.
     * This constructor is needed for Bukkit to properly instantiate the generator.
     */
    public BukkitWorldGenerator() {
    }

}
