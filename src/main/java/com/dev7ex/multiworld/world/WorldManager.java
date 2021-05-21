package com.dev7ex.multiworld.world;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.generator.FlatChunkGenerator;
import com.dev7ex.multiworld.generator.VoidChunkGenerator;
import com.dev7ex.multiworld.user.WorldUser;

import com.dev7ex.multiworld.user.WorldUserProperties;
import com.dev7ex.multiworld.user.WorldUserService;

import com.google.common.collect.Maps;

import lombok.Getter;

import org.apache.commons.io.FileUtils;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author Dev7ex
 * @since 20.05.2021
 *
 */

@Getter
public final class WorldManager {

    private boolean serverCreatingWorld = false;
    private boolean serverDeletingWorld = false;

    private final Map<String, WorldProperties> worldProperties = Maps.newHashMap();
    private final WorldConfiguration worldConfiguration;
    private final WorldUserService worldUserService;

    public WorldManager(final WorldConfiguration worldConfiguration, final WorldUserService worldUserService) {
        this.worldConfiguration = worldConfiguration;
        this.worldUserService = worldUserService;
    }

    public final void createWorld(final WorldUser creator, final String worldName, final WorldType worldType) {
        final WorldCreator worldCreator = new WorldCreator(worldName);

        switch (worldType) {
            case NORMAL:
                worldCreator.type(org.bukkit.WorldType.NORMAL);
                break;

            case FLAT:
                worldCreator.generator(new FlatChunkGenerator());
                break;

            case VOID:
                worldCreator.generator(new VoidChunkGenerator());
                break;
        }
        this.serverCreatingWorld = true;
        creator.sendMessage("§7The world §a" + worldName + " §7is being created§7");
        worldCreator.createWorld();
        this.serverCreatingWorld = false;
        creator.sendMessage("§7The world §a" + worldName + " §7was successfully created!");
        final WorldProperties worldProperties = new WorldProperties(worldName, creator.getPlayer().getName(), System.currentTimeMillis(), 0L);
        worldProperties.setLoaded(true);
        this.worldProperties.put(worldName, worldProperties);
        this.worldConfiguration.registerWorld(worldName, worldProperties);
    }

    public final void deleteWorld(final WorldUser worldUser, final String worldName) {
        this.serverDeletingWorld = true;
        worldUser.sendMessage("§7The world §a" + worldName + " §7will be deleted...");

        if(Bukkit.getWorld(worldName) != null) {
            this.unloadWorld(worldUser, worldName);
        }
        try {
            FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer() + File.separator + worldName));
        } catch (final IOException exception) {
            worldUser.sendMessage("§cAn error has occurred. View the logs");
            return;
        }
        this.worldConfiguration.unregisterWorld(worldName);
        this.worldProperties.remove(worldName);
        worldUser.sendMessage("§7The world §a" + worldName + " §7has been deleted!");
        this.serverDeletingWorld = false;
    }

    public final void unloadWorld(final String worldName) {
        final World world = Bukkit.getWorld(worldName);

        for(final Chunk chunk : world.getLoadedChunks()) {
            if(chunk == null) {
                continue;
            }
            chunk.unload();
        }
        Bukkit.getLogger().info("Unload world + " + worldName);
        Bukkit.unloadWorld(world.getName(), true);
        final WorldProperties worldProperties = this.getWorldProperties().get(worldName);
        worldProperties.setLoaded(false);
    }

    public final void unloadWorld(final WorldUser worldUser, final String worldName) {
        final World world = Bukkit.getWorld(worldName);

        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getWorld().getName().equalsIgnoreCase(worldName)) {
                continue;
            }
            player.sendMessage(MultiWorldPlugin.getInstance().getConfiguration().getPrefix() + "§7This world has been unloaded you will be teleported");
            player.teleport(Bukkit.getWorld(MultiWorldPlugin.getInstance().getConfiguration().getDefaultWorldName()).getSpawnLocation());
        }

        if(world.getLoadedChunks() != null) {
            Arrays.stream(world.getLoadedChunks()).forEach(Chunk::unload);
            worldUser.sendMessage("§7All chunks are unloaded");
        }
        Bukkit.unloadWorld(world.getName(), true);
        worldUser.sendMessage("§7The World §a" + worldName + " §7has been unloaded");

        final WorldProperties worldProperties = this.getWorldProperties().get(worldName);
        worldProperties.setLoaded(false);
    }

    public final void loadWorld(final WorldUser worldUser, final String worldName) {
        worldUser.sendMessage("§7The world §a" + worldName + " §7is loading...");
        Bukkit.createWorld(new WorldCreator(worldName));
        worldUser.sendMessage("§7The world §a" + worldName + " §7was successfully loaded!");
        final WorldProperties worldProperties = this.worldConfiguration.getWorldProperties(worldName);
        worldProperties.setLoaded(true);
        this.worldProperties.put(worldName, worldProperties);
    }

    public final void teleportWorld(final WorldUser worldUser, final World world, final Location worldLocation) {
        final Player player = worldUser.getPlayer();
        final WorldUserProperties userProperties = worldUser.getProperties();
        userProperties.setLastWorld(player.getWorld().getName());
        userProperties.setLastWorldLocation(player.getLocation());
        player.teleport(worldLocation);

        final WorldProperties worldProperties = this.worldProperties.get(world.getName());
        worldProperties.setLastWorldInteraction(System.currentTimeMillis());

        this.worldConfiguration.updateLastWorldInteraction(this.worldProperties.get(world.getName()));
    }

}
