package com.dev7ex.multiworld;

import com.dev7ex.common.bukkit.plugin.BukkitPlugin;

import com.dev7ex.multiworld.command.WorldCommand;
import com.dev7ex.multiworld.listener.*;
import com.dev7ex.multiworld.world.WorldConfiguration;
import com.dev7ex.multiworld.user.WorldUserService;
import com.dev7ex.multiworld.world.WorldManager;
import com.dev7ex.multiworld.world.WorldService;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Dev7ex
 * @since 19.05.2021
 */
@Getter(AccessLevel.PUBLIC)
public final class MultiWorldPlugin extends BukkitPlugin {

    private static final int SERVICE_ID = 15446;

    private MultiWorldConfiguration configuration;
    private WorldConfiguration worldConfiguration;
    private WorldService worldService;
    private WorldManager worldManager;
    private WorldUserService worldUserService;

    @Override
    public void onLoad() {
        this.configuration = new MultiWorldConfiguration(this);
        super.onLoad();
        this.worldConfiguration = new WorldConfiguration(this, "worlds.yml");
    }

    @Override
    public void onEnable() {
        super.registerService(this.worldUserService = new WorldUserService());
        this.worldManager = new WorldManager(this.configuration, this.worldConfiguration, this.worldUserService);
        super.registerService(this.worldService = new WorldService(this.worldManager, this.worldConfiguration));
        super.onEnable();
    }

    @Override
    public void registerCommands() {
        super.registerCommand("world").setExecutor(new WorldCommand(this));
    }

    @Override
    public void registerListeners() {
        super.registerListener(new EntityDamageByEntityListener(this));
        super.registerListenerIf(new PlayerChangeWorldListener(this),
                enable -> this.configuration.getBooleanSafe("settings.auto-gamemode"));
        super.registerListener(new PlayerConnectionListener(this));
    }

    public static MultiWorldPlugin getInstance() {
        return JavaPlugin.getPlugin(MultiWorldPlugin.class);
    }

}
