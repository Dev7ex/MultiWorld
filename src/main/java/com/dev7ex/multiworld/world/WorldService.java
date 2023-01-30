package com.dev7ex.multiworld.world;

import com.dev7ex.common.bukkit.plugin.service.PluginService;

import com.dev7ex.multiworld.MultiWorldConfiguration;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.event.plugin.MultiWorldStartupCompleteEvent;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@Getter(AccessLevel.PUBLIC)
public final class WorldService implements PluginService {

    private final WorldConfiguration worldConfiguration;
    private final WorldManager worldManager;
    private final MultiWorldConfiguration configuration;
    private final Map<Plugin, String> worldGenerators = new HashMap<>();

    public WorldService(final WorldManager worldManager, final WorldConfiguration worldConfiguration) {
        this.worldManager = worldManager;
        this.worldConfiguration = worldConfiguration;
        this.configuration = worldManager.getConfiguration();
    }

    @Override
    public void onEnable() {
        final long startTime = System.currentTimeMillis();

        for (final World worlds : Bukkit.getWorlds()) {
            if (this.worldConfiguration.isWorldRegistered(worlds.getName())) {
                continue;
            }
            this.worldManager.importWorld(Bukkit.getConsoleSender(), worlds.getName(), WorldType.getByEnvironment(worlds.getEnvironment()));
        }

        for (final String worlds : this.configuration.getAutoLoadableWorlds()) {
            if (!this.worldConfiguration.isWorldRegistered(worlds)) {
                Bukkit.getConsoleSender().sendMessage(this.configuration.getMessage("loading.not-registered").replaceAll("%world%", worlds));
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

        for (final Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (!plugin.isEnabled()) {
                continue;
            }

            try {
                if (plugin.getDefaultWorldGenerator("world", "") == null) {
                    continue;
                }
            } catch (final Exception exception) {
                MultiWorldPlugin.getInstance().getLogger().severe(plugin.getName() + " could not be loaded");
                continue;
            }
            this.worldGenerators.put(plugin, plugin.getDescription().getName());
        }
        MultiWorldPlugin.getInstance().getLogger().info("Found: [" + this.worldGenerators.values().size() + "] World Generator");
        Bukkit.getPluginManager().callEvent(new MultiWorldStartupCompleteEvent(System.currentTimeMillis() - startTime));
    }

    @Override
    public void onDisable() {
        this.worldManager.getWorldProperties().clear();
    }

}
