package com.dev7ex.multiworld.world;

import com.dev7ex.common.bukkit.plugin.service.PluginService;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.event.plugin.MultiWorldStartupCompleteEvent;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldProvider;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldType;
import com.dev7ex.multiworld.api.world.WorldProperty;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public class DefaultWorldProvider implements PluginService, BukkitWorldProvider {

    private final Map<String, BukkitWorldHolder> worldHolders = new HashMap<>();
    private final DefaultWorldManager worldManager;
    private final DefaultWorldConfiguration configuration;

    public DefaultWorldProvider(@NotNull final DefaultWorldManager worldManager, @NotNull final DefaultWorldConfiguration configuration) {
        this.worldManager = worldManager;
        this.configuration = configuration;
    }

    @Override
    public void onEnable() {
        final long startTime = System.currentTimeMillis();

        // Use this to import the standard worlds
        for (final World worlds : Bukkit.getWorlds()) {
            if (this.configuration.contains(worlds.getName())) {
                continue;
            }
            this.worldManager.importWorld(Bukkit.getConsoleSender().getName(), worlds.getName(), BukkitWorldType.fromEnvironment(worlds.getEnvironment()));
        }

        for (final String worldEntry : this.configuration.getWorldHolders().keySet()) {
            final BukkitWorldHolder worldHolder = this.configuration.getWorldHolder(worldEntry);

            /*
              Check if the worldHolder entry has missing values from new updates
             */
            for (final WorldProperty property : WorldProperty.values()) {
                if (!this.configuration.hasProperty(worldEntry, property)) {
                    this.configuration.addMissingProperty(worldHolder, property);
                }
            }

            this.configuration.removeUnusableProperties(worldEntry);

            if (Bukkit.getWorld(worldEntry) != null) {
                final World world = worldHolder.getWorld();
                world.setDifficulty(worldHolder.getDifficulty());
                world.setSpawnFlags(worldHolder.isSpawnMonsters(), worldHolder.isSpawnMonsters());
                worldHolder.setLoaded(true);
            }
            this.register(worldHolder);
        }

        for (final String worldName : MultiWorldPlugin.getInstance().getConfiguration().getAutoLoadableWorlds()) {
            if (!this.configuration.contains(worldName)) {
                Bukkit.getConsoleSender().sendMessage("%prefix% §7Couldnt load the world §a%world%§7. Use /world import");
                continue;
            }
            this.worldManager.loadWorld(Bukkit.getConsoleSender().getName(), worldName);
        }

        MultiWorldPlugin.getInstance().getLogger().info("Found: [" + this.worldHolders.size() + "] Worlds");
        Bukkit.getPluginManager().callEvent(new MultiWorldStartupCompleteEvent(MultiWorldPlugin.getInstance(), (System.currentTimeMillis() - startTime)));
    }

    @Override
    public void onDisable() {
        this.worldHolders.clear();
    }

    @Override
    public void register(@NotNull final BukkitWorldHolder worldHolder) {
        this.worldHolders.put(worldHolder.getName(), worldHolder);
    }

    @Override
    public void unregister(@NotNull final String name) {
        this.worldHolders.remove(name);
    }

    @Override
    public boolean isRegistered(@NotNull final String name) {
        return this.worldHolders.containsKey(name);
    }

    @Override
    public Optional<BukkitWorldHolder> getWorldHolder(@NotNull final String name) {
        return Optional.ofNullable(this.worldHolders.get(name));
    }

}
