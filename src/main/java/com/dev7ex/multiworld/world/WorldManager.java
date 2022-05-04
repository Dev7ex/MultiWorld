package com.dev7ex.multiworld.world;

import com.dev7ex.multiworld.MultiWorldConfiguration;
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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@Getter
public final class WorldManager {

    private boolean serverCreatingWorld = false;
    private boolean serverDeletingWorld = false;

    private final Map<String, WorldProperties> worldProperties = Maps.newHashMap();
    private final WorldConfiguration worldConfiguration;
    private final WorldUserService worldUserService;
    private final MultiWorldConfiguration configuration;

    public WorldManager(final MultiWorldConfiguration configuration, final WorldConfiguration worldConfiguration, final WorldUserService worldUserService) {
        this.configuration = configuration;
        this.worldConfiguration = worldConfiguration;
        this.worldUserService = worldUserService;
    }

    public final void createWorld(final CommandSender commandSender, final String worldName, final WorldType worldType) {
        final WorldCreator worldCreator = new WorldCreator(worldName);

        switch (worldType) {
            case FLAT:
                worldCreator.generator(new FlatChunkGenerator());
                break;

            case NETHER:
                worldCreator.environment(World.Environment.NETHER);
                break;

            case END:
                worldCreator.environment(World.Environment.THE_END);
                break;

            case VOID:
                worldCreator.generator(new VoidChunkGenerator());
                break;

            default:
                worldCreator.type(org.bukkit.WorldType.NORMAL);
                break;
        }
        this.serverCreatingWorld = true;
        commandSender.sendMessage(this.configuration.getWorldMessage("create.starting").replaceAll("%world%", worldName));
        final WorldProperties worldProperties = new WorldProperties(worldName, commandSender.getName(),
                System.currentTimeMillis(), System.currentTimeMillis(), worldType,
                Difficulty.valueOf(this.configuration.getValues().getString("defaults.difficulty")),
                GameMode.valueOf(this.configuration.getValues().getString("defaults.gameMode")),
                this.configuration.getValues().getBoolean("defaults.pvp-enabled"),
                this.configuration.getValues().getBoolean("defaults.spawn-animals", false),
                this.configuration.getValues().getBoolean("defaults.spawn-monsters", false));

        final World world = worldCreator.createWorld();
        commandSender.sendMessage(this.configuration.getWorldMessage("create.finished").replaceAll("%world%", worldName));
        worldProperties.setLoaded(true);
        world.setDifficulty(worldProperties.getDifficulty());
        this.worldConfiguration.registerWorld(worldName, worldProperties);
        this.worldProperties.put(worldName, worldProperties);
        this.serverCreatingWorld = false;
    }

