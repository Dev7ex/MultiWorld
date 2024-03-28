package com.dev7ex.multiworld.api.bukkit.world.generator;

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
 * @author Dev7ex
 * @since 26.03.2024
 */
@NoArgsConstructor
public class WaterWorldGenerator extends BukkitWorldGenerator {

    public WaterWorldGenerator(@NotNull final Plugin plugin) {
        super(plugin);
    }

    @NotNull
    @Override
    public ChunkData generateChunkData(@NotNull final World world, @NotNull final Random random, final int x, final int z, @NotNull final ChunkGenerator.BiomeGrid biome) {
        final ChunkGenerator.ChunkData chunkData = super.createChunkData(world);
        chunkData.setRegion(0, 0, 0, 16, 1, 16, Material.BEDROCK);
        chunkData.setRegion(0, 1, 0, 16, 64, 16, Material.WATER);

        if ((x == 0) && (z == 0)) {
            chunkData.setBlock(0, 63, 0, Material.STONE);
            world.setSpawnLocation(new Location(world, 0, 64, 0));
        }

        for (int chunkX = 0; chunkX < 15; chunkX++) {
            for (int chunkZ = 0; chunkZ < 15; chunkZ++) {
                if (biome.getBiome(chunkX, chunkZ) != Biome.OCEAN) {
                    biome.setBiome(chunkX, chunkZ, Biome.OCEAN);
                }

            }
        }
        return chunkData;
    }
}
