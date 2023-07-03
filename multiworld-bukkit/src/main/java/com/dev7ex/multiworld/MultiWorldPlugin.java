package com.dev7ex.multiworld;

import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.common.bukkit.plugin.PluginProperties;
import com.dev7ex.multiworld.api.MultiWorldApiProvider;
import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.expansion.MultiWorldExpansion;
import com.dev7ex.multiworld.api.bukkit.world.location.BukkitWorldLocation;
import com.dev7ex.multiworld.command.WorldCommand;
import com.dev7ex.multiworld.listener.PlayerConnectionListener;
import com.dev7ex.multiworld.listener.PlayerDamagePlayerListener;
import com.dev7ex.multiworld.listener.PlayerEnterPortalListener;
import com.dev7ex.multiworld.listener.UserTeleportWorldListener;
import com.dev7ex.multiworld.user.UserService;
import com.dev7ex.multiworld.util.UpdateChecker;
import com.dev7ex.multiworld.world.DefaultWorldConfiguration;
import com.dev7ex.multiworld.world.DefaultWorldGeneratorProvider;
import com.dev7ex.multiworld.world.DefaultWorldManager;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author Dev7ex
 * @since 19.05.2021
 */
@Getter(AccessLevel.PUBLIC)
@PluginProperties(resourceId = 92559, metricsId = 15446, metrics = true)
public final class MultiWorldPlugin extends BukkitPlugin implements MultiWorldBukkitApi {

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
    }

    @Override
    public void onEnable() {
        MultiWorldApiProvider.registerApi(this);

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
        super.registerCommand(new WorldCommand(this));
    }

    @Override
    public void registerListeners() {
        super.registerListener(new PlayerConnectionListener(this));
        super.registerListenerIf(new PlayerEnterPortalListener(this),
                enableIf -> this.configuration.isWorldLinkEnabled());
        super.registerListener(new PlayerDamagePlayerListener(this));

        super.registerListener(new UserTeleportWorldListener(this));
    }

    @Override
    public void registerServices() {
        super.registerService(this.worldProvider = new DefaultWorldProvider(this.worldManager, this.worldConfiguration));
        super.registerService(this.userProvider = new UserService());
        super.registerService(this.worldGeneratorProvider = new DefaultWorldGeneratorProvider());
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
