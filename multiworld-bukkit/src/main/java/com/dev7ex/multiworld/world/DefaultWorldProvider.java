package com.dev7ex.multiworld.world;

import com.dev7ex.common.bukkit.plugin.module.PluginModule;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.event.plugin.MultiWorldStartupCompleteEvent;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldProvider;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldType;
import com.dev7ex.multiworld.api.world.WorldEnvironment;
import com.dev7ex.multiworld.api.world.WorldType;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of the BukkitWorldProvider interface.
 * Provides management and access to BukkitWorldHolder instances.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public class DefaultWorldProvider implements PluginModule, BukkitWorldProvider {

    // Map to store world holders by name
    private final Map<String, BukkitWorldHolder> worldHolders = new HashMap<>();

    private final DefaultWorldManager worldManager;
    private final DefaultWorldConfiguration configuration;

    /**
     * Constructs a DefaultWorldProvider with the specified parameters.
     *
     * @param worldManager  The world manager to use.
     * @param configuration The world configuration to use.
     */
    public DefaultWorldProvider(@NotNull final DefaultWorldManager worldManager, @NotNull final DefaultWorldConfiguration configuration) {
        this.worldManager = worldManager;
        this.configuration = configuration;
    }

    /**
     * Called when the plugin is enabled.
     * Loads and registers worlds, and triggers the startup complete event.
     */
    @Override
    public void onEnable() {
        final long startTime = System.currentTimeMillis();

        // Import standard worlds
        for (final World world : Bukkit.getWorlds()) {
            if (this.configuration.contains(world.getName())) {
                continue;
            }
            final WorldType worldType = BukkitWorldType.fromEnvironment(world.getEnvironment());
            this.worldManager.importWorld(Bukkit.getConsoleSender().getName(), world.getName(), WorldEnvironment.fromType(worldType), worldType);
        }

        // Iterate through world entries in configuration
        for (final String worldEntry : this.configuration.getWorldHolders().keySet()) {
            final BukkitWorldHolder worldHolder = this.configuration.getWorldHolder(worldEntry);

            // Check and add missing properties
            /*for (final WorldProperty property : WorldProperty.values()) {
                if (!this.configuration.hasProperty(worldEntry, property)) {
                    this.configuration.addMissingProperty(worldHolder, property);
                }
            }

            this.configuration.removeUnusableProperties(worldEntry);*/

            // Load world if it exists
            if (Bukkit.getWorld(worldEntry) != null) {
                final World world = worldHolder.getWorld();
                world.setDifficulty(worldHolder.getDifficulty());
                world.setSpawnFlags(worldHolder.isSpawnMonsters(), worldHolder.isSpawnMonsters());
                worldHolder.setLoaded(true);
            }
            this.register(worldHolder);
        }

        // Auto-load worlds
        for (final BukkitWorldHolder worldHolder : MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders().values()) {
            if (!worldHolder.isAutoLoadEnabled()) {
                continue;
            }

            if (worldHolder.getWorld() != null) {
                continue;
            }
            this.worldManager.loadWorld(Bukkit.getConsoleSender().getName(), worldHolder.getName());
        }
        MultiWorldPlugin.getInstance().getLogger().info("Found: [" + this.worldHolders.size() + "] Worlds");
        Bukkit.getPluginManager().callEvent(new MultiWorldStartupCompleteEvent(MultiWorldPlugin.getInstance(), (System.currentTimeMillis() - startTime)));
    }

    /**
     * Called when the plugin is disabled.
     * Clears the map of world holders.
     */
    @Override
    public void onDisable() {
        this.worldHolders.clear();
    }

    /**
     * Registers a BukkitWorldHolder.
     *
     * @param worldHolder The BukkitWorldHolder to register.
     */
    @Override
    public void register(@NotNull final BukkitWorldHolder worldHolder) {
        this.worldHolders.put(worldHolder.getName(), worldHolder);
    }

    /**
     * Unregisters a BukkitWorldHolder by name.
     *
     * @param name The name of the BukkitWorldHolder to unregister.
     */
    @Override
    public void unregister(@NotNull final String name) {
        this.worldHolders.remove(name);
    }

    /**
     * Checks if a BukkitWorldHolder is registered by name.
     *
     * @param name The name of the BukkitWorldHolder to check.
     * @return True if the BukkitWorldHolder is registered, false otherwise.
     */
    @Override
    public boolean isRegistered(@NotNull final String name) {
        return this.worldHolders.containsKey(name);
    }

    /**
     * Retrieves a BukkitWorldHolder by name.
     *
     * @param name The name of the BukkitWorldHolder to retrieve.
     * @return An Optional containing the BukkitWorldHolder, or an empty Optional if not found.
     */
    @Override
    public Optional<BukkitWorldHolder> getWorldHolder(@NotNull final String name) {
        return Optional.ofNullable(this.worldHolders.get(name));
    }

}
