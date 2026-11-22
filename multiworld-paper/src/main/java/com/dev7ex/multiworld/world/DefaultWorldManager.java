package com.dev7ex.multiworld.world;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.event.world.WorldCreateEvent;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.util.BukkitUtil;
import com.dev7ex.multiworld.api.world.WorldBuilder;
import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.WorldDirectory;
import com.dev7ex.multiworld.api.world.WorldEnvironment;
import com.dev7ex.multiworld.api.world.WorldManager;
import com.dev7ex.multiworld.api.world.WorldType;
import com.dev7ex.multiworld.api.world.group.WorldGroup;
import com.dev7ex.multiworld.api.world.result.WorldLifecycleResult;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultWorldManager implements WorldManager {

    private static final String UNKNOWN_GENERATOR = "Unknown";
    private static final String LEVEL_FILE = "level.dat";
    private static final String PREVIOUS_SUFFIX = "previous";
    private static final String DIMENSIONS_DIRECTORY = "dimensions";
    private static final Pattern WORLD_NAME_PATTERN = Pattern.compile("^[A-Za-z0-9_-]{1,64}$");
    private static final String UNKNOWN_CREATOR = "Unknown";
    private static final Pattern LEFTOVER_PATTERN = Pattern.compile("^(.+)\\.(previous|restoring|deleted)-\\d{13}$");
    private static final Set<NamespacedKey> PROTECTED_WORLD_KEYS = Set.of(
            NamespacedKey.minecraft("overworld"),
            NamespacedKey.minecraft("the_nether"),
            NamespacedKey.minecraft("the_end"));

    private final MultiWorldApi multiWorldApi;

    public DefaultWorldManager(@NotNull final MultiWorldApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }

    @Override
    public @Nullable WorldContext create(@NotNull final Audience audience, @NotNull final WorldCreator worldCreator) {
        BukkitUtil.checkPrimaryThread();

        final String worldName = worldCreator.name();

        if (this.multiWorldApi.getScheduler().isRegionized()) {
            this.multiWorldApi.getLogger().warning(String.format("Can't create World %s: Folia doesn't support creating Worlds while running", worldName));
            this.sendMessage(audience, WorldLocalizationEntry.MESSAGE_WORLD_RUNTIME_UNSUPPORTED, worldName);
            return null;
        }

        if (!DefaultWorldManager.WORLD_NAME_PATTERN.matcher(worldName).matches()) {
            this.sendMessage(audience, WorldLocalizationEntry.MESSAGE_WORLD_NAME_INVALID, worldName);
            return null;
        }

        if ((Bukkit.getWorld(worldCreator.key()) != null) || (Bukkit.getWorld(worldName) != null)
                || (this.multiWorldApi.getWorldRegistry().find(worldCreator.key()).isPresent())
                || (this.multiWorldApi.getWorldRegistry().find(worldName).isPresent())) {
            this.sendMessage(audience, WorldLocalizationEntry.MESSAGE_WORLD_ALREADY_EXISTS, worldName);
            return null;
        }
        final WorldGroup defaultGroup = this.multiWorldApi.getGroupRegistry().getDefaultGroup();
        final WorldContext context = DefaultWorldContext.builder()
                .setKey(worldCreator.key())
                .setName(worldName)
                .setDisplayName(worldName)
                .setColor(NamedTextColor.GRAY)
                .setIcon(Material.GRASS_BLOCK)
                .setCreatorName(audience.get(Identity.NAME).orElse(DefaultWorldManager.UNKNOWN_CREATOR))
                .setRegistrationTimestamp(System.currentTimeMillis())
                .setGroupId(defaultGroup.getId())
                .setGroup(defaultGroup)
                .setEnvironment(WorldEnvironment.ofBukkit(worldCreator.environment()))
                .setType(WorldType.ofBukkit(worldCreator.type()))
                .setSeed(worldCreator.seed())
                .build();
        final File directory = this.multiWorldApi.getWorldResolver().resolveDirectory(context);

        if (directory.exists()) {
            this.sendMessage(audience, WorldLocalizationEntry.MESSAGE_WORLD_DIRECTORY_EXISTS, worldName);
            return null;
        }
        this.sendMessage(audience, WorldLocalizationEntry.COMMAND_WORLD_CREATE_STARTING, worldName);

        final WorldCreateEvent event = new WorldCreateEvent(context, audience);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            if (event.isNotifyOnCancel()) {
                this.sendMessage(audience, WorldLocalizationEntry.COMMAND_WORLD_CREATE_CANCELLED, worldName);
            }
            return null;
        }
        World world = null;

        try {
            world = worldCreator.createWorld();

            if (world == null) {
                throw new IllegalStateException("The server returned no World");
            }
            context.setKey(world.getKey());
            context.setName(world.getName());
            context.setEnvironment(WorldEnvironment.ofBukkit(world.getEnvironment()));
            context.setSeed(world.getSeed());
            context.setDirectory(new WorldDirectory(world.getWorldFolder()));

            if (this.multiWorldApi.getWorldRepository().exists(context)) {
                this.multiWorldApi.getWorldRepository().delete(context);
            }
            this.multiWorldApi.getWorldRepository().insert(context);
            this.multiWorldApi.getWorldRegistry().register(context);

        } catch (final RuntimeException | LinkageError exception) {
            this.multiWorldApi.getLogger().log(Level.SEVERE, String.format("Can't create World %s", worldName), exception);
            this.sendMessage(audience, WorldLocalizationEntry.COMMAND_WORLD_CREATE_FAILED, worldName);

            try {
                this.rollbackCreate(context, world, directory);

            } catch (final RuntimeException rollbackException) {
                this.multiWorldApi.getLogger().log(Level.SEVERE, String.format("Can't roll back the failed World %s", worldName), rollbackException);
            }
            return null;
        }
        this.sendMessage(audience, WorldLocalizationEntry.COMMAND_WORLD_CREATE_FINISHING, worldName);
        return context;
    }

    @Override
    public @NotNull WorldLifecycleResult load(@NotNull final Audience audience, @NotNull final NamespacedKey key) {
        BukkitUtil.checkPrimaryThread();

        if (Bukkit.getWorld(key) != null) {
            return WorldLifecycleResult.ALREADY_LOADED;
        }

        if (this.multiWorldApi.getScheduler().isRegionized()) {
            this.multiWorldApi.getLogger().warning(String.format("Can't load World %s: Folia doesn't support loading Worlds while running", key));
            return WorldLifecycleResult.UNSUPPORTED;
        }
        final Optional<WorldContext> optionalContext = this.multiWorldApi.getWorldRegistry().find(key);

        if (optionalContext.isEmpty()) {
            this.multiWorldApi.getLogger().warning(String.format("Can't load World %s: not registered", key));
            return WorldLifecycleResult.FAILED;
        }
        final WorldContext context = optionalContext.get();

        if (this.multiWorldApi.getBackupRegistry().isLocked(context)) {
            this.multiWorldApi.getLogger().warning(String.format("Can't load World %s: a backup operation is running", context.getName()));
            return WorldLifecycleResult.FAILED;
        }

        final File directory = this.multiWorldApi.getWorldResolver().resolveDirectory(context);

        if (!directory.isDirectory()) {
            this.multiWorldApi.getLogger().warning(String.format("Can't load World %s: directory not found", context.getName()));
            return WorldLifecycleResult.FAILED;
        }

        if ((!this.multiWorldApi.getWorldResolver().isDimensionLayout()) && (!new File(directory, DefaultWorldManager.LEVEL_FILE).isFile())) {
            this.multiWorldApi.getLogger().warning(String.format("Can't load World %s: %s not found", context.getName(), DefaultWorldManager.LEVEL_FILE));
            return WorldLifecycleResult.FAILED;
        }

        try {
            final WorldBuilder worldBuilder = (this.multiWorldApi.getWorldResolver().isDimensionLayout() ? new WorldBuilder(key) : new WorldBuilder(context.getName(), key))
                    .environment(context.getEnvironment())
                    .type(context.getType())
                    .seed(context.getSeed());

            final String generatorName = context.getGeneratorName();

            if ((generatorName != null) && (!generatorName.isBlank()) && (!generatorName.equals(DefaultWorldManager.UNKNOWN_GENERATOR))) {
                worldBuilder.generator(generatorName);
            }
            final World world = worldBuilder.createWorld();

            if (world == null) {
                this.multiWorldApi.getLogger().warning(String.format("Can't load World %s", context.getName()));
                return WorldLifecycleResult.FAILED;
            }
            context.setDirectory(new WorldDirectory(world.getWorldFolder()));
            return WorldLifecycleResult.SUCCESS;

        } catch (final RuntimeException | LinkageError exception) {
            this.multiWorldApi.getLogger().log(Level.SEVERE, String.format("Can't load World %s", context.getName()), exception);
            return WorldLifecycleResult.FAILED;
        }
    }

    @Override
    public @NotNull WorldLifecycleResult unload(@NotNull final Audience audience, @NotNull final NamespacedKey key, final boolean save) {
        BukkitUtil.checkPrimaryThread();

        final World world = Bukkit.getWorld(key);

        if (world == null) {
            return WorldLifecycleResult.NOT_LOADED;
        }

        if (this.multiWorldApi.getScheduler().isRegionized()) {
            this.multiWorldApi.getLogger().warning(String.format("Can't unload World %s: Folia doesn't support unloading Worlds while running", world.getName()));
            return WorldLifecycleResult.UNSUPPORTED;
        }
        final World defaultWorld = Bukkit.getWorlds().get(0);

        if ((world.equals(defaultWorld)) || (DefaultWorldManager.PROTECTED_WORLD_KEYS.contains(world.getKey()))) {
            return WorldLifecycleResult.UNSUPPORTED;
        }
        final Optional<WorldContext> context = this.multiWorldApi.getWorldRegistry().find(key);

        if ((context.isPresent()) && (this.multiWorldApi.getBackupRegistry().isLocked(context.get()))) {
            this.multiWorldApi.getLogger().warning(String.format("Can't unload World %s: a backup operation is running", world.getName()));
            return WorldLifecycleResult.FAILED;
        }
        final Location spawnLocation = defaultWorld.getSpawnLocation();

        for (final Player player : world.getPlayers()) {
            if (!player.teleport(spawnLocation, PlayerTeleportEvent.TeleportCause.PLUGIN)) {
                this.multiWorldApi.getLogger().warning(String.format("Can't unload World %s: %s could not be teleported", world.getName(), player.getName()));
                return WorldLifecycleResult.FAILED;
            }
        }

        if (!Bukkit.unloadWorld(world, save)) {
            this.multiWorldApi.getLogger().warning(String.format("Can't unload World %s", world.getName()));
            return WorldLifecycleResult.FAILED;
        }
        return WorldLifecycleResult.SUCCESS;
    }

    public void cleanup() {
        BukkitUtil.checkPrimaryThread();

        final Set<String> worldNames = new HashSet<>();
        final Set<Path> directories = new HashSet<>();
        directories.add(this.normalize(Bukkit.getWorldContainer()));

        for (final WorldContext context : this.multiWorldApi.getWorldRegistry().getWorlds()) {
            final Path worldDirectory = this.normalize(this.multiWorldApi.getWorldResolver().resolveDirectory(context));
            final Path parentDirectory = worldDirectory.getParent();
            worldNames.add(context.getName());

            if (worldDirectory.getFileName() != null) {
                worldNames.add(worldDirectory.getFileName().toString());
            }

            if (parentDirectory == null) {
                continue;
            }
            directories.add(parentDirectory);

            final Path dimensionsDirectory = parentDirectory.getParent();

            if ((dimensionsDirectory != null) && (dimensionsDirectory.getParent() != null) && (dimensionsDirectory.getFileName() != null)
                    && (dimensionsDirectory.getFileName().toString().equals(DefaultWorldManager.DIMENSIONS_DIRECTORY))
                    && (this.multiWorldApi.getWorldResolver().isLevelDirectory(dimensionsDirectory.getParent().toFile()))) {
                final File[] namespaceDirectories = dimensionsDirectory.toFile().listFiles(File::isDirectory);

                if (namespaceDirectories != null) {
                    Arrays.stream(namespaceDirectories).map(this::normalize).forEach(directories::add);
                }
            }
        }
        final List<File> leftovers = new ArrayList<>();

        for (final Path directory : directories) {
            final File[] files = directory.toFile().listFiles(File::isDirectory);

            if (files == null) {
                continue;
            }
            Arrays.sort(files, Comparator.comparing(File::getName).reversed());

            for (final File file : files) {
                final Matcher matcher = DefaultWorldManager.LEFTOVER_PATTERN.matcher(file.getName());

                if (!matcher.matches()) {
                    continue;
                }
                final File worldDirectory = directory.resolve(matcher.group(1)).toFile();

                if ((matcher.group(2).equals(DefaultWorldManager.PREVIOUS_SUFFIX)) && (!worldDirectory.exists())) {
                    if (!worldNames.contains(matcher.group(1))) {
                        this.multiWorldApi.getLogger().warning(String.format("Found %s from an interrupted restore of an unknown World, leaving it untouched", file));
                        continue;
                    }

                    try {
                        Files.move(file.toPath(), worldDirectory.toPath());
                        this.multiWorldApi.getLogger().warning(String.format("Recovered World directory %s from an interrupted restore", worldDirectory));

                    } catch (final IOException exception) {
                        this.multiWorldApi.getLogger().log(Level.SEVERE, String.format("Can't recover World directory %s from %s", worldDirectory, file), exception);
                    }
                    continue;
                }
                leftovers.add(file);
            }
        }

        if (leftovers.isEmpty()) {
            return;
        }
        this.multiWorldApi.getScheduler().runTaskAsynchronously(() -> {
            for (final File leftover : leftovers) {
                try {
                    FileUtils.deleteDirectory(leftover);
                    this.multiWorldApi.getLogger().info(String.format("Removed leftover World directory %s", leftover));

                } catch (final IOException exception) {
                    this.multiWorldApi.getLogger().log(Level.WARNING, String.format("Can't remove leftover World directory %s", leftover), exception);
                }
            }
        });
    }

    private void rollbackCreate(@NotNull final WorldContext context, @Nullable final World createdWorld, @NotNull final File directory) {
        try {
            this.multiWorldApi.getWorldRegistry().unregister(context);

            if (this.multiWorldApi.getWorldRepository().exists(context)) {
                this.multiWorldApi.getWorldRepository().delete(context);
            }

        } catch (final RuntimeException exception) {
            this.multiWorldApi.getLogger().log(Level.SEVERE, String.format("Can't remove the registration of World %s", context.getName()), exception);
        }
        final World world = (createdWorld != null) ? createdWorld : Bukkit.getWorld(context.getKey());
        final File worldDirectory = (world != null) ? world.getWorldFolder() : directory;

        if ((world != null) && (!Bukkit.unloadWorld(world, false))) {
            this.multiWorldApi.getLogger().severe(String.format("Can't unload the failed World %s, its directory is kept", context.getName()));
            return;
        }

        if ((!worldDirectory.exists()) || (!this.multiWorldApi.getWorldResolver().isDeletable(worldDirectory))) {
            return;
        }
        final File trashDirectory = new File(worldDirectory.getAbsoluteFile().getParentFile(), String.format("%s.deleted-%d", worldDirectory.getName(), System.currentTimeMillis()));

        try {
            Files.move(worldDirectory.toPath(), trashDirectory.toPath());

        } catch (final IOException exception) {
            this.multiWorldApi.getLogger().log(Level.SEVERE, String.format("Can't remove the directory of the failed World %s", context.getName()), exception);
            return;
        }
        this.multiWorldApi.getScheduler().runTaskAsynchronously(() -> {
            try {
                FileUtils.deleteDirectory(trashDirectory);

            } catch (final IOException exception) {
                this.multiWorldApi.getLogger().log(Level.WARNING, String.format("Can't remove the directory of the failed World %s", context.getName()), exception);
            }
        });
    }

    private void sendMessage(@NotNull final Audience audience, @NotNull final WorldLocalizationEntry entry, @NotNull final String worldName) {
        audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, entry)
                .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(worldName))
                .replaceText(builder -> builder.matchLiteral("%world_colored_display_name%").replacement(worldName)));
    }

    @NotNull
    private Path normalize(@NotNull final File file) {
        return file.toPath().toAbsolutePath().normalize();
    }

}
