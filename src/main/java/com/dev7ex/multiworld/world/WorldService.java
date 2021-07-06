package com.dev7ex.multiworld.world;

import com.dev7ex.common.bukkit.plugin.service.PluginService;

import com.dev7ex.multiworld.MultiWorldConfiguration;

import org.bukkit.Bukkit;
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
            if (this.worldConfiguration.isWorldRegistered(worlds.getName())) {
                continue;
            }
            this.worldManager.importWorld(Bukkit.getConsoleSender(), worlds.getName(), WorldType.getByEnvironment(worlds.getEnvironment()));
        }

        for (final String worlds : this.configuration.getAutoLoadableWorlds()) {
            if (!this.worldConfiguration.isWorldRegistered(worlds)) {
                Bukkit.getConsoleSender().sendMessage(this.configuration.getWorldMessage("loading.not-registered").replaceAll("%world%", worlds));
                continue;
            }
            this.worldManager.loadWorld(Bukkit.getConsoleSender(), worlds);
        }

        for (final String worlds : this.worldConfiguration.getWorlds()) {
            final WorldProperties worldProperties = this.worldConfiguration.getWorldProperties(worlds);
            if (Bukkit.getWorld(worlds) != null) {
                final World currentWorld = Bukkit.getWorld(worlds);
                currentWorld.setDifficulty(this.worldConfiguration.getWorldProperties(worlds).getDifficulty());
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
