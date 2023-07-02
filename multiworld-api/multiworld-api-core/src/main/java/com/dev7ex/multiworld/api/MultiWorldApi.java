package com.dev7ex.multiworld.api;

import com.dev7ex.multiworld.api.user.WorldUserProvider;
import com.dev7ex.multiworld.api.world.WorldConfiguration;
import com.dev7ex.multiworld.api.world.WorldGeneratorProvider;
import com.dev7ex.multiworld.api.world.WorldManager;
import com.dev7ex.multiworld.api.world.WorldProvider;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author Dev7ex
 * @since 20.12.2022
 */
public interface MultiWorldApi {

    @NotNull
    MultiWorldApiConfiguration getConfiguration();

    @NotNull
    WorldConfiguration<?> getWorldConfiguration();

    @NotNull
    WorldProvider<?> getWorldProvider();

    @NotNull
    WorldManager getWorldManager();

    @NotNull
    WorldGeneratorProvider getWorldGeneratorProvider();

    @NotNull
    WorldUserProvider getUserProvider();

    File getUserFolder();

    File getBackupFolder();

}
