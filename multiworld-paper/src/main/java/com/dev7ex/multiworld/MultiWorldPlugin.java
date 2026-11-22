package com.dev7ex.multiworld;

import com.dev7ex.multicommon.api.configuration.ConfigurationHolder;
import com.dev7ex.multicommon.api.plugin.AdvancedPlugin;
import com.dev7ex.multicommon.api.plugin.PluginFileMonitor;
import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.MultiWorldApiProvider;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.api.module.WorldModuleManager;
import com.dev7ex.multiworld.api.permission.MultiWorldPermission;
import com.dev7ex.multiworld.api.scheduler.WorldScheduler;
import com.dev7ex.multiworld.api.user.WorldUserFactory;
import com.dev7ex.multiworld.api.user.WorldUserRegistry;
import com.dev7ex.multiworld.api.user.inventory.WorldUserInventoryFactory;
import com.dev7ex.multiworld.api.user.inventory.WorldUserInventoryRegistry;
import com.dev7ex.multiworld.api.util.BukkitUtil;
import com.dev7ex.multiworld.api.world.*;
import com.dev7ex.multiworld.api.world.backup.WorldBackupFactory;
import com.dev7ex.multiworld.api.world.backup.WorldBackupManager;
import com.dev7ex.multiworld.api.world.backup.WorldBackupRegistry;
import com.dev7ex.multiworld.api.world.flag.WorldFlagRegistry;
import com.dev7ex.multiworld.api.world.generator.WorldGeneratorFactory;
import com.dev7ex.multiworld.api.world.generator.WorldGeneratorRegistry;
import com.dev7ex.multiworld.api.world.generator.WorldGeneratorRepository;
import com.dev7ex.multiworld.api.world.group.WorldGroupFactory;
import com.dev7ex.multiworld.api.world.group.WorldGroupManager;
import com.dev7ex.multiworld.api.world.group.WorldGroupRegistry;
import com.dev7ex.multiworld.api.world.group.WorldGroupRepository;
import com.dev7ex.multiworld.command.WorldCommand;
import com.dev7ex.multiworld.listener.plugin.MultiWorldFileChangeListener;
import com.dev7ex.multiworld.localization.DefaultWorldLocalizationRegistry;
import com.dev7ex.multiworld.module.DefaultWorldModuleManager;
import com.dev7ex.multiworld.scheduler.BukkitWorldScheduler;
import com.dev7ex.multiworld.scheduler.PaperWorldScheduler;
import com.dev7ex.multiworld.user.DefaultWorldUserFactory;
import com.dev7ex.multiworld.user.DefaultWorldUserRegistry;
import com.dev7ex.multiworld.world.*;
import com.dev7ex.multiworld.world.backup.DefaultWorldBackupFactory;
import com.dev7ex.multiworld.world.backup.DefaultWorldBackupManager;
import com.dev7ex.multiworld.world.backup.DefaultWorldBackupRegistry;
import com.dev7ex.multiworld.world.flag.DefaultWorldFlagRegistry;
import com.dev7ex.multiworld.world.generator.DefaultWorldGeneratorRegistry;
import com.dev7ex.multiworld.world.generator.DefaultWorldGeneratorRepository;
import com.dev7ex.multiworld.world.group.DefaultWorldGroupFactory;
import com.dev7ex.multiworld.world.group.DefaultWorldGroupManager;
import com.dev7ex.multiworld.world.group.DefaultWorldGroupRegistry;
import com.dev7ex.multiworld.world.group.DefaultWorldGroupRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

