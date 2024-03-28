package com.dev7ex.multiworld.api.bukkit.event;

import com.dev7ex.multiworld.api.MultiWorldApiConfiguration;
import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldGeneratorProvider;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldManager;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldProvider;
import com.dev7ex.multiworld.api.user.WorldUserProvider;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public abstract class MultiWorldListener implements Listener {

    private final MultiWorldBukkitApi multiWorldApi;
    public MultiWorldListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }

    public MultiWorldApiConfiguration getConfiguration() {
        return this.multiWorldApi.getConfiguration();
    }

    public String getPrefix() {
        return this.multiWorldApi.getConfiguration().getPrefix();
    }

    public BukkitWorldProvider getWorldProvider() {
        return this.multiWorldApi.getWorldProvider();
    }

    public BukkitWorldManager getWorldManager() {
        return this.multiWorldApi.getWorldManager();
    }

    public BukkitWorldGeneratorProvider getWorldGeneratorProvider() {
        return this.multiWorldApi.getWorldGeneratorProvider();
    }

    protected WorldUserProvider getUserProvider() {
        return this.multiWorldApi.getUserProvider();
    }

}
