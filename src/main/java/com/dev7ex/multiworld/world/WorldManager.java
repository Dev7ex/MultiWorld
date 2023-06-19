package com.dev7ex.multiworld.world;

import com.dev7ex.common.io.Files;
import com.dev7ex.multiworld.MultiWorldConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.event.world.WorldCloneEvent;
import com.dev7ex.multiworld.api.event.world.WorldCreateEvent;
import com.dev7ex.multiworld.api.event.world.WorldDeleteEvent;
import com.dev7ex.multiworld.command.WorldCommand;
import com.dev7ex.multiworld.generator.FlatChunkGenerator;
import com.dev7ex.multiworld.generator.VoidChunkGenerator;
import com.dev7ex.multiworld.user.WorldUser;
import com.dev7ex.multiworld.user.WorldUserProperties;
import com.dev7ex.multiworld.user.WorldUserService;

import com.google.common.collect.Maps;

import lombok.AccessLevel;
import lombok.Getter;

import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@Getter(AccessLevel.PUBLIC)
public final class WorldManager {

    private final Map<String, WorldProperties> worldProperties = Maps.newHashMap();
    private final WorldConfiguration worldConfiguration;
    private final WorldUserService worldUserService;
    private final MultiWorldConfiguration configuration;

    public WorldManager(final MultiWorldConfiguration configuration, final WorldConfiguration worldConfiguration, final WorldUserService worldUserService) {
        this.configuration = configuration;
        this.worldConfiguration = worldConfiguration;
        this.worldUserService = worldUserService;
    }

