package com.dev7ex.multiworld.world;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.world.*;
import com.dev7ex.multiworld.api.world.flag.WorldFlagRegistry;
import com.dev7ex.multiworld.api.world.group.WorldGroup;
import com.dev7ex.multiworld.api.world.group.WorldGroupRegistry;
import com.dev7ex.multiworld.api.world.result.WorldLifecycleResult;
import com.dev7ex.multiworld.module.DefaultWorldModule;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DefaultWorldRegistry extends DefaultWorldModule implements WorldRegistry {

    private static final String DEFAULT_WORLD_ID = "world";

    private final WorldRepository repository;
    private final WorldManager worldManager;
    private final WorldFlagRegistry flagRegistry;
    private final WorldGroupRegistry groupRegistry;
    private final WorldFactory factory;
    private final Map<NamespacedKey, WorldContext> worldContexts = new HashMap<>();

    public DefaultWorldRegistry(@NotNull final MultiWorldApi multiWorldApi) {
        super(multiWorldApi);
        this.repository = multiWorldApi.getWorldRepository();
        this.worldManager = multiWorldApi.getWorldManager();
        this.flagRegistry = multiWorldApi.getFlagRegistry();
        this.groupRegistry = multiWorldApi.getGroupRegistry();
        this.factory = multiWorldApi.getWorldFactory();
    }


    @Override
    public void onEnable() {

        // Load persisted worlds into the registry.
        this.repository.findAll().forEach((worldKey, worldContext) -> {
            final WorldGroup group = this.groupRegistry
                    .find(worldContext.getGroupId())
                    .orElseGet(this.groupRegistry::getDefaultGroup);

            worldContext.setGroup(group);
            worldContext.setDirectory(new WorldDirectory(super.getMultiWorldApi().getWorldResolver().resolveDirectory(worldContext)));

            this.repository.update(worldContext, WorldProperty.GROUP_ID, group.getId());
            this.register(worldContext);
        });

        // Discover loaded Bukkit worlds from legacy and namespaced layouts.
        for (final World world : Bukkit.getWorlds()) {
            final NamespacedKey worldKey = world.getKey();

            if (this.find(worldKey).isPresent()) {
                continue;
            }

            final WorldGroup group = this.groupRegistry.getDefaultGroup();

            final WorldContext worldContext = DefaultWorldContext.builder()
                    .setKey(worldKey)
                    .setName(world.getName())
                    .setDisplayName(world.getName())
                    .setColor(NamedTextColor.GRAY)
                    .setIcon(Material.GRASS_BLOCK)
                    .setCreatorName("Unknown")
                    .setRegistrationTimestamp(System.currentTimeMillis())
                    .setGroupId(group.getId())
                    .setGroup(group)
                    .setEnvironment(WorldEnvironment.ofBukkit(world.getEnvironment()))
                    .setType(WorldType.ofBukkit(world.getWorldType()))
                    .setSeed(world.getSeed())
                    .build();

            worldContext.setDirectory(new WorldDirectory(world.getWorldFolder()));

            if (!this.repository.exists(worldKey)) {
                this.repository.insert(worldContext);
            }

            this.register(worldContext);
        }

        // Log the number of registered worlds.
        super.getMultiWorldApi().getLogger().info("Registered %d world(s).".formatted(this.worldContexts.size()));

        super.getMultiWorldApi().getScheduler().runTask(() -> {
            if (this.worldManager instanceof final DefaultWorldManager defaultWorldManager) {
                defaultWorldManager.cleanup();
            }
            this.loadWorlds();
        });
    }


    @Override
    public void onDisable() {
        this.worldContexts.clear();
    }

    private void loadWorlds() {
        final List<WorldContext> worlds = this.getUnloadedWorlds()
                .stream()
                .sorted(Comparator.comparing(WorldContext::getName))
                .toList();

        if (worlds.isEmpty()) {
            return;
        }

        if (super.getMultiWorldApi().getScheduler().isRegionized()) {
            super.getMultiWorldApi().getLogger().warning(String.format("Folia can't load Worlds while running, [%d] World(s) not loaded", worlds.size()));
            return;
        }
        int loaded = 0;

        for (final WorldContext context : worlds) {
            if (this.worldManager.load(Bukkit.getConsoleSender(), context.getKey()) == WorldLifecycleResult.SUCCESS) {
                loaded++;
            }
        }
        super.getMultiWorldApi().getLogger().info(String.format("Loaded [%d/%d] World(s)", loaded, worlds.size()));
    }

    @Override
    public void register(@NotNull final WorldContext context) {
        this.worldContexts.put(context.getKey(), context);
    }

    @Override
    public void unregister(@NotNull final WorldContext context) {
        this.worldContexts.remove(context.getKey());
    }

    @Override
    public void unregister(@NotNull final NamespacedKey key) {
        this.worldContexts.remove(key);
    }

    @Override
    public @NotNull Optional<WorldContext> find(@NotNull final String worldName) {
        return this.worldContexts.values()
                .stream()
                .filter(context -> context.getName().equalsIgnoreCase(worldName))
                .findFirst();
    }

    @Override
    public @NotNull Optional<WorldContext> find(@NotNull final NamespacedKey key) {
        return Optional.ofNullable(this.worldContexts.get(key));
    }

    @Override
    public @NotNull Optional<WorldContext> find(@NotNull final World world) {
        return Optional.ofNullable(this.worldContexts.get(world.getKey()));
    }

    @Override
    public @NotNull Optional<WorldContext> findDefaultWorld() {
        return Optional.empty();
    }

    @Override
    public @NotNull List<WorldContext> getWorlds() {
        return new ArrayList<>(this.worldContexts.values());
    }

    @Override
    public @NotNull List<WorldContext> getLoadedWorlds() {
        return this.getWorlds()
                .stream()
                .filter(WorldContext::isLoaded)
                .toList();
    }

    @Override
    public @NotNull List<WorldContext> getUnloadedWorlds() {
        return this.getWorlds()
                .stream()
                .filter(worldContext -> !worldContext.isLoaded())
                .toList();
    }

    @Override
    public @NotNull List<WorldContext> getWorldsWithCreator(@NotNull final String creatorName) {
        return this.getWorlds()
                .stream()
                .filter(worldContext -> worldContext.getCreatorName().equalsIgnoreCase(creatorName))
                .toList();
    }

}