/**
 * @author Dev7ex
 * @since 19.05.2021
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class MultiWorldPlugin extends AdvancedPlugin implements MultiWorldApi, ConfigurationHolder {

    private static final String PAPER_SCHEDULER_CLASS = "io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler";
    private static final String FOLIA_SERVER_CLASS = "io.papermc.paper.threadedregions.RegionizedServer";

    private MultiWorldConfiguration configuration;

    private WorldGeneratorRepository generatorRepository;
    private WorldGroupRepository groupRepository;
    private WorldRepository worldRepository;

    private WorldModuleManager moduleManager;
    private WorldBackupManager backupManager;
    private WorldGroupManager groupManager;
    private WorldManager worldManager;
    private WorldResolver worldResolver;
    private WorldScheduler scheduler;

    private WorldLocalizationRegistry localizationRegistry;
    private WorldUserInventoryRegistry userInventoryRegistry;
    private WorldUserRegistry userRegistry;
    private WorldBackupRegistry backupRegistry;
    private WorldFlagRegistry flagRegistry;
    private WorldGeneratorRegistry generatorRegistry;
    private WorldGroupRegistry groupRegistry;
    private WorldRegistry worldRegistry;

    private WorldUserInventoryFactory userInventoryFactory;
    private WorldUserFactory userFactory;
    private WorldBackupFactory backupFactory;
    private WorldGeneratorFactory generatorFactory;
    private WorldGroupFactory groupFactory;
    private WorldFactory worldFactory;

    private File backupDirectory;
    private File generatorDirectory;
    private File languageDirectory;
    private File logDirectory;
    private File userDirectory;

    private PluginFileMonitor fileMonitor;

    @Override
    public void onLoad() {
        this.createDataDirectory();

        this.backupDirectory = super.createSubDirectory("backup");
        this.generatorDirectory = this.createSubDirectory("generator");
        this.languageDirectory = super.createSubDirectory("language");
        this.logDirectory = this.createSubDirectory("log");
        this.userDirectory = super.createSubDirectory("user");

        this.configuration = new MultiWorldConfiguration(this);
        this.configuration.copyFile();
        this.configuration.loadFile();

        this.registerRepositories();
    }

    @Override
    public void onEnable() {
        this.registerScheduler();

        MultiWorldApiProvider.register(this);

        try {
            this.fileMonitor = new PluginFileMonitor(this, new MultiWorldFileChangeListener(this));
            this.fileMonitor.start();

        } catch (final LinkageError exception) {
            this.fileMonitor = null;
            super.getLogger().warning("File monitoring isn't available on this server version");
        }

        super.getServer().getServicesManager().register(MultiWorldApi.class, this, this, ServicePriority.Normal);

        Arrays.stream(MultiWorldPermission.values())
                .map(MultiWorldPermission::asBukkit)
                .filter(permission -> super.getServer().getPluginManager().getPermission(permission.getName()) == null)
                .forEach(permission -> super.getServer().getPluginManager().addPermission(permission));

        this.registerManagers();

        this.registerModules();
        this.registerFactories();
        this.registerCommands();
        this.registerListeners();
        this.registerTasks();

        this.moduleManager.enableAll();
    }

    @Override
    public void onDisable() {
        if (this.backupFactory instanceof final DefaultWorldBackupFactory defaultBackupFactory) {
            defaultBackupFactory.resumeAutoSaves();
        }
        this.moduleManager.disableAll();

        MultiWorldApiProvider.unregister();
        super.getServer().getServicesManager().unregisterAll(this);

        if (this.fileMonitor != null) {
            this.fileMonitor.stop();
        }
    }

    private void registerScheduler() {
        if (!BukkitUtil.isClassPresent(MultiWorldPlugin.PAPER_SCHEDULER_CLASS)) {
            this.scheduler = new BukkitWorldScheduler(this);
            return;
        }
        this.scheduler = new PaperWorldScheduler(this, BukkitUtil.isClassPresent(MultiWorldPlugin.FOLIA_SERVER_CLASS));
    }

    @Override
    public void registerCommands() {
        this.registerCommand(new WorldCommand(this));
    }

    @Override
    public void registerFactories() {
        this.userFactory = new DefaultWorldUserFactory();
        this.backupFactory = new DefaultWorldBackupFactory(this);
        this.groupFactory = new DefaultWorldGroupFactory();
        this.worldFactory = new DefaultWorldFactory(this);
    }

    @Override
    public void registerListeners() {

    }

    @Override
    public void registerManagers() {
        this.moduleManager = new DefaultWorldModuleManager();
        this.backupManager = new DefaultWorldBackupManager(this);
        this.groupManager = new DefaultWorldGroupManager(this);
        this.worldManager = new DefaultWorldManager(this);
        this.worldResolver = new DefaultWorldResolver(this);
    }

    @Override
    public void registerModules() {
        this.moduleManager.register(this.localizationRegistry = new DefaultWorldLocalizationRegistry(this));
        this.moduleManager.register(this.userRegistry = new DefaultWorldUserRegistry(this));
        this.moduleManager.register(this.flagRegistry = new DefaultWorldFlagRegistry(this));
        this.moduleManager.register(this.groupRegistry = new DefaultWorldGroupRegistry(this));
        this.moduleManager.register(this.worldRegistry = new DefaultWorldRegistry(this));
        this.moduleManager.register(this.backupRegistry = new DefaultWorldBackupRegistry(this));
        this.moduleManager.register(this.generatorRegistry = new DefaultWorldGeneratorRegistry(this));
    }

    @Override
    public void registerRepositories() {
        this.generatorRepository = new DefaultWorldGeneratorRepository(this);
        this.generatorRepository.createFile();
        this.generatorRepository.loadFile();

        this.groupRepository = new DefaultWorldGroupRepository(this);
        this.groupRepository.createFile();
        this.groupRepository.loadFile();

        this.worldRepository = new DefaultWorldRepository(this);
        this.worldRepository.createFile();
        this.worldRepository.loadFile();
    }

    public static MultiWorldPlugin getInstance() {
        return JavaPlugin.getPlugin(MultiWorldPlugin.class);
    }

}
