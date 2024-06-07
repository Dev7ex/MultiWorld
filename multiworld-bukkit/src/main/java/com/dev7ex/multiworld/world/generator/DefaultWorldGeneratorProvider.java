package com.dev7ex.multiworld.world.generator;

import com.dev7ex.common.bukkit.plugin.module.PluginModule;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.generator.BukkitWorldGeneratorHolder;
import com.dev7ex.multiworld.api.bukkit.world.generator.BukkitWorldGeneratorProvider;
import com.dev7ex.multiworld.api.world.generator.WorldGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of a world generator provider for managing custom world generators.
 * This module scans all enabled plugins to find and register their custom world generators.
 * It implements PluginModule and BukkitWorldGeneratorProvider.
 *
 * @author Dev7ex
 * @since 29.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public class DefaultWorldGeneratorProvider implements PluginModule, BukkitWorldGeneratorProvider {

    private final Map<BukkitWorldGeneratorHolder, String> customGenerators = new HashMap<>();
    private final Map<BukkitWorldGeneratorHolder, WorldGenerator> defaultGenerators = new HashMap<>();
    private final Map<BukkitWorldGeneratorHolder, WorldGenerator> fileGenerators = new HashMap<>();

    /**
     * Called when the module is enabled.
     * Scans all enabled plugins to find and register their custom world generators.
     */
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
            this.customGenerators.put(new BukkitWorldGeneratorHolder(plugin), plugin.getDescription().getName());
        }
        MultiWorldPlugin.getInstance().getLogger().info("Found: [" + this.customGenerators.values().size() + "] World Generator");
    }

    /**
     * Called when the module is disabled.
     * Clears the list of custom world generators.
     */
    @Override
    public void onDisable() {
        this.customGenerators.clear();
        this.defaultGenerators.clear();
        this.fileGenerators.clear();
    }

    /**
     * Checks if a custom world generator is registered.
     *
     * @param generator The name of the generator to check.
     * @return True if the generator is registered, false otherwise.
     */
    @Override
    public boolean isRegistered(final String generator) {
        return this.customGenerators.containsValue(generator);
    }

}
