package com.dev7ex.multiworld.world;

import com.dev7ex.common.bukkit.plugin.service.PluginService;

import com.dev7ex.multiworld.MultiWorldConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */

public final class WorldService implements PluginService {

    private final WorldConfiguration worldConfiguration;
    private final WorldManager worldManager;
    private final MultiWorldConfiguration configuration;

    public WorldService(final WorldManager worldManager, final WorldConfiguration worldConfiguration) {
        this.worldManager = worldManager;
        this.worldConfiguration = worldConfiguration;
        this.configuration = worldManager.getConfiguration();
    }

    @Override
    public final void onEnable() {
        for (final World worlds : Bukkit.getWorlds()) {
            System.out.println(worlds.getName());
            if (this.worldConfiguration.isWorldRegistered(worlds.getName())) {
                continue;
            }
            final WorldProperties worldProperties = new WorldProperties(worlds.getName(), "CONSOLE",
                    System.currentTimeMillis(), System.currentTimeMillis(), WorldType.getByEnvironment(worlds.getEnvironment()),
                    Difficulty.valueOf(this.configuration.getValues().getString("defaults.difficulty")),
                    GameMode.valueOf(this.configuration.getValues().getString("defaults.gameMode")),
                    this.configuration.getValues().getBoolean("defaults.pvp-enabled"));

            Bukkit.getLogger().info("Register World: " + worlds.getName());
            this.worldConfiguration.registerWorld(worlds.getName(), worldProperties);
        }

        for (final String worlds : this.worldConfiguration.getWorlds()) {
            final WorldProperties worldProperties = this.worldConfiguration.getWorldProperties(worlds);
            if (Bukkit.getWorld(worlds) != null) {
                worldProperties.setLoaded(true);
            }
            this.worldManager.getWorldProperties().put(worlds, worldProperties);
        }
    }

    @Override
    public final void onDisable() {
        this.worldManager.getWorldProperties().clear();
    }

}
