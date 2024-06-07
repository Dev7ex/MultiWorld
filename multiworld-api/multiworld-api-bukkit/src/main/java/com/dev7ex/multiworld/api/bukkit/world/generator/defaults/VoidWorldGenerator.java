package com.dev7ex.multiworld.api.bukkit.world.generator.defaults;

import com.dev7ex.multiworld.api.bukkit.world.generator.BukkitWorldGenerator;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Generates a void world with a single stone block at the spawn location.
 * This class extends BukkitWorldGenerator and overrides the generateChunkData method.
 * It is used to create void worlds in Bukkit.
 *
 * @author Dev7ex
 * @since 20.05.2021
 */
@NoArgsConstructor
public class VoidWorldGenerator extends BukkitWorldGenerator {

    /**
     * Constructs a new VoidWorldGenerator with the given plugin.
     *
     * @param plugin The Bukkit plugin associated with this generator.
     */
    public VoidWorldGenerator(@NotNull final Plugin plugin) {
        super(plugin);
    }

    /**
     * Generates the chunk data for a void world.
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

        // Set a stone block at the center of the world as the spawn location
        if ((x == 0) && (z == 0)) {
            chunkData.setBlock(0, 63, 0, Material.STONE);
            world.setSpawnLocation(new Location(world, 0, 64, 0));
        }

        // Set plains biome for all chunks
        for (int chunkX = 0; chunkX < 16; chunkX++) {
            for (int chunkZ = 0; chunkZ < 16; chunkZ++) {
                if (biome.getBiome(chunkX, chunkZ) != Biome.PLAINS) {
                    biome.setBiome(chunkX, chunkZ, Biome.PLAINS);
                }
            }
        }
        return chunkData;
    }

}