    public final void unloadWorld(final CommandSender commandSender, final String worldName) {
        final World world = Bukkit.getWorld(worldName);

        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getWorld().getName().equalsIgnoreCase(worldName)) {
                continue;
            }
            player.sendMessage(this.configuration.getWorldMessage("unloading.chunk-teleport").replaceAll("%world%", worldName));
            player.teleport(Bukkit.getWorld(MultiWorldPlugin.getInstance().getConfiguration().getDefaultWorldName()).getSpawnLocation());
        }
        commandSender.sendMessage(this.configuration.getWorldMessage("unloading.chunk-starting").replaceAll("%world%", worldName));
        Arrays.stream(world.getLoadedChunks()).forEach(Chunk::unload);
        commandSender.sendMessage(this.configuration.getWorldMessage("unloading.chunk-finished").replaceAll("%world%", worldName));

        commandSender.sendMessage(this.configuration.getWorldMessage("unloading.starting").replaceAll("%world%", worldName));
        Bukkit.unloadWorld(world.getName(), true);
        commandSender.sendMessage(this.configuration.getWorldMessage("unloading.finished").replaceAll("%world%", worldName));
        this.worldProperties.get(worldName).setLoaded(false);
    }

    public final void deleteWorld(final CommandSender commandSender, final String worldName) {
        this.serverDeletingWorld = true;
        commandSender.sendMessage(this.configuration.getWorldMessage("delete.starting").replaceAll("%world%", worldName));

        if (Bukkit.getWorld(worldName) != null) {
            this.unloadWorld(commandSender, worldName);
        }

        try {
            FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer() + File.separator + worldName));

        } catch (final IOException exception) {
            commandSender.sendMessage("§cAn error has occurred. View the logs");
            exception.printStackTrace();
            return;
        }
        this.worldConfiguration.unregisterWorld(worldName);
        this.worldProperties.remove(worldName);
        commandSender.sendMessage(this.configuration.getWorldMessage("delete.finished").replaceAll("%world%", worldName));
        this.serverDeletingWorld = false;
    }

    public final void loadWorld(final CommandSender commandSender, final String worldName) {
        final WorldProperties worldProperties = this.worldConfiguration.getWorldProperties(worldName);
        final WorldCreator worldCreator = new WorldCreator(worldName);

        commandSender.sendMessage(this.configuration.getWorldMessage("loading.starting").replaceAll("%world%", worldName));

        if (!worldProperties.getWorldType().isOverWorld()) {
            worldCreator.environment(worldProperties.getWorldType().getEnvironment());
        }

        switch (worldProperties.getWorldType()) {
            case VOID: worldCreator.generator(new VoidChunkGenerator()); break;
            case FLAT: worldCreator.generator(new FlatChunkGenerator()); break;
        }

        final World world = Bukkit.createWorld(worldCreator);
        world.setDifficulty(worldProperties.getDifficulty());
        commandSender.sendMessage(this.configuration.getWorldMessage("loading.finished").replaceAll("%world%", worldName));

        worldProperties.setLoaded(true);
        this.worldProperties.put(worldName, worldProperties);
    }

    public final void importWorld(final CommandSender commandSender, final String worldName, final WorldType worldType) {
        commandSender.sendMessage(this.configuration.getWorldMessage("import.starting").replaceAll("%world%", worldName));

        final WorldProperties worldProperties = new WorldProperties(worldName, commandSender.getName(),
                System.currentTimeMillis(), System.currentTimeMillis(), worldType,
                Difficulty.valueOf(this.configuration.getValues().getString("defaults.difficulty")),
                GameMode.valueOf(this.configuration.getValues().getString("defaults.gameMode")),
                this.configuration.getValues().getBoolean("defaults.pvp-enabled"),
                this.configuration.getValues().getBoolean("defaults.spawn-animals", false),
                this.configuration.getValues().getBoolean("defaults.spawn-monsters", false));

        this.worldConfiguration.registerWorld(worldName, worldProperties);
        this.worldProperties.put(worldName, worldProperties);
        commandSender.sendMessage(this.configuration.getWorldMessage("import.finished").replaceAll("%world%", worldName));
    }

    public final void teleportWorld(final CommandSender commandSender, final Player target, final Location teleportLocation) {
        final WorldUser worldUser = this.worldUserService.getUsers().get(target.getUniqueId());
        final WorldUserProperties userProperties = worldUser.getProperties();
        final WorldProperties worldProperties = this.worldProperties.get(teleportLocation.getWorld().getName());

        userProperties.setLastWorld(target.getWorld().getName());
        userProperties.setLastWorldLocation(target.getLocation());

        target.teleport(teleportLocation);
        commandSender.sendMessage(this.configuration.getWorldMessage("teleport.message").replaceAll("%player%", target.getName()).replaceAll("%world%", teleportLocation.getWorld().getName()));

        worldProperties.setLastWorldInteraction(System.currentTimeMillis());
        this.worldConfiguration.updateLastWorldInteraction(worldProperties);
    }

    public final void teleportWorld(final Player target, final Location teleportLocation) {
        final WorldUser worldUser = this.worldUserService.getUsers().get(target.getUniqueId());
        final WorldUserProperties userProperties = worldUser.getProperties();
        final WorldProperties worldProperties = this.worldProperties.get(teleportLocation.getWorld().getName());

        userProperties.setLastWorld(target.getWorld().getName());
        userProperties.setLastWorldLocation(target.getLocation());

        target.teleport(teleportLocation);

        worldProperties.setLastWorldInteraction(System.currentTimeMillis());
        this.worldConfiguration.updateLastWorldInteraction(this.worldProperties.get(teleportLocation.getWorld().getName()));
    }

}
