package com.dev7ex.multiworld.api;
import com.dev7ex.multicommon.api.configuration.ConfigurationHolder;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.api.module.WorldModuleManager;
import com.dev7ex.multiworld.api.scheduler.WorldScheduler;
import com.dev7ex.multiworld.api.user.WorldUserFactory;
import com.dev7ex.multiworld.api.user.WorldUserRegistry;
import com.dev7ex.multiworld.api.user.inventory.WorldUserInventoryFactory;
import com.dev7ex.multiworld.api.user.inventory.WorldUserInventoryRegistry;
import com.dev7ex.multiworld.api.world.WorldFactory;
import com.dev7ex.multiworld.api.world.WorldManager;
import com.dev7ex.multiworld.api.world.WorldRegistry;
import com.dev7ex.multiworld.api.world.WorldRepository;
import com.dev7ex.multiworld.api.world.WorldResolver;
import com.dev7ex.multiworld.api.world.backup.WorldBackupFactory;
import com.dev7ex.multiworld.api.world.backup.WorldBackupManager;
import com.dev7ex.multiworld.api.world.backup.WorldBackupRegistry;
import com.dev7ex.multiworld.api.world.flag.WorldFlagRegistry;
import com.dev7ex.multiworld.api.world.generator.WorldGeneratorFactory;
import com.dev7ex.multiworld.api.world.generator.WorldGeneratorRegistry;
import com.dev7ex.multiworld.api.world.group.WorldGroupFactory;
import com.dev7ex.multiworld.api.world.group.WorldGroupRegistry;
import com.dev7ex.multiworld.api.world.group.WorldGroupRepository;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public interface MultiWorldApi extends Plugin, ConfigurationHolder {

    @NotNull
    default NamespacedKey createKey(@NotNull final String key) {
        return new NamespacedKey(this, key);
    }

    @NotNull
    MultiWorldApiConfiguration getConfiguration();

    @Nullable
    File getBackupDirectory();

    void setBackupDirectory(@NotNull final File backupDirectory);

    @Nullable
    File getGeneratorDirectory();

    void setGeneratorDirectory(@NotNull final File generatorDirectory);

    @Nullable
    File getLanguageDirectory();

    void setLanguageDirectory(@NotNull final File languageDirectory);

    @Nullable
    File getLogDirectory();

    void setLogDirectory(@NotNull final File logDirectory);

    @Nullable
    File getUserDirectory();

    void setUserDirectory(@NotNull final File userDirectory);

    @NotNull
    WorldLocalizationRegistry getLocalizationRegistry();

    void setLocalizationRegistry(@NotNull final WorldLocalizationRegistry localizationRegistry);

    @NotNull
    WorldModuleManager getModuleManager();

    void setModuleManager(@NotNull final WorldModuleManager moduleManager);

    @NotNull
    WorldUserInventoryFactory getUserInventoryFactory();

    void setUserInventoryFactory(@NotNull final WorldUserInventoryFactory userInventoryFactory);

    @NotNull
    WorldUserInventoryRegistry getUserInventoryRegistry();

    void setUserInventoryRegistry(@NotNull final WorldUserInventoryRegistry userInventoryRegistry);

    @NotNull
    WorldUserFactory getUserFactory();

    void setUserFactory(@NotNull final WorldUserFactory userFactory);

    @NotNull
    WorldUserRegistry getUserRegistry();

    void setUserRegistry(@NotNull final WorldUserRegistry userRegistry);

    @NotNull WorldBackupFactory getBackupFactory();

    void setBackupFactory(@NotNull final WorldBackupFactory backupFactory);

    @NotNull
    WorldBackupManager getBackupManager();

    void setBackupManager(@NotNull final WorldBackupManager backupManager);

    @NotNull
    WorldBackupRegistry getBackupRegistry();

    void setBackupRegistry(@NotNull final WorldBackupRegistry backupRegistry);

    @NotNull
    WorldFlagRegistry getFlagRegistry();

    void setFlagRegistry(@NotNull final WorldFlagRegistry flagRegistry);

    @NotNull
    WorldGeneratorFactory getGeneratorFactory();

    void setGeneratorFactory(@NotNull final WorldGeneratorFactory generatorFactory);

    @NotNull
    WorldGeneratorRegistry getGeneratorRegistry();

    void setGeneratorRegistry(@NotNull final WorldGeneratorRegistry generatorRegistry);

    @NotNull
    WorldGroupFactory getGroupFactory();

    void setGroupFactory(@NotNull final WorldGroupFactory groupFactory);

    @NotNull
    WorldGroupRegistry getGroupRegistry();

    void setGroupRegistry(@NotNull final WorldGroupRegistry groupRegistry);

    @NotNull
    WorldFactory getWorldFactory();

    void setWorldFactory(@NotNull final WorldFactory worldFactory);

    @NotNull
    WorldManager getWorldManager();

    void setWorldManager(@NotNull final WorldManager worldManager);

    @NotNull
    WorldResolver getWorldResolver();

    @NotNull
    WorldScheduler getScheduler();

    @NotNull
    WorldRegistry getWorldRegistry();

    void setWorldRegistry(@NotNull final WorldRegistry worldRegistry);

    @NotNull WorldRepository getWorldRepository();

    void setWorldRepository(@NotNull final WorldRepository worldRepository);

    WorldGroupRepository getGroupRepository();

    void setGroupRepository(@NotNull final WorldGroupRepository groupRepository);



}
