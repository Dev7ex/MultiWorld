package com.dev7ex.multiworld.world;

import com.dev7ex.common.bukkit.plugin.service.PluginService;

import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 *
 * @author Dev7ex
 * @since 20.05.2021
 *
 */

public final class WorldService implements PluginService {

    private final WorldConfiguration worldConfiguration;
    private final WorldManager worldManager;

    public WorldService(final WorldManager worldManager, final WorldConfiguration worldConfiguration) {
        this.worldManager = worldManager;
        this.worldConfiguration = worldConfiguration;
    }

    @Override
    public final void onEnable() {
        for(final World worlds : Bukkit.getWorlds()) {
            if(this.worldConfiguration.isWorldRegistered(worlds.getName())) {
               continue;
            }
            this.worldConfiguration.registerWorld(worlds.getName(), new WorldProperties(worlds.getName(), "Server", System.currentTimeMillis(), 0L));
            Bukkit.getLogger().info("Register World: " + worlds.getName());
        }
        for(final String worlds : this.worldConfiguration.getWorlds()) {
            final WorldProperties worldProperties = this.worldConfiguration.getWorldProperties(worlds);
            if(Bukkit.getWorld(worlds) != null) {
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
