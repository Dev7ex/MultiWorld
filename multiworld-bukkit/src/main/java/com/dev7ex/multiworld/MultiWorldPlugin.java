package com.dev7ex.multiworld;

import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.common.bukkit.plugin.ConfigurablePlugin;
import com.dev7ex.common.bukkit.plugin.PluginIdentification;
import com.dev7ex.common.bukkit.plugin.statistic.PluginStatisticProperties;
import com.dev7ex.multiworld.api.MultiWorldApiProvider;
import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.expansion.MultiWorldExpansion;
import com.dev7ex.multiworld.api.bukkit.world.location.BukkitWorldLocation;
import com.dev7ex.multiworld.command.WorldCommand;
import com.dev7ex.multiworld.listener.*;
import com.dev7ex.multiworld.user.UserService;
import com.dev7ex.multiworld.util.UpdateChecker;
import com.dev7ex.multiworld.world.DefaultWorldConfiguration;
import com.dev7ex.multiworld.world.DefaultWorldGeneratorProvider;
import com.dev7ex.multiworld.world.DefaultWorldManager;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author Dev7ex
 * @since 19.05.2021
 */
@Getter(AccessLevel.PUBLIC)
@PluginIdentification(spigotResourceId = 92559)
@PluginStatisticProperties(enabled = true, identification = 15446)
public final class MultiWorldPlugin extends BukkitPlugin implements MultiWorldBukkitApi, ConfigurablePlugin {

    private MultiWorldConfiguration configuration;
    private DefaultWorldConfiguration worldConfiguration;

    private DefaultWorldManager worldManager;

    private DefaultWorldProvider worldProvider;
    private UserService userProvider;
    private DefaultWorldGeneratorProvider worldGeneratorProvider;

    private final UpdateChecker updateChecker = new UpdateChecker(this);

    @Override
    public void onLoad() {
        super.createDataFolder();
        super.createSubFolder("user");
        super.createSubFolder("backup");

        this.configuration = new MultiWorldConfiguration(this);
        this.configuration.load();

        this.worldConfiguration = new DefaultWorldConfiguration(this);
        this.worldConfiguration.createFile();
        this.worldConfiguration.loadFile();
    }

    @Override
    public void onEnable() {
        MultiWorldApiProvider.registerApi(this);
        super.getServer().getServicesManager().register(MultiWorldBukkitApi.class, this, this, ServicePriority.Normal);

        this.worldManager = new DefaultWorldManager(this.worldConfiguration, this.configuration);

        this.updateChecker.getVersion(updateAvailable -> {});

        if (super.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new MultiWorldExpansion(this).register();
        }
        ConfigurationSerialization.registerClass(BukkitWorldLocation.class);
    }

    @Override
    public void onDisable() {
        MultiWorldApiProvider.unregisterApi();
    }

    @Override
    public void registerCommands() {
        super.registerCommand(new WorldCommand(this));
    }

    @Override
    public void registerListeners() {
        super.registerListener(new PlayerConnectionListener(this));
        super.registerListener(new PlayerEnterPortalListener(this));
        super.registerListener(new PlayerDamagePlayerListener(this));
        super.registerListener(new EntitySpawnListener(this));
        super.registerListener(new UserTeleportWorldListener(this));
    }

    @Override
    public void registerModules() {
        super.registerModule(this.worldProvider = new DefaultWorldProvider(this.worldManager, this.worldConfiguration));
        super.registerModule(this.userProvider = new UserService());
        super.registerModule(this.worldGeneratorProvider = new DefaultWorldGeneratorProvider());
    }

    @Override
    public File getBackupFolder() {
        return super.getSubFolder("backup");
    }

    @Override
    public File getUserFolder() {
        return super.getSubFolder("user");
    }

    public static MultiWorldPlugin getInstance() {
        return JavaPlugin.getPlugin(MultiWorldPlugin.class);
    }

}
