package com.dev7ex.multiworld;

import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.common.bukkit.plugin.ConfigurablePlugin;
import com.dev7ex.common.bukkit.plugin.PluginIdentification;
import com.dev7ex.common.bukkit.plugin.statistic.PluginStatisticProperties;
import com.dev7ex.multiworld.api.MultiWorldApiProvider;
import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.command.WorldCommand;
import com.dev7ex.multiworld.hook.DefaultHookProvider;
import com.dev7ex.multiworld.listener.entity.EntityPortalListener;
import com.dev7ex.multiworld.listener.entity.EntitySpawnListener;
import com.dev7ex.multiworld.listener.player.*;
import com.dev7ex.multiworld.listener.user.UserTeleportWorldListener;
import com.dev7ex.multiworld.listener.world.WorldActivityListener;
import com.dev7ex.multiworld.listener.world.WorldFlagListener;
import com.dev7ex.multiworld.task.WorldUnloadTask;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import com.dev7ex.multiworld.user.UserProvider;
import com.dev7ex.multiworld.util.PluginUpdater;
import com.dev7ex.multiworld.world.DefaultWorldConfiguration;
import com.dev7ex.multiworld.world.DefaultWorldManager;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import com.dev7ex.multiworld.world.generator.DefaultWorldGeneratorProvider;
import lombok.AccessLevel;
import lombok.Getter;
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
    private final PluginUpdater updater = new PluginUpdater(this);

    private DefaultWorldManager worldManager;

    private WorldUnloadTask worldUnloadTask;

    private DefaultWorldProvider worldProvider;
    private UserProvider userProvider;
    private DefaultWorldGeneratorProvider worldGeneratorProvider;
    private DefaultHookProvider hookProvider;
    private DefaultTranslationProvider translationProvider;

    @Override
    public void onLoad() {
        super.createDataFolder();
        super.createSubFolder("user");
        super.createSubFolder("backup");
        super.createSubFolder("language");

        this.configuration = new MultiWorldConfiguration(this);
        this.configuration.load();

        this.worldConfiguration = new DefaultWorldConfiguration(this);
        this.worldConfiguration.createFile();
        this.worldConfiguration.loadFile();
    }

    @Override
    public void onEnable() {
        super.getServer().getServicesManager().register(MultiWorldBukkitApi.class, this, this, ServicePriority.Normal);

        this.updater.checkAsync();

        MultiWorldApiProvider.registerApi(this);
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

        super.registerListener(new WorldActivityListener(this));
        super.registerListener(new WorldFlagListener(this));
    }

    @Override
    public void registerModules() {
        super.registerModule(this.translationProvider = new DefaultTranslationProvider(this));

        this.worldManager = new DefaultWorldManager(this.worldConfiguration, this.configuration, this.translationProvider);

        super.registerModule(this.worldGeneratorProvider = new DefaultWorldGeneratorProvider());

        super.registerModule(this.worldProvider = new DefaultWorldProvider(this.worldManager, this.worldConfiguration));
        super.registerModule(this.userProvider = new UserProvider());
        super.registerModule(this.hookProvider = new DefaultHookProvider());
    }

    @Override
    public void registerTasks() {
        super.getServer()
                .getScheduler()
                .runTaskTimer(this, this.worldUnloadTask = new WorldUnloadTask(this.getWorldProvider()), 20L, 20L);
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
