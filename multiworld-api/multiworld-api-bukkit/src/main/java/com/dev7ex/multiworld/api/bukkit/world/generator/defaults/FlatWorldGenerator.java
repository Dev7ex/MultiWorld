package com.dev7ex.multiworld.api.bukkit.world.generator.defaults;

import com.dev7ex.multiworld.api.bukkit.world.generator.BukkitWorldGenerator;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Generates a flat world with bedrock at the bottom and grass blocks on top.
 * This class extends BukkitWorldGenerator and overrides the generateChunkData method.
 * It is used to create flat worlds in Bukkit.
 *
 * @author Dev7ex
 * @since 20.05.2021
 */
@NoArgsConstructor
public class FlatWorldGenerator extends BukkitWorldGenerator {

    /**
     * Constructs a new FlatWorldGenerator with the given plugin.
     *
     * @param plugin The Bukkit plugin associated with this generator.
     */
    public FlatWorldGenerator(@NotNull final Plugin plugin) {
        super(plugin);
    }

    /**
     * Generates the chunk data for a flat world.
     *
     * @param world  The Bukkit world.
     * @param random The random number generator.
     * @param x      The X-coordinate of the chunk.
     * @param z      The Z-coordinate of the chunk.
     * @param biome  The biome grid.
     * @return The generated chunk data.
     */
    @Override
    public @NotNull ChunkGenerator.ChunkData generateChunkData(@NotNull final World world, @NotNull final Random random, final int x, final int z, @NotNull final ChunkGenerator.BiomeGrid biome) {
        final ChunkGenerator.ChunkData chunkData = super.createChunkData(world);

        // Set bedrock at the bottom
        chunkData.setRegion(0, 0, 0, 16, 1, 16, Material.BEDROCK);

        // Set grass blocks on top
        chunkData.setRegion(0, 1, 0, 16, 64, 16, Material.GRASS_BLOCK);

        // Set plains biome for all chunks
        for (int chunkX = 0; chunkX < 15; chunkX++) {
            for (int chunkZ = 0; chunkZ < 15; chunkZ++) {
                if (biome.getBiome(chunkX, chunkZ) != Biome.PLAINS) {
                    biome.setBiome(chunkX, chunkZ, Biome.PLAINS);
                }
            }
        }
        return chunkData;
    }

}
