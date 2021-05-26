package com.dev7ex.multiworld;

import com.dev7ex.common.bukkit.plugin.BukkitPlugin;

import com.dev7ex.multiworld.command.BackCommand;
import com.dev7ex.multiworld.command.WorldCommand;
import com.dev7ex.multiworld.generator.VoidChunkGenerator;
import com.dev7ex.multiworld.listener.*;
import com.dev7ex.multiworld.world.WorldConfiguration;
import com.dev7ex.multiworld.user.WorldUserService;
import com.dev7ex.multiworld.world.WorldManager;
import com.dev7ex.multiworld.world.WorldService;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Dev7ex
 * @since 19.05.2021
 */

@Getter
public final class MultiWorldPlugin extends BukkitPlugin {

    private MultiWorldConfiguration configuration;
    private WorldConfiguration worldConfiguration;
    private WorldService worldService;
    private WorldManager worldManager;
    private WorldUserService worldUserService;

    @Override
    public final void onLoad() {
        this.configuration = new MultiWorldConfiguration(this);
        super.onLoad();
        this.configuration.load();
        this.worldConfiguration = new WorldConfiguration(this, "worlds.yml");
    }

    @Override
    public final void onEnable() {
        super.registerService(this.worldUserService = new WorldUserService());
        this.worldManager = new WorldManager(this.configuration, this.worldConfiguration, this.worldUserService);
        super.registerService(this.worldService = new WorldService(this.worldManager, this.worldConfiguration));
        super.onEnable();
    }

    @Override
    public final void registerCommands() {
        super.registerCommand("back").setExecutor(new BackCommand(this));
        super.registerCommand("world").setExecutor(new WorldCommand(this));
    }

    @Override
    public final void registerListeners() {
        super.registerListener(new EntityDamageByEntityListener(this));
        super.registerListener(new PlayerChangeWorldListener(this));
        super.registerListener(new PlayerLoginListener(this));
        super.registerListener(new PlayerQuitListener(this));
    }

    public static MultiWorldPlugin getInstance() {
        return JavaPlugin.getPlugin(MultiWorldPlugin.class);
    }

}
