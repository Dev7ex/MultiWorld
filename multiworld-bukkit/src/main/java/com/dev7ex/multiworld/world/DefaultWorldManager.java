package com.dev7ex.multiworld.world;

import com.dev7ex.common.bukkit.BukkitCommon;
import com.dev7ex.common.collect.map.ParsedMap;
import com.dev7ex.common.io.file.Files;
import com.dev7ex.multiworld.MultiWorldConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.event.world.WorldCloneEvent;
import com.dev7ex.multiworld.api.bukkit.event.world.WorldCreateEvent;
import com.dev7ex.multiworld.api.bukkit.event.world.WorldDeleteEvent;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldEnvironment;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldManager;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldProvider;
import com.dev7ex.multiworld.api.bukkit.world.generator.defaults.FlatWorldGenerator;
import com.dev7ex.multiworld.api.bukkit.world.generator.defaults.VoidWorldGenerator;
import com.dev7ex.multiworld.api.world.WorldDefaultProperty;
import com.dev7ex.multiworld.api.world.WorldEnvironment;
import com.dev7ex.multiworld.api.world.WorldType;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;

/**
 * Manages world creation, deletion, cloning, and backup operations.
 * This class implements the BukkitWorldManager interface.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public class DefaultWorldManager implements BukkitWorldManager {

    private final DefaultWorldConfiguration configuration;
    private final MultiWorldConfiguration pluginConfiguration;
    private final DefaultTranslationProvider translationProvider;

    /**
     * Constructs a DefaultWorldManager with the specified configurations.
     *
     * @param configuration       The world configuration to use.
     * @param pluginConfiguration The plugin configuration to use.
     */
    public DefaultWorldManager(@NotNull final DefaultWorldConfiguration configuration,
                               @NotNull final MultiWorldConfiguration pluginConfiguration,
                               @NotNull final DefaultTranslationProvider translationProvider) {
        this.configuration = configuration;
        this.pluginConfiguration = pluginConfiguration;
        this.translationProvider = translationProvider;
    }

    /**
     * Clones an existing world to create a new one.
     *
     * @param creatorName The name of the creator initiating the operation.
     * @param name        The name of the world to clone.
     * @param clonedName  The name of the new cloned world.
     */
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
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.clone.starting")
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
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.clone.finished")
                    .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                    .replaceAll("%world_name%", name));

        } catch (final IOException exception) {
            commandSender.sendMessage("§cAn error has occurred. View the logs");
            exception.printStackTrace();
        }
    }

    /**
     * Creates a backup of the specified world.
     *
     * @param creatorName The name of the creator initiating the backup.
     * @param name        The name of the world to backup.
     */
    @Override
    public void createBackup(@NotNull final String creatorName, @NotNull final String name) {
        final CommandSender commandSender = BukkitCommon.getCommandSender(creatorName);
        final File sourceFolder = new File(Bukkit.getWorldContainer(), name);
        final File destinationFolder = new File(MultiWorldPlugin.getInstance().getSubFolder("backup"),
                name + "-" + new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss").format(Calendar.getInstance().getTime()));

        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.backup.starting")
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
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.backup.finished")
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
            case THE_END:
                worldCreator.environment(World.Environment.THE_END);
                break;

            case NETHER:
                worldCreator.environment(World.Environment.NETHER);
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
                .setAutoLoadEnabled(defaultProperties.getBoolean(WorldDefaultProperty.AUTO_LOAD_ENABLED))
                .setAutoUnloadEnabled(defaultProperties.getBoolean(WorldDefaultProperty.AUTO_UNLOAD_ENABLED))
                .setDifficulty(Difficulty.valueOf(defaultProperties.getString(WorldDefaultProperty.DIFFICULTY)))
                .setEndPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.END_PORTAL_ACCESSIBLE))
                .setGameMode(GameMode.valueOf(defaultProperties.getString(WorldDefaultProperty.GAME_MODE)))
                .setHungerEnabled(defaultProperties.getBoolean(WorldDefaultProperty.HUNGER_ENABLED))
                .setKeepSpawnInMemory(defaultProperties.getBoolean(WorldDefaultProperty.KEEP_SPAWN_IN_MEMORY))
                .setNetherPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.NETHER_PORTAL_ACCESSIBLE))
                .setPvpEnabled(defaultProperties.getBoolean(WorldDefaultProperty.PVP_ENABLED))
                .setReceiveAchievements(defaultProperties.getBoolean(WorldDefaultProperty.RECEIVE_ACHIEVEMENTS))
                .setRedstoneEnabled(defaultProperties.getBoolean(WorldDefaultProperty.REDSTONE_ENABLED))
                .setSpawnAnimals(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ANIMALS))
                .setSpawnMonsters(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_MONSTERS))
                .setSpawnEntities(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ENTITIES))
                .setWeatherEnabled(defaultProperties.getBoolean(WorldDefaultProperty.WEATHER_ENABLED))
                .setEnvironment(BukkitWorldEnvironment.from(WorldEnvironment.fromType(type)))
                .setGenerator("NONE")
                .setType(type)
                .setEndWorldName(defaultProperties.getString(WorldDefaultProperty.END_WORLD))
                .setNetherWorldName(defaultProperties.getString(WorldDefaultProperty.NETHER_WORLD))
                .setNormalWorldName(defaultProperties.getString(WorldDefaultProperty.NORMAL_WORLD))
                .setWhitelist(Collections.emptyList())
                .setWhitelistEnabled(defaultProperties.getBoolean(WorldDefaultProperty.WHITELIST_ENABLED))
                .build();

        final WorldCreateEvent event = new WorldCreateEvent(worldHolder, commandSender);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.create.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        final World world = worldCreator.createWorld();
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.create.finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        world.setDifficulty(worldHolder.getDifficulty());
        world.setSpawnFlags(worldHolder.isSpawnMonsters(), worldHolder.isSpawnAnimals());

        worldHolder.setLoadTimeStamp(System.currentTimeMillis());
        worldHolder.setLoaded(true);

        this.configuration.add(worldHolder);
        this.getProvider().register(worldHolder);
    }

    @Override
    public void createWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final WorldEnvironment environment, final long seed) {
        final WorldCreator worldCreator = new WorldCreator(name);
        final CommandSender commandSender = BukkitCommon.getCommandSender(creatorName);

        worldCreator.environment(BukkitWorldEnvironment.from(environment));
        worldCreator.seed(seed);

        final ParsedMap<WorldDefaultProperty, Object> defaultProperties = this.pluginConfiguration.getDefaultProperties();

        final BukkitWorldHolder worldHolder = BukkitWorldHolder.builder()
                .setName(name)
                .setCreatorName(commandSender.getName())
                .setCreationTimeStamp(System.currentTimeMillis())
                .setAutoLoadEnabled(defaultProperties.getBoolean(WorldDefaultProperty.AUTO_LOAD_ENABLED))
                .setAutoUnloadEnabled(defaultProperties.getBoolean(WorldDefaultProperty.AUTO_UNLOAD_ENABLED))
                .setDifficulty(Difficulty.valueOf(defaultProperties.getString(WorldDefaultProperty.DIFFICULTY)))
                .setEndPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.END_PORTAL_ACCESSIBLE))
                .setGameMode(GameMode.valueOf(defaultProperties.getString(WorldDefaultProperty.GAME_MODE)))
                .setHungerEnabled(defaultProperties.getBoolean(WorldDefaultProperty.HUNGER_ENABLED))
                .setKeepSpawnInMemory(defaultProperties.getBoolean(WorldDefaultProperty.KEEP_SPAWN_IN_MEMORY))
                .setNetherPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.NETHER_PORTAL_ACCESSIBLE))
                .setPvpEnabled(defaultProperties.getBoolean(WorldDefaultProperty.PVP_ENABLED))
                .setReceiveAchievements(defaultProperties.getBoolean(WorldDefaultProperty.RECEIVE_ACHIEVEMENTS))
                .setRedstoneEnabled(defaultProperties.getBoolean(WorldDefaultProperty.REDSTONE_ENABLED))
                .setSpawnAnimals(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ANIMALS))
                .setSpawnMonsters(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_MONSTERS))
                .setSpawnEntities(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ENTITIES))
                .setWeatherEnabled(defaultProperties.getBoolean(WorldDefaultProperty.WEATHER_ENABLED))
                .setEnvironment(BukkitWorldEnvironment.from(WorldEnvironment.fromType(WorldType.CUSTOM)))
                .setGenerator("NONE")
                .setType(WorldType.CUSTOM)
                .setEndWorldName(defaultProperties.getString(WorldDefaultProperty.END_WORLD))
                .setNetherWorldName(defaultProperties.getString(WorldDefaultProperty.NETHER_WORLD))
                .setNormalWorldName(defaultProperties.getString(WorldDefaultProperty.NORMAL_WORLD))
                .setWhitelist(Collections.emptyList())
                .setWhitelistEnabled(defaultProperties.getBoolean(WorldDefaultProperty.WHITELIST_ENABLED))
                .build();

        final WorldCreateEvent event = new WorldCreateEvent(worldHolder, commandSender);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.create.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));
        final World world = worldCreator.createWorld();
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.create.finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        world.setDifficulty(worldHolder.getDifficulty());
        world.setSpawnFlags(worldHolder.isSpawnMonsters(), worldHolder.isSpawnAnimals());

        worldHolder.setLoadTimeStamp(System.currentTimeMillis());
        worldHolder.setLoaded(true);

        this.configuration.add(worldHolder);
        this.getProvider().register(worldHolder);
    }

    @Override
    public void createWorld(@NotNull final String creatorName, @NotNull final String name,
                            @NotNull final WorldEnvironment environment, @NotNull final String generator) {
        final WorldCreator worldCreator = new WorldCreator(name);
        final ParsedMap<WorldDefaultProperty, Object> defaultProperties = this.pluginConfiguration.getDefaultProperties();
        final CommandSender commandSender = BukkitCommon.getCommandSender(creatorName);

        if (BukkitWorldEnvironment.from(environment) != World.Environment.CUSTOM) {
            worldCreator.environment(BukkitWorldEnvironment.from(environment));
        }

        switch (generator) {
            case "FlatWorldGenerator":
                worldCreator.generator(new FlatWorldGenerator(MultiWorldPlugin.getInstance()));
                break;

            case "VoidWorldGenerator":
                worldCreator.generator(new VoidWorldGenerator(MultiWorldPlugin.getInstance()));
                break;

            default:
                worldCreator.generator(generator);
                break;
        }

        final BukkitWorldHolder worldHolder = BukkitWorldHolder.builder()
                .setName(name)
                .setCreatorName(commandSender.getName())
                .setCreationTimeStamp(System.currentTimeMillis())
                .setAutoLoadEnabled(defaultProperties.getBoolean(WorldDefaultProperty.AUTO_LOAD_ENABLED))
                .setAutoUnloadEnabled(defaultProperties.getBoolean(WorldDefaultProperty.AUTO_UNLOAD_ENABLED))
                .setDifficulty(Difficulty.valueOf(defaultProperties.getString(WorldDefaultProperty.DIFFICULTY)))
                .setEndPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.END_PORTAL_ACCESSIBLE))
                .setGameMode(GameMode.valueOf(defaultProperties.getString(WorldDefaultProperty.GAME_MODE)))
                .setHungerEnabled(defaultProperties.getBoolean(WorldDefaultProperty.HUNGER_ENABLED))
                .setKeepSpawnInMemory(defaultProperties.getBoolean(WorldDefaultProperty.KEEP_SPAWN_IN_MEMORY))
                .setNetherPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.NETHER_PORTAL_ACCESSIBLE))
                .setPvpEnabled(defaultProperties.getBoolean(WorldDefaultProperty.PVP_ENABLED))
                .setReceiveAchievements(defaultProperties.getBoolean(WorldDefaultProperty.RECEIVE_ACHIEVEMENTS))
                .setRedstoneEnabled(defaultProperties.getBoolean(WorldDefaultProperty.REDSTONE_ENABLED))
                .setSpawnAnimals(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ANIMALS))
                .setSpawnMonsters(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_MONSTERS))
                .setSpawnEntities(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ENTITIES))
                .setWeatherEnabled(defaultProperties.getBoolean(WorldDefaultProperty.WEATHER_ENABLED))
                .setEnvironment(BukkitWorldEnvironment.from(WorldEnvironment.fromType(WorldType.CUSTOM)))
                .setGenerator(generator)
                .setType(WorldType.CUSTOM)
                .setEndWorldName(defaultProperties.getString(WorldDefaultProperty.END_WORLD))
                .setNetherWorldName(defaultProperties.getString(WorldDefaultProperty.NETHER_WORLD))
                .setNormalWorldName(defaultProperties.getString(WorldDefaultProperty.NORMAL_WORLD))
                .setWhitelist(Collections.emptyList())
                .setWhitelistEnabled(defaultProperties.getBoolean(WorldDefaultProperty.WHITELIST_ENABLED))
                .build();

        final WorldCreateEvent event = new WorldCreateEvent(worldHolder, commandSender);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.create.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));
        final World world = worldCreator.createWorld();
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.create.finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        world.setDifficulty(worldHolder.getDifficulty());
        world.setSpawnFlags(worldHolder.isSpawnMonsters(), worldHolder.isSpawnAnimals());

        worldHolder.setLoadTimeStamp(System.currentTimeMillis());
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
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.delete.starting")
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
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.delete.finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));
    }

    @Override
    public void importWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final WorldEnvironment environment, @NotNull final String generator) {
        final CommandSender commandSender = BukkitCommon.getCommandSender(creatorName);
        final ParsedMap<WorldDefaultProperty, Object> defaultProperties = this.pluginConfiguration.getDefaultProperties();

        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.import.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        final BukkitWorldHolder worldHolder = BukkitWorldHolder.builder()
                .setName(name)
                .setCreatorName(commandSender.getName())
                .setCreationTimeStamp(System.currentTimeMillis())
                .setAutoLoadEnabled(defaultProperties.getBoolean(WorldDefaultProperty.AUTO_LOAD_ENABLED))
                .setAutoUnloadEnabled(defaultProperties.getBoolean(WorldDefaultProperty.AUTO_UNLOAD_ENABLED))
                .setDifficulty(Difficulty.valueOf(defaultProperties.getString(WorldDefaultProperty.DIFFICULTY)))
                .setEndPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.END_PORTAL_ACCESSIBLE))
                .setGameMode(GameMode.valueOf(defaultProperties.getString(WorldDefaultProperty.GAME_MODE)))
                .setHungerEnabled(defaultProperties.getBoolean(WorldDefaultProperty.HUNGER_ENABLED))
                .setKeepSpawnInMemory(defaultProperties.getBoolean(WorldDefaultProperty.KEEP_SPAWN_IN_MEMORY))
                .setNetherPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.NETHER_PORTAL_ACCESSIBLE))
                .setPvpEnabled(defaultProperties.getBoolean(WorldDefaultProperty.PVP_ENABLED))
                .setReceiveAchievements(defaultProperties.getBoolean(WorldDefaultProperty.RECEIVE_ACHIEVEMENTS))
                .setRedstoneEnabled(defaultProperties.getBoolean(WorldDefaultProperty.REDSTONE_ENABLED))
                .setSpawnAnimals(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ANIMALS))
                .setSpawnMonsters(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_MONSTERS))
                .setSpawnEntities(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ENTITIES))
                .setWeatherEnabled(defaultProperties.getBoolean(WorldDefaultProperty.WEATHER_ENABLED))
                .setEnvironment(BukkitWorldEnvironment.from(WorldEnvironment.fromType(WorldType.CUSTOM)))
                .setGenerator(generator)
                .setType(WorldType.CUSTOM)
                .setEndWorldName(defaultProperties.getString(WorldDefaultProperty.END_WORLD))
                .setNetherWorldName(defaultProperties.getString(WorldDefaultProperty.NETHER_WORLD))
                .setNormalWorldName(defaultProperties.getString(WorldDefaultProperty.NORMAL_WORLD))
                .setWhitelist(Collections.emptyList())
                .setWhitelistEnabled(defaultProperties.getBoolean(WorldDefaultProperty.WHITELIST_ENABLED))
                .build();

        this.configuration.add(worldHolder);
        this.getProvider().register(worldHolder);
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.import.finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));
    }

    @Override
    public void importWorld(@NotNull final String creatorName, @NotNull final String name, @NotNull final WorldEnvironment environment, @NotNull final WorldType type) {
        final CommandSender commandSender = BukkitCommon.getCommandSender(creatorName);

        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.import.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        final ParsedMap<WorldDefaultProperty, Object> defaultProperties = this.pluginConfiguration.getDefaultProperties();

        final BukkitWorldHolder worldHolder = BukkitWorldHolder.builder()
                .setName(name)
                .setCreatorName(commandSender.getName())
                .setCreationTimeStamp(System.currentTimeMillis())
                .setAutoLoadEnabled(defaultProperties.getBoolean(WorldDefaultProperty.AUTO_LOAD_ENABLED))
                .setAutoUnloadEnabled(defaultProperties.getBoolean(WorldDefaultProperty.AUTO_UNLOAD_ENABLED))
                .setDifficulty(Difficulty.valueOf(defaultProperties.getString(WorldDefaultProperty.DIFFICULTY)))
                .setEndPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.END_PORTAL_ACCESSIBLE))
                .setGameMode(GameMode.valueOf(defaultProperties.getString(WorldDefaultProperty.GAME_MODE)))
                .setHungerEnabled(defaultProperties.getBoolean(WorldDefaultProperty.HUNGER_ENABLED))
                .setKeepSpawnInMemory(defaultProperties.getBoolean(WorldDefaultProperty.KEEP_SPAWN_IN_MEMORY))
                .setNetherPortalAccessible(defaultProperties.getBoolean(WorldDefaultProperty.NETHER_PORTAL_ACCESSIBLE))
                .setPvpEnabled(defaultProperties.getBoolean(WorldDefaultProperty.PVP_ENABLED))
                .setReceiveAchievements(defaultProperties.getBoolean(WorldDefaultProperty.RECEIVE_ACHIEVEMENTS))
                .setRedstoneEnabled(defaultProperties.getBoolean(WorldDefaultProperty.REDSTONE_ENABLED))
                .setSpawnAnimals(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ANIMALS))
                .setSpawnMonsters(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_MONSTERS))
                .setSpawnEntities(defaultProperties.getBoolean(WorldDefaultProperty.SPAWN_ENTITIES))
                .setWeatherEnabled(defaultProperties.getBoolean(WorldDefaultProperty.WEATHER_ENABLED))
                .setEnvironment(BukkitWorldEnvironment.from(WorldEnvironment.fromType(type)))
                .setGenerator("NONE")
                .setType(type)
                .setEndWorldName(defaultProperties.getString(WorldDefaultProperty.END_WORLD))
                .setNetherWorldName(defaultProperties.getString(WorldDefaultProperty.NETHER_WORLD))
                .setNormalWorldName(defaultProperties.getString(WorldDefaultProperty.NORMAL_WORLD))
                .setWhitelist(Collections.emptyList())
                .setWhitelistEnabled(defaultProperties.getBoolean(WorldDefaultProperty.WHITELIST_ENABLED))
                .build();

        this.configuration.add(worldHolder);
        this.getProvider().register(worldHolder);
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.import.finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));
    }

    @Override
    public void loadWorld(@NotNull final String creatorName, @NotNull final String name) {
        final CommandSender commandSender = BukkitCommon.getCommandSender(creatorName);
        final BukkitWorldHolder worldHolder = this.getProvider().getWorldHolder(name).orElseThrow();
        final File worldFolder = new File(Bukkit.getWorldContainer(), worldHolder.getName());
        final WorldCreator worldCreator = new WorldCreator(name);

        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.load.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));


        if ((worldHolder.getGenerator() != null) && (!worldHolder.getGenerator().equalsIgnoreCase("NONE"))) {
            switch (worldHolder.getGenerator()) {
                case "FlatWorldGenerator":
                    worldCreator.generator(new FlatWorldGenerator(MultiWorldPlugin.getInstance()));
                    break;

                case "VoidWorldGenerator":
                    worldCreator.generator(new VoidWorldGenerator(MultiWorldPlugin.getInstance()));
                    break;

                default:
                    worldCreator.generator(worldHolder.getGenerator());
                    break;
            }
        }

        if (!MultiWorldPlugin.getInstance().getWorldGeneratorProvider().isRegistered(worldHolder.getGenerator())
                && (!worldHolder.getGenerator().equalsIgnoreCase("NONE"))) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.load.error-missing-generator")
                    .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                    .replaceAll("%world_name%", name)
                    .replaceAll("%generator_name%", worldHolder.getGenerator()));
            return;
        }

        switch (worldHolder.getType()) {
            case THE_END:
                worldCreator.environment(World.Environment.THE_END);
                break;

            case NETHER:
                worldCreator.environment(World.Environment.NETHER);
                break;

            case NORMAL: case CUSTOM:
                worldCreator.environment(World.Environment.NORMAL);
                break;
        }
        final World world = Bukkit.createWorld(worldCreator);

        Objects.requireNonNull(world).setDifficulty(worldHolder.getDifficulty());
        world.setSpawnFlags(worldHolder.isSpawnMonsters(), worldHolder.isSpawnAnimals());

        worldHolder.setLoadTimeStamp(System.currentTimeMillis());
        worldHolder.setLoaded(true);
        this.getProvider().register(worldHolder);

        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.load.finished")
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
            player.sendMessage(this.translationProvider.getMessage(player, "commands.world.unload.chunk-teleport")
                    .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                    .replaceAll("%world_name%", name)
                    .replaceAll("%unloader_name%", creatorName));

            player.teleport(Objects.requireNonNull(Bukkit.getWorld(MultiWorldPlugin.getInstance()
                            .getConfiguration()
                            .getString("settings.defaults.normal-world")))
                    .getSpawnLocation());
        }

        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.unload.chunk-starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        Arrays.stream(world.getLoadedChunks()).forEach(Chunk::unload);

        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.unload.chunk-finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.unload.starting")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));

        Bukkit.unloadWorld(world.getName(), true);
        this.getProvider().getWorldHolder(name).orElseThrow().setLoaded(false);

        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.unload.finished")
                .replaceAll("%prefix%", this.pluginConfiguration.getPrefix())
                .replaceAll("%world_name%", name));
    }

    @Override
    public BukkitWorldProvider getProvider() {
        return MultiWorldPlugin.getInstance().getWorldProvider();
    }

}