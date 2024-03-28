package com.dev7ex.multiworld.api.bukkit.world.generator;

import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@NoArgsConstructor
public class FlatWorldGenerator extends BukkitWorldGenerator {

    public FlatWorldGenerator(@NotNull final Plugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull ChunkGenerator.ChunkData generateChunkData(@NotNull final World world, @NotNull final Random random, final int x, final int z, @NotNull final ChunkGenerator.BiomeGrid biome) {
        final ChunkGenerator.ChunkData chunkData = super.createChunkData(world);
        chunkData.setRegion(0, 0, 0, 16, 1, 16, Material.BEDROCK);
        chunkData.setRegion(0, 1, 0, 16, 64, 16, Material.GRASS_BLOCK);

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
