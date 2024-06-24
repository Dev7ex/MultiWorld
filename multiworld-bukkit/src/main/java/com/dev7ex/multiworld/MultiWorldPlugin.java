package com.dev7ex.multiworld;

import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.common.bukkit.plugin.ConfigurablePlugin;
import com.dev7ex.common.bukkit.plugin.PluginIdentification;
import com.dev7ex.common.bukkit.plugin.statistic.PluginStatisticProperties;
import com.dev7ex.common.bukkit.util.UpdateChecker;
import com.dev7ex.multiworld.api.MultiWorldApiProvider;
import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.expansion.MultiWorldExpansion;
import com.dev7ex.multiworld.api.bukkit.world.location.BukkitWorldLocation;
import com.dev7ex.multiworld.command.WorldCommand;
import com.dev7ex.multiworld.listener.entity.EntityPortalListener;
import com.dev7ex.multiworld.listener.entity.EntitySpawnListener;
import com.dev7ex.multiworld.listener.player.*;
import com.dev7ex.multiworld.listener.user.UserTeleportWorldListener;
import com.dev7ex.multiworld.user.UserProvider;
import com.dev7ex.multiworld.world.DefaultWorldConfiguration;
import com.dev7ex.multiworld.world.DefaultWorldManager;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import com.dev7ex.multiworld.world.generator.DefaultWorldGeneratorProvider;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * The main plugin class for MultiWorld.
 *
 * @author Dev7ex
 * @since 19.05.2021
 */
@Getter(AccessLevel.PUBLIC)
@PluginIdentification(spigotResourceId = 92559)
@PluginStatisticProperties(enabled = true, identification = 15446)
public final class MultiWorldPlugin extends BukkitPlugin implements MultiWorldBukkitApi, ConfigurablePlugin {

    private MultiWorldConfiguration configuration;
    private DefaultWorldConfiguration worldConfiguration;

    private final WorldCommand worldCommand = new WorldCommand(this);

    private DefaultWorldManager worldManager;

    private DefaultWorldProvider worldProvider;
    private UserProvider userProvider;
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

        this.updateChecker.getVersion(updateAvailable -> {
        });

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
        super.registerCommand(this.worldCommand);
    }

    @Override
    public void registerListeners() {
        super.registerListener(new EntityPortalListener(this));
        super.registerListener(new EntitySpawnListener(this));

        super.registerListener(new PlayerAdvancementDoneListener(this));
        super.registerListener(new PlayerChangeWorldListener(this));
        super.registerListener(new PlayerConnectionListener(this));
        super.registerListener(new PlayerDamagePlayerListener(this));
        super.registerListener(new PlayerPortalListener(this));

        super.registerListener(new UserTeleportWorldListener(this));
    }

    @Override
    public void registerModules() {
        super.registerModule(this.worldProvider = new DefaultWorldProvider(this.worldManager, this.worldConfiguration));
        super.registerModule(this.userProvider = new UserProvider());
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

    /**
     * Gets the instance of MultiWorldPlugin.
     *
     * @return The instance of MultiWorldPlugin.
     */
    public static MultiWorldPlugin getInstance() {
        return JavaPlugin.getPlugin(MultiWorldPlugin.class);
    }

}
