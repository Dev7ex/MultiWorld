package com.dev7ex.multiworld.api.bukkit.generator;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
public final class FlatChunkGenerator extends ChunkGenerator {

    @Override
    public @NotNull ChunkData generateChunkData(@NotNull final World world, @NotNull final Random random, final int x, final int z, @NotNull final BiomeGrid biome) {
        final ChunkData chunkData = super.createChunkData(world);
        chunkData.setRegion(0, 0, 0, 16, 1, 16, Material.BEDROCK);
        chunkData.setRegion(0, 1, 0, 16, 64, 16, Material.GRASS_BLOCK);

        for (int chunkX = 0; chunkX < 16; chunkX++) {
            for (int chunkZ = 0; chunkZ < 16; chunkZ++) {
                biome.setBiome(x, z, Biome.PLAINS);
            }
        }
        return chunkData;
    }

}
