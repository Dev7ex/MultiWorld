
package com.dev7ex.multiworld.api.event;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.MultiWorldApiConfiguration;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.api.module.WorldModuleManager;
import com.dev7ex.multiworld.api.user.WorldUserFactory;
import com.dev7ex.multiworld.api.user.WorldUserRegistry;
import com.dev7ex.multiworld.api.user.inventory.WorldUserInventoryFactory;
import com.dev7ex.multiworld.api.user.inventory.WorldUserInventoryRegistry;
import com.dev7ex.multiworld.api.world.*;
import com.dev7ex.multiworld.api.world.backup.WorldBackupFactory;
import com.dev7ex.multiworld.api.world.backup.WorldBackupManager;
import com.dev7ex.multiworld.api.world.backup.WorldBackupRegistry;
import com.dev7ex.multiworld.api.world.flag.WorldFlagRegistry;
import com.dev7ex.multiworld.api.world.generator.WorldGeneratorFactory;
import com.dev7ex.multiworld.api.world.generator.WorldGeneratorRegistry;
import com.dev7ex.multiworld.api.world.group.WorldGroupFactory;
import com.dev7ex.multiworld.api.world.group.WorldGroupRegistry;
import com.dev7ex.multiworld.api.world.group.WorldGroupRepository;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public abstract class MultiWorldListener implements Listener {

    protected final MultiWorldApi multiWorldApi;

    protected MultiWorldListener(@NotNull final MultiWorldApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }

    @NotNull
    public MultiWorldApiConfiguration getConfiguration() {
        return this.multiWorldApi.getConfiguration();
    }

    @Nullable
    public File getBackupDirectory() {
        return this.multiWorldApi.getBackupDirectory();
    }

    @Nullable
    public File getDatabaseDirectory() {
        return this.multiWorldApi.getDatabaseDirectory();
    }

    @Nullable
    public File getGeneratorDirectory() {
        return this.multiWorldApi.getGeneratorDirectory();
    }

    @Nullable
    public File getLanguageDirectory() {
        return this.multiWorldApi.getLanguageDirectory();
    }

    @Nullable
    public File getLogDirectory() {
        return this.multiWorldApi.getLogDirectory();
    }

    @Nullable
    public File getUserDirectory() {
        return this.multiWorldApi.getUserDirectory();
    }

    @NotNull
    public WorldLocalizationRegistry getLocalizationRegistry() {
        return this.multiWorldApi.getLocalizationRegistry();
    }

    @NotNull
    public WorldModuleManager getModuleManager() {
        return this.multiWorldApi.getModuleManager();
    }

    @NotNull
    public WorldUserInventoryFactory getUserInventoryFactory() {
        return this.multiWorldApi.getUserInventoryFactory();
    }

    @NotNull
    public WorldUserInventoryRegistry getUserInventoryRegistry() {
        return this.multiWorldApi.getUserInventoryRegistry();
    }

    @NotNull
    public WorldUserFactory getUserFactory() {
        return this.multiWorldApi.getUserFactory();
    }

    @NotNull
    public WorldUserRegistry getUserRegistry() {
        return this.multiWorldApi.getUserRegistry();
    }

    @NotNull
    public WorldBackupFactory getBackupFactory() {
        return this.multiWorldApi.getBackupFactory();
    }

    @NotNull
    public WorldBackupManager getBackupManager() {
        return this.multiWorldApi.getBackupManager();
    }

    @NotNull
    public WorldBackupRegistry getBackupRegistry() {
        return this.multiWorldApi.getBackupRegistry();
    }

    @NotNull
    public WorldFlagRegistry getFlagRegistry() {
        return this.multiWorldApi.getFlagRegistry();
    }

    @NotNull
    public WorldGeneratorFactory getGeneratorFactory() {
        return this.multiWorldApi.getGeneratorFactory();
    }

    @NotNull
    public WorldGeneratorRegistry getGeneratorRegistry() {
        return this.multiWorldApi.getGeneratorRegistry();
    }

    @NotNull
    public WorldGroupFactory getGroupFactory() {
        return this.multiWorldApi.getGroupFactory();
    }

    @NotNull
    public WorldGroupRegistry getGroupRegistry() {
        return this.multiWorldApi.getGroupRegistry();
    }

    @NotNull
    public WorldGroupRepository getGroupRepository() {
        return this.multiWorldApi.getGroupRepository();
    }

    @NotNull
    public WorldFactory getWorldFactory() {
        return this.multiWorldApi.getWorldFactory();
    }

    @NotNull
    public WorldManager getWorldManager() {
        return this.multiWorldApi.getWorldManager();
    }

    @NotNull
    public WorldResolver getWorldResolver() {
        return this.multiWorldApi.getWorldResolver();
    }

    @NotNull
    public WorldRegistry getWorldRegistry() {
        return this.multiWorldApi.getWorldRegistry();
    }

    @NotNull
    public WorldRepository getWorldRepository() {
        return this.multiWorldApi.getWorldRepository();
    }

}
