package com.dev7ex.multiworld;

import com.dev7ex.common.bukkit.plugin.BukkitPlugin;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.MultiWorldProvider;
import com.dev7ex.multiworld.command.WorldCommand;
import com.dev7ex.multiworld.listener.*;
import com.dev7ex.multiworld.world.WorldConfiguration;
import com.dev7ex.multiworld.user.WorldUserService;
import com.dev7ex.multiworld.world.WorldManager;
import com.dev7ex.multiworld.world.WorldService;

import lombok.AccessLevel;
import lombok.Getter;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

/**
 * @author Dev7ex
 * @since 19.05.2021
 */
@Getter(AccessLevel.PUBLIC)
public final class MultiWorldPlugin extends BukkitPlugin implements MultiWorldApi {

    private static final int SERVICE_ID = 15446;
    private static final int RESOURCE_ID = 92559;

    private boolean updateAvailable = false;

    private MultiWorldConfiguration configuration;
    private WorldConfiguration worldConfiguration;
    private WorldService worldService;
    private WorldManager worldManager;
    private WorldUserService worldUserService;

    @Override
    public void onLoad() {
        this.configuration = new MultiWorldConfiguration(this);
        super.onLoad();
        this.worldConfiguration = new WorldConfiguration(this);
    }

    @Override
    public void onEnable() {
        MultiWorldProvider.registerApi(this);

        this.checkUpdates();

        final Metrics metrics = new Metrics(this, MultiWorldPlugin.SERVICE_ID);

        super.registerService(this.worldUserService = new WorldUserService());
        this.worldManager = new WorldManager(this.configuration, this.worldConfiguration, this.worldUserService);
        super.registerService(this.worldService = new WorldService(this.worldManager, this.worldConfiguration));
        super.onEnable();
    }

    @Override
    public void onDisable() {
        MultiWorldProvider.unregisterApi();

        super.onDisable();
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

    public void checkUpdates() {
        super.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try (final InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + MultiWorldPlugin.RESOURCE_ID).openStream()) {
                try (final Scanner scanner = new Scanner(inputStream)) {
                    final String newVersion = scanner.next();

                    if (scanner.hasNext()) {
                        if (!this.getDescription().getVersion().equalsIgnoreCase(newVersion)) {
                            this.updateAvailable = true;
                            super.getServer().getScheduler().runTask(this, () -> {
                                super.getLogger().info("There is a new update available.");
                            });

                        }
                    }
                }
            } catch (final IOException exception) {
                super.getServer().getScheduler().runTask(this, () -> {
                    super.getLogger().info("Unable to check for updates: " + exception.getMessage());
                });
            }
        });
    }

    public static MultiWorldPlugin getInstance() {
        return JavaPlugin.getPlugin(MultiWorldPlugin.class);
    }

}
