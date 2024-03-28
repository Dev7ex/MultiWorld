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
 * @since 20.05.2021
 */
@NoArgsConstructor
public class VoidWorldGenerator extends BukkitWorldGenerator {

    public VoidWorldGenerator(@NotNull final Plugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull ChunkGenerator.ChunkData generateChunkData(@NotNull final World world, @NotNull final Random random, final int x, final int z, @NotNull final ChunkGenerator.BiomeGrid biome) {
        final ChunkGenerator.ChunkData chunkData = super.createChunkData(world);

        if ((x == 0) && (z == 0)) {
            chunkData.setBlock(0, 63, 0, Material.STONE);
            world.setSpawnLocation(new Location(world, 0, 64, 0));
        }
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
