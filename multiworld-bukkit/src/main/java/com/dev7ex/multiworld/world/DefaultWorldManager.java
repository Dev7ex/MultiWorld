package com.dev7ex.multiworld.world;

import com.dev7ex.common.bukkit.BukkitCommon;
import com.dev7ex.common.collect.map.ParsedMap;
import com.dev7ex.common.io.Files;
import com.dev7ex.multiworld.MultiWorldConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.event.world.WorldCloneEvent;
import com.dev7ex.multiworld.api.bukkit.event.world.WorldCreateEvent;
import com.dev7ex.multiworld.api.bukkit.event.world.WorldDeleteEvent;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldManager;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldProvider;
import com.dev7ex.multiworld.api.bukkit.world.generator.FlatWorldGenerator;
import com.dev7ex.multiworld.api.bukkit.world.generator.VoidWorldGenerator;
import com.dev7ex.multiworld.api.bukkit.world.generator.WaterWorldGenerator;
import com.dev7ex.multiworld.api.world.WorldDefaultProperty;
import com.dev7ex.multiworld.api.world.WorldType;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public class DefaultWorldManager implements BukkitWorldManager {

    private final DefaultWorldConfiguration configuration;
    private final MultiWorldConfiguration pluginConfiguration;

    public DefaultWorldManager(@NotNull final DefaultWorldConfiguration configuration,
                               @NotNull final MultiWorldConfiguration pluginConfiguration) {
        this.configuration = configuration;
        this.pluginConfiguration = pluginConfiguration;
    }

    @Override
    public void cloneWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final String clonedName) {
        final File sourceFolder = new File(Bukkit.getWorldContainer(), name);
        final File destinationFolder = new File(Bukkit.getWorldContainer(), clonedName);
        final CommandSender commandSender = BukkitCommon.getCommandSender(creatorName);
        final BukkitWorldHolder worldHolder = this.getProvider().getWorldHolder(name).orElseThrow();

        final WorldCloneEvent event = new WorldCloneEvent(worldHolder, commandSender, clonedName, sourceFolder, destinationFolder);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.clone.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        try {
            final File sessionFile = new File(sourceFolder, "session.lock");

            if (sessionFile.exists()) {
                final boolean deleted = sessionFile.delete();
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
            commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.clone.finished")
                    .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                    .replaceAll("%world_name%", name));

        } catch (final IOException exception) {
            commandSender.sendMessage("§cAn error has occurred. View the logs");
            exception.printStackTrace();
        }
    }

    @Override
    public void createBackup(@NotNull final String creatorName, @NotNull final String name) {
        final CommandSender commandSender = BukkitCommon.getCommandSender(creatorName);
        final File sourceFolder = new File(Bukkit.getWorldContainer(), name);
        final File destinationFolder = new File(MultiWorldPlugin.getInstance().getSubFolder("backup"),
                name + "-" + new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss").format(Calendar.getInstance().getTime()));

        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.backup.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

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
            commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.backup.finished")
                    .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                    .replaceAll("%world_name%", name));

        } catch (final IOException exception) {
            commandSender.sendMessage("§cAn error has occurred. View the logs");
            exception.printStackTrace();
        }
    }

    @Override
    public void createWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final WorldType type) {
        final WorldCreator worldCreator = new WorldCreator(name);
        final CommandSender commandSender = BukkitCommon.getCommandSender(creatorName);

        switch (type) {
            case FLAT:
                worldCreator.generator(new FlatWorldGenerator());
                worldCreator.generateStructures(false);
                break;

            case NETHER:
                worldCreator.environment(World.Environment.NETHER);
                break;

            case END:
                worldCreator.environment(World.Environment.THE_END);
                break;

            case WATER:
                worldCreator.generator(new WaterWorldGenerator());
                worldCreator.generateStructures(false);
                break;

            case VOID:
                worldCreator.generator(new VoidWorldGenerator());
                worldCreator.generateStructures(false);
                break;

            default:
                worldCreator.type(org.bukkit.WorldType.NORMAL);
                break;
        }
        final ParsedMap<WorldDefaultProperty, Object> defaultProperties = this.pluginConfiguration.getDefaultProperties();

        final BukkitWorldHolder worldHolder = BukkitWorldHolder.builder()
                .setName(name)
                .setCreatorName(commandSender.getName())
                .setCreationTimeStamp(System.currentTimeMillis())
                .setType(type)
                .setGameMode(GameMode.valueOf(defaultProperties.getString(WorldDefaultProperty.GAME_MODE)))
                .setDifficulty(Difficulty.valueOf(defaultProperties.getString(WorldDefaultProperty.DIFFICULTY)))
                .setPvpEnabled(defaultProperties.getBoolean(WorldDefaultProperty.PVP_ENABLED))
                .setLoaded(false)
                .setSpawnAnimals(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ANIMALS))
                .setSpawnMonsters(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_MONSTERS))
                .setSpawnEntities(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ENTITIES))
                .setEndPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.END_PORTAL_ACCESSIBLE))
                .setNetherPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.NETHER_PORTAL_ACCESSIBLE))
                .setEndWorldName(defaultProperties.getString(WorldDefaultProperty.END_WORLD))
                .setNetherWorldName(defaultProperties.getString(WorldDefaultProperty.NETHER_WORLD))
                .setNormalWorldName(defaultProperties.getString(WorldDefaultProperty.NORMAL_WORLD))
                .setWhitelist(new ArrayList<>())
                .setWhitelistEnabled(defaultProperties.getBoolean(WorldDefaultProperty.WHITELIST_ENABLED))
                .build();

        final WorldCreateEvent event = new WorldCreateEvent(worldHolder, commandSender);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.create.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        final World world = worldCreator.createWorld();
        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.create.finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        world.setDifficulty(worldHolder.getDifficulty());
        world.setSpawnFlags(worldHolder.isSpawnMonsters(), worldHolder.isSpawnAnimals());

        worldHolder.setLoaded(true);
        this.configuration.add(worldHolder);
        this.getProvider().register(worldHolder);
    }

    @Override
    public void createWorld(@NotNull final String creatorName, @NotNull final String name, final long seed) {
        final WorldCreator worldCreator = new WorldCreator(name);
        final CommandSender commandSender = BukkitCommon.getCommandSender(creatorName);

        worldCreator.seed(seed);

        final ParsedMap<WorldDefaultProperty, Object> defaultProperties = this.pluginConfiguration.getDefaultProperties();

        final BukkitWorldHolder worldHolder = BukkitWorldHolder.builder()
                .setName(name)
                .setCreatorName(commandSender.getName())
                .setCreationTimeStamp(System.currentTimeMillis())
                .setType(WorldType.NORMAL)
                .setGameMode(GameMode.valueOf(defaultProperties.getString(WorldDefaultProperty.GAME_MODE)))
                .setDifficulty(Difficulty.valueOf(defaultProperties.getString(WorldDefaultProperty.DIFFICULTY)))
                .setPvpEnabled(defaultProperties.getBoolean(WorldDefaultProperty.PVP_ENABLED))
                .setLoaded(false)
                .setSpawnAnimals(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ANIMALS))
                .setSpawnMonsters(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_MONSTERS))
                .setSpawnEntities(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ENTITIES))
                .setEndPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.END_PORTAL_ACCESSIBLE))
                .setNetherPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.NETHER_PORTAL_ACCESSIBLE))
                .setEndWorldName(defaultProperties.getString(WorldDefaultProperty.END_WORLD))
                .setNetherWorldName(defaultProperties.getString(WorldDefaultProperty.NETHER_WORLD))
                .setNormalWorldName(defaultProperties.getString(WorldDefaultProperty.NORMAL_WORLD))
                .setReceiveAchievements(defaultProperties.getBoolean(WorldDefaultProperty.RECEIVE_ACHIEVEMENTS))
                .setWhitelist(new ArrayList<>())
                .setWhitelistEnabled(defaultProperties.getBoolean(WorldDefaultProperty.WHITELIST_ENABLED))
                .build();

        final WorldCreateEvent event = new WorldCreateEvent(worldHolder, commandSender);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.create.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));
        final World world = worldCreator.createWorld();
        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.create.finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        world.setDifficulty(worldHolder.getDifficulty());
        world.setSpawnFlags(worldHolder.isSpawnMonsters(), worldHolder.isSpawnAnimals());

        worldHolder.setLoaded(true);
        this.configuration.add(worldHolder);
        this.getProvider().register(worldHolder);
    }

    @Override
    public void createWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final String generator) {
        final WorldCreator worldCreator = new WorldCreator(name);
        final CommandSender commandSender = BukkitCommon.getCommandSender(creatorName);

        worldCreator.generator(generator);

        final ParsedMap<WorldDefaultProperty, Object> defaultProperties = this.pluginConfiguration.getDefaultProperties();

        final BukkitWorldHolder worldHolder = BukkitWorldHolder.builder()
                .setName(name)
                .setCreatorName(commandSender.getName())
                .setCreationTimeStamp(System.currentTimeMillis())
                .setType(WorldType.NORMAL)
                .setGameMode(GameMode.valueOf(defaultProperties.getString(WorldDefaultProperty.GAME_MODE)))
                .setDifficulty(Difficulty.valueOf(defaultProperties.getString(WorldDefaultProperty.DIFFICULTY)))
                .setPvpEnabled(defaultProperties.getBoolean(WorldDefaultProperty.PVP_ENABLED))
                .setLoaded(false)
                .setSpawnAnimals(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ANIMALS))
                .setSpawnMonsters(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_MONSTERS))
                .setSpawnEntities(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ENTITIES))
                .setEndPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.END_PORTAL_ACCESSIBLE))
                .setNetherPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.NETHER_PORTAL_ACCESSIBLE))
                .setEndWorldName(defaultProperties.getString(WorldDefaultProperty.END_WORLD))
                .setNetherWorldName(defaultProperties.getString(WorldDefaultProperty.NETHER_WORLD))
                .setNormalWorldName(defaultProperties.getString(WorldDefaultProperty.NORMAL_WORLD))
                .setReceiveAchievements(defaultProperties.getBoolean(WorldDefaultProperty.RECEIVE_ACHIEVEMENTS))
                .setWhitelist(new ArrayList<>())
                .setWhitelistEnabled(defaultProperties.getBoolean(WorldDefaultProperty.WHITELIST_ENABLED))
                .build();

        final WorldCreateEvent event = new WorldCreateEvent(worldHolder, commandSender);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.create.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));
        final World world = worldCreator.createWorld();
        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.create.finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        world.setDifficulty(worldHolder.getDifficulty());
        world.setSpawnFlags(worldHolder.isSpawnMonsters(), worldHolder.isSpawnAnimals());

        worldHolder.setLoaded(true);
        this.configuration.add(worldHolder);
        this.getProvider().register(worldHolder);
    }

    @Override
    public void deleteWorld(@NotNull final String creatorName, @NotNull final String name) {
        final CommandSender commandSender = BukkitCommon.getCommandSender(creatorName);
        final BukkitWorldHolder worldHolder = this.getProvider().getWorldHolder(name).orElseThrow();

        final WorldDeleteEvent event = new WorldDeleteEvent(worldHolder, commandSender);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.delete.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));


        if (worldHolder.isLoaded()) {
            this.unloadWorld(creatorName, name);
        }

        try {
            FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer() + File.separator + name));

        } catch (final IOException exception) {
            commandSender.sendMessage("§cAn error has occurred. View the logs");
            exception.printStackTrace();
            return;
        }
        this.configuration.remove(name);
        this.getProvider().unregister(name);
        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.delete.finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));
    }

    @Override
    public void importWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final WorldType type) {
        final CommandSender commandSender = BukkitCommon.getCommandSender(creatorName);

        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.import.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        final ParsedMap<WorldDefaultProperty, Object> defaultProperties = this.pluginConfiguration.getDefaultProperties();

        final BukkitWorldHolder worldHolder = BukkitWorldHolder.builder()
                .setName(name)
                .setCreatorName(commandSender.getName())
                .setCreationTimeStamp(System.currentTimeMillis())
                .setType(type)
                .setGameMode(GameMode.valueOf(defaultProperties.getString(WorldDefaultProperty.GAME_MODE)))
                .setDifficulty(Difficulty.valueOf(defaultProperties.getString(WorldDefaultProperty.DIFFICULTY)))
                .setPvpEnabled(defaultProperties.getBoolean(WorldDefaultProperty.PVP_ENABLED))
                .setLoaded(false)
                .setSpawnAnimals(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ANIMALS))
                .setSpawnMonsters(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_MONSTERS))
                .setSpawnEntities(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ENTITIES))
                .setEndPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.END_PORTAL_ACCESSIBLE))
                .setNetherPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.NETHER_PORTAL_ACCESSIBLE))
                .setEndWorldName(defaultProperties.getString(WorldDefaultProperty.END_WORLD))
                .setNetherWorldName(defaultProperties.getString(WorldDefaultProperty.NETHER_WORLD))
                .setNormalWorldName(defaultProperties.getString(WorldDefaultProperty.NORMAL_WORLD))
                .setReceiveAchievements(defaultProperties.getBoolean(WorldDefaultProperty.RECEIVE_ACHIEVEMENTS))
                .setWhitelist(Collections.emptyList())
                .setWhitelistEnabled(defaultProperties.getBoolean(WorldDefaultProperty.WHITELIST_ENABLED))
                .build();

        this.configuration.add(worldHolder);
        this.getProvider().register(worldHolder);
        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.import.finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));
    }

    @Override
    public void loadWorld(@NotNull final String creatorName, @NotNull final String name) {
        final CommandSender commandSender = BukkitCommon.getCommandSender(creatorName);
        final BukkitWorldHolder worldHolder = this.getProvider().getWorldHolder(name).orElseThrow();
        final WorldCreator worldCreator = new WorldCreator(name);

        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.load.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        switch (worldHolder.getType()) {
            case VOID:
                worldCreator.generator(new VoidWorldGenerator());
                worldCreator.generateStructures(false);
                break;

            case FLAT:
                worldCreator.generator(new FlatWorldGenerator());
                worldCreator.generateStructures(false);
                break;

            case NETHER:
                worldCreator.environment(World.Environment.NETHER);
                break;

            case WATER:
                worldCreator.generator(new WaterWorldGenerator());
                worldCreator.generateStructures(false);
                break;

            case END:
                worldCreator.environment(World.Environment.THE_END);
                break;
        }

        final World world = Bukkit.createWorld(worldCreator);

        world.setDifficulty(worldHolder.getDifficulty());
        world.setSpawnFlags(worldHolder.isSpawnMonsters(), worldHolder.isSpawnAnimals());

        worldHolder.setLoaded(true);
        this.getProvider().register(worldHolder);

        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.load.finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));
    }

    @Override
    public void unloadWorld(@NotNull final String creatorName, @NotNull final String name) {
        final CommandSender commandSender = BukkitCommon.getCommandSender(creatorName);
        final World world = Objects.requireNonNull(Bukkit.getWorld(name));

        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getWorld().getName().equalsIgnoreCase(name)) {
                continue;
            }
            player.sendMessage(this.pluginConfiguration.getString("messages.commands.unload.chunk-teleport")
                    .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                    .replaceAll("%world_name%", name));
            player.teleport(Bukkit.getWorld(MultiWorldPlugin.getInstance()
                            .getConfiguration()
                            .getString("settings.defaults.normal-world"))
                    .getSpawnLocation());
        }

        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.unload.chunk-starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        Arrays.stream(world.getLoadedChunks()).forEach(Chunk::unload);

        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.unload.chunk-finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.unload.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        Bukkit.unloadWorld(world.getName(), true);
        this.getProvider().getWorldHolder(name).orElseThrow().setLoaded(false);

        commandSender.sendMessage(this.pluginConfiguration.getString("messages.commands.unload.finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));
    }

    @Override
    public BukkitWorldProvider getProvider() {
        return MultiWorldPlugin.getInstance().getWorldProvider();
    }

}