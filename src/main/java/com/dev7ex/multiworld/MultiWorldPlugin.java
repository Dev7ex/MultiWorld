package com.dev7ex.multiworld;

import com.dev7ex.common.bukkit.plugin.BukkitPlugin;

import com.dev7ex.common.bukkit.plugin.PluginProperties;
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

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
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
@PluginProperties(resourceId = 92559, metricsId = 15446, metrics = true)
public final class MultiWorldPlugin extends BukkitPlugin implements MultiWorldApi {

    private boolean updateAvailable = false;

    private MultiWorldConfiguration configuration;
    private WorldConfiguration worldConfiguration;
    private WorldService worldService;
    private WorldManager worldManager;
    private WorldUserService worldUserService;

    @Override
    public void onLoad() {
        this.configuration = new MultiWorldConfiguration(this);
        this.configuration.load();

        this.worldConfiguration = new WorldConfiguration(this);
    }

    @Override
    public void onEnable() {
        MultiWorldProvider.registerApi(this);

        final Plugin facilisCommonPlugin = super.getServer().getPluginManager().getPlugin("FacilisCommon");

        if (!facilisCommonPlugin.getDescription().getVersion().equalsIgnoreCase("1.0.0-b002-SNAPSHOT")) {
            super.getLogger().severe("MultiWorld need FacilisCommon 1.0.0-b002-SNAPSHOT");
            super.getLogger().severe("https://www.spigotmc.org/resources/faciliscommon.107198/");
            super.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.checkUpdates();

        super.registerService(this.worldUserService = new WorldUserService());
        this.worldManager = new WorldManager(this.configuration, this.worldConfiguration, this.worldUserService);
        super.registerService(this.worldService = new WorldService(this.worldManager, this.worldConfiguration));
    }

    @Override
    public void onDisable() {
        MultiWorldProvider.unregisterApi();

        super.onDisable();
    }

    @Override
    public void registerCommands() {
        super.registerCommand(new WorldCommand(this));
    }

    @Override
    public void registerListeners() {
        super.registerListener(new EntityDamageByEntityListener(this));
        super.registerListenerIf(new PlayerChangeWorldListener(this),
                enable -> this.configuration.getBoolean("settings.auto-gamemode"));
        super.registerListener(new PlayerConnectionListener(this));
    }

    public void checkUpdates() {
        super.getServer().getScheduler().runTaskAsynchronously(this, () -> {

            try (final InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + super.getResourceId()).openStream()) {
                try (final Scanner scanner = new Scanner(inputStream)) {

                    if (scanner.hasNext()) {
                        final String currentVersion = this.getDescription().getVersion();
                        final String newVersion = scanner.next();
                        if (!currentVersion.equalsIgnoreCase(newVersion)) {
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
