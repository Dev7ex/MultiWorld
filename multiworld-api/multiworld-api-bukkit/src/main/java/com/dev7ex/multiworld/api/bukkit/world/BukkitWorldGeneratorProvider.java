package com.dev7ex.multiworld.api.bukkit.world;

import com.dev7ex.multiworld.api.world.WorldGeneratorProvider;
import org.bukkit.plugin.Plugin;

import java.util.Map;

/**
 * @author Dev7ex
 * @since 29.06.2023
 */
public interface BukkitWorldGeneratorProvider extends WorldGeneratorProvider {

    @Override
    Map<Plugin, String> getCustomGenerators();

}