    public void createBackup(final CommandSender commandSender, final String worldName) {
        final File sourceFolder = new File(Bukkit.getWorldContainer(), worldName);
        final File destinationFolder = new File(MultiWorldPlugin.getInstance().getSubFolder("backup"),
                worldName + "-" + new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss").format(Calendar.getInstance().getTime()));

        commandSender.sendMessage(this.configuration.getMessage("backup.starting").replaceAll("%world%", worldName));

        try {
            final File sessionFile = new File(sourceFolder, "session.lock");

            if (sessionFile.exists()) {
                sessionFile.delete();
            }
            FileUtils.copyDirectory(sourceFolder, destinationFolder);

            for (final File file : Files.getFiles(destinationFolder)) {
                if (!file.isFile()) {
                    continue;
                }
                if (!file.getName().equalsIgnoreCase("uid.dat")) {
                    continue;
                }
                file.delete();
            }
            commandSender.sendMessage(this.configuration.getMessage("backup.finished").replaceAll("%world%", worldName));

        } catch (final IOException exception) {
            commandSender.sendMessage("§cAn error has occurred. View the logs");
            exception.printStackTrace();
        }

    }

    public void createWorld(final CommandSender commandSender, final String worldName, final WorldType worldType) {
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
        commandSender.sendMessage(this.configuration.getMessage("create.starting").replaceAll("%world%", worldName));

        final WorldProperties worldProperties = new WorldProperties(worldName, commandSender.getName(),
                System.currentTimeMillis(), System.currentTimeMillis(),
                worldType, Difficulty.valueOf(this.configuration.getString("defaults.difficulty")),
                GameMode.valueOf(this.configuration.getString("defaults.gamemode")), this.configuration.getBoolean("defaults.pvp-enabled"),
                this.configuration.getBoolean("defaults.spawn-animals"), this.configuration.getBoolean("defaults.spawn-monsters"));

        final WorldCreateEvent event = new WorldCreateEvent(worldProperties);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        final World world = worldCreator.createWorld();

        commandSender.sendMessage(this.configuration.getMessage("create.finished").replaceAll("%world%", worldName));

        worldProperties.setLoaded(true);
        world.setDifficulty(worldProperties.getDifficulty());
        world.setSpawnFlags(worldProperties.isSpawnMonsters(), worldProperties.isSpawnAnimals());

        this.worldConfiguration.registerWorld(worldName, worldProperties);
        this.worldProperties.put(worldName, worldProperties);
    }

    public void createWorld(final CommandSender commandSender, final String worldName, final long seed) {
        final WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.seed(seed);

        commandSender.sendMessage(this.configuration.getMessage("create.starting").replaceAll("%world%", worldName));

        final WorldProperties worldProperties = new WorldProperties(worldName, commandSender.getName(),
                System.currentTimeMillis(), System.currentTimeMillis(),
                WorldType.NORMAL, Difficulty.valueOf(this.configuration.getString("defaults.difficulty")),
                GameMode.valueOf(this.configuration.getString("defaults.gamemode")), this.configuration.getBoolean("defaults.pvp-enabled"),
                this.configuration.getBoolean("defaults.spawn-animals"), this.configuration.getBoolean("defaults.spawn-monsters"));

        final WorldCreateEvent event = new WorldCreateEvent(worldProperties);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        final World world = worldCreator.createWorld();

        commandSender.sendMessage(this.configuration.getMessage("create.finished").replaceAll("%world%", worldName));

        worldProperties.setLoaded(true);
        world.setDifficulty(worldProperties.getDifficulty());
        world.setSpawnFlags(worldProperties.isSpawnMonsters(), worldProperties.isSpawnAnimals());

        this.worldConfiguration.registerWorld(worldName, worldProperties);
        this.worldProperties.put(worldName, worldProperties);
    }

    public void createWorld(final CommandSender commandSender, final String worldName, final String generator) {
        final WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.generator(generator);

        commandSender.sendMessage(this.configuration.getMessage("create.starting").replaceAll("%world%", worldName));

        final WorldProperties worldProperties = new WorldProperties(worldName, commandSender.getName(),
                System.currentTimeMillis(), System.currentTimeMillis(),
                WorldType.NORMAL, Difficulty.valueOf(this.configuration.getString("defaults.difficulty")),
                GameMode.valueOf(this.configuration.getString("defaults.gamemode")), this.configuration.getBoolean("defaults.pvp-enabled"),
                this.configuration.getBoolean("defaults.spawn-animals"), this.configuration.getBoolean("defaults.spawn-monsters"));

        final WorldCreateEvent event = new WorldCreateEvent(worldProperties);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        final World world = worldCreator.createWorld();

        commandSender.sendMessage(this.configuration.getMessage("create.finished").replaceAll("%world%", worldName));

        worldProperties.setLoaded(true);
        world.setDifficulty(worldProperties.getDifficulty());
        world.setSpawnFlags(worldProperties.isSpawnMonsters(), worldProperties.isSpawnAnimals());

        this.worldConfiguration.registerWorld(worldName, worldProperties);
        this.worldProperties.put(worldName, worldProperties);
    }

    public void cloneWorld(final CommandSender commandSender, final String worldName, final String clonedName) {
        final File sourceFolder = new File(Bukkit.getWorldContainer(), worldName);
        final File destinationFolder = new File(Bukkit.getWorldContainer(), clonedName);

        final WorldCloneEvent event = new WorldCloneEvent(commandSender, worldName, clonedName, sourceFolder, destinationFolder);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        commandSender.sendMessage(this.configuration.getMessage("clone.starting").replaceAll("%world%", worldName));

        try {
            final File sessionFile = new File(sourceFolder, "session.lock");

            if (sessionFile.exists()) {
                sessionFile.delete();
            }

            FileUtils.copyDirectory(sourceFolder, destinationFolder);

            for (final File file : Files.getFiles(destinationFolder)) {
                if (!file.isFile()) {
                    continue;
                }
                if (!file.getName().equalsIgnoreCase("uid.dat")) {
                    continue;
                }
                file.delete();
            }
            commandSender.sendMessage(this.configuration.getMessage("clone.finished").replaceAll("%world%", worldName));

        } catch (final IOException exception) {
            commandSender.sendMessage("§cAn error has occurred. View the logs");
            exception.printStackTrace();
        }
    }

    public void deleteWorld(final CommandSender commandSender, final String worldName) {
        commandSender.sendMessage(this.configuration.getMessage("delete.starting").replaceAll("%world%", worldName));

        final WorldDeleteEvent event = new WorldDeleteEvent(this.worldConfiguration.getWorldProperties(worldName));
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

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
        commandSender.sendMessage(this.configuration.getMessage("delete.finished").replaceAll("%world%", worldName));
    }

    public void importWorld(final CommandSender commandSender, final String worldName, final WorldType worldType) {
        commandSender.sendMessage(this.configuration.getMessage("import.starting").replaceAll("%world%", worldName));

        final WorldProperties worldProperties = new WorldProperties(worldName, commandSender.getName(),
                System.currentTimeMillis(), System.currentTimeMillis(), worldType,
                Difficulty.valueOf(this.configuration.getString("defaults.difficulty")),
                GameMode.valueOf(this.configuration.getString("defaults.gamemode")),
                this.configuration.getBoolean("defaults.pvp-enabled"),
                this.configuration.getBoolean("defaults.spawn-animals"),
                this.configuration.getBoolean("defaults.spawn-monsters"));

        this.worldConfiguration.registerWorld(worldName, worldProperties);
        this.worldProperties.put(worldName, worldProperties);
        commandSender.sendMessage(this.configuration.getMessage("import.finished").replaceAll("%world%", worldName));
    }

    public void loadWorld(final CommandSender commandSender, final String worldName) {
        final WorldProperties worldProperties = this.worldConfiguration.getWorldProperties(worldName);
        final WorldCreator worldCreator = new WorldCreator(worldName);

        commandSender.sendMessage(this.configuration.getMessage("load.starting").replaceAll("%world%", worldName));

        if (!worldProperties.getWorldType().isOverWorld()) {
            worldCreator.environment(worldProperties.getWorldType().getEnvironment());
        }

        switch (worldProperties.getWorldType()) {
            case VOID:
                worldCreator.generator(new VoidChunkGenerator());
                worldCreator.generateStructures(false);
                break;

            case FLAT:
                worldCreator.generator(new FlatChunkGenerator());
                worldCreator.generateStructures(false);
                break;
        }

        final World world = Bukkit.createWorld(worldCreator);
        world.setDifficulty(worldProperties.getDifficulty());
        world.setSpawnFlags(worldProperties.isSpawnMonsters(), worldProperties.isSpawnAnimals());

        commandSender.sendMessage(this.configuration.getMessage("load.finished").replaceAll("%world%", worldName));

        worldProperties.setLoaded(true);
        this.worldProperties.put(worldName, worldProperties);
    }

    public void unloadWorld(final CommandSender commandSender, final String worldName) {
        final World world = Bukkit.getWorld(worldName);

        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getWorld().getName().equalsIgnoreCase(worldName)) {
                continue;
            }
            player.sendMessage(this.configuration.getMessage("unload.chunk-teleport").replaceAll("%world%", worldName));
            player.teleport(Bukkit.getWorld(MultiWorldPlugin.getInstance().getConfiguration().getString("defaults.world")).getSpawnLocation());
        }
        commandSender.sendMessage(this.configuration.getMessage("unload.chunk-starting").replaceAll("%world%", worldName));
        Arrays.stream(world.getLoadedChunks()).forEach(Chunk::unload);
        commandSender.sendMessage(this.configuration.getMessage("unload.chunk-finished").replaceAll("%world%", worldName));

        commandSender.sendMessage(this.configuration.getMessage("unload.starting").replaceAll("%world%", worldName));
        Bukkit.unloadWorld(world.getName(), true);
        commandSender.sendMessage(this.configuration.getMessage("unload.finished").replaceAll("%world%", worldName));
        this.worldProperties.get(worldName).setLoaded(false);
    }

    public void teleportWorld(final CommandSender commandSender, final Player target, final Location teleportLocation) {
        final WorldUser worldUser = this.worldUserService.getUsers().get(target.getUniqueId());
        final WorldUserProperties userProperties = worldUser.getProperties();
        final WorldProperties worldProperties = this.worldProperties.get(teleportLocation.getWorld().getName());

        userProperties.setLastWorld(target.getWorld().getName());
        userProperties.setLastWorldLocation(target.getLocation());

        target.teleport(teleportLocation);
        commandSender.sendMessage(this.configuration.getMessage("teleport.message").replaceAll("%player%", target.getName()).replaceAll("%world%", teleportLocation.getWorld().getName()));

        worldProperties.setLastWorldInteraction(System.currentTimeMillis());
        this.worldConfiguration.updateLastWorldInteraction(worldProperties);
    }

    public void teleportWorld(final Player target, final Location teleportLocation) {
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
