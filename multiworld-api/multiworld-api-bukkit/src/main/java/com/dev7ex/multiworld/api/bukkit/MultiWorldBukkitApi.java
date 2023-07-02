package com.dev7ex.multiworld.api.bukkit;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldConfiguration;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldGeneratorProvider;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldManager;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldProvider;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface MultiWorldBukkitApi extends MultiWorldApi {

    @Override
    @NotNull
    BukkitWorldConfiguration getWorldConfiguration();

    @Override
    @NotNull
    BukkitWorldProvider getWorldProvider();

    @Override
    @NotNull
    BukkitWorldGeneratorProvider getWorldGeneratorProvider();

    @Override
    @NotNull
    BukkitWorldManager getWorldManager();
}
