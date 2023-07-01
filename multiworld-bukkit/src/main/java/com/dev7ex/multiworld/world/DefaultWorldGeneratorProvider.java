package com.dev7ex.multiworld.world;

import com.dev7ex.common.bukkit.plugin.service.PluginService;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldGeneratorProvider;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dev7ex
 * @since 29.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public class DefaultWorldGeneratorProvider implements PluginService, BukkitWorldGeneratorProvider {

    private final Map<Plugin, String> customGenerators = new HashMap<>();

    @Override
    public void onEnable() {
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
            this.customGenerators.put(plugin, plugin.getDescription().getName());
        }
        MultiWorldPlugin.getInstance().getLogger().info("Found: [" + this.customGenerators.values().size() + "] World Generator");
    }

    @Override
    public void onDisable() {
        this.customGenerators.clear();
    }

    @Override
    public boolean exists(@NotNull final String generator) {
        return this.customGenerators.containsValue(generator);
    }

}
