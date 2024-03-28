package com.dev7ex.multiworld.api.bukkit.world.generator;

import com.dev7ex.multiworld.api.world.generator.WorldGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 26.03.2024
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class BukkitWorldGenerator extends ChunkGenerator implements WorldGenerator {

    private Plugin plugin;

    public BukkitWorldGenerator(@NotNull final Plugin plugin) {
        this.plugin = plugin;
    }

    public BukkitWorldGenerator() {}

}
