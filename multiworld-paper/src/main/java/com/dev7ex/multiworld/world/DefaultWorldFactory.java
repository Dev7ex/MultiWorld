package com.dev7ex.multiworld.world;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.event.world.WorldCreateEvent;
import com.dev7ex.multiworld.api.event.world.WorldDeleteEvent;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.world.*;
import com.dev7ex.multiworld.api.world.WorldType;
import com.dev7ex.multiworld.api.world.group.WorldGroupRegistry;
import com.dev7ex.multiworld.api.world.result.WorldDeletionResult;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Dev7ex
 * @since 16.10.2025
 */
public class DefaultWorldFactory implements WorldFactory {

    private final MultiWorldApi multiWorldApi;
    private final WorldRepository repository;
    private final WorldGroupRegistry groupRegistry;
    private final WorldRegistry registry;

    public DefaultWorldFactory(@NotNull final MultiWorldApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
        this.repository = multiWorldApi.getWorldRepository();
        this.groupRegistry = multiWorldApi.getGroupRegistry();
        this.registry = multiWorldApi.getWorldRegistry();
    }

    @Override
    public @Nullable WorldContext create(@NotNull final Audience audience, @NotNull final NamespacedKey worldKey) {
        return this.create(audience, new WorldBuilder(worldKey)
                .seed(ThreadLocalRandom.current().nextLong())
        );
    }

    @Override
    public @Nullable WorldContext create(@NotNull final Audience audience, @NotNull final String worldName) {
        final World world = Bukkit.getWorld(worldName);

        if (world != null) {
            return this.create(audience, world);
        }
        return this.create(audience, new WorldBuilder(this.createWorldKey(worldName))
                .seed(ThreadLocalRandom.current().nextLong())
        );
    }

    @Override
    public @Nullable WorldContext create(@NotNull final Audience audience, @NotNull final WorldContext context) {
        return this.create(audience, new WorldBuilder(context.getKey())
                .environment(context.getEnvironment())
                .type(context.getType())
                .seed(context.getSeed())
        );
    }

    @Override
    public @Nullable WorldContext create(@NotNull final Audience audience, @NotNull final WorldCreator worldCreator) {
        audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_CREATE_STARTING)
                .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(worldCreator.name())));

        final WorldContext worldContext = DefaultWorldContext.builder()
                .setKey(worldCreator.key())
                .setName(worldCreator.name())
                .setDisplayName(worldCreator.name())
                .setColor(NamedTextColor.GRAY)
                .setIcon(Material.GRASS_BLOCK)
                .setCreatorName(audience.get(Identity.NAME).orElse("Unknown"))
                .setRegistrationTimestamp(System.currentTimeMillis())
                .setGroupId(this.groupRegistry.getDefaultGroup().getId())
                .setGroup(this.groupRegistry.getDefaultGroup())
                .setEnvironment(WorldEnvironment.ofBukkit(worldCreator.environment()))
                .setType(WorldType.ofBukkit(worldCreator.type()))
                .setSeed(worldCreator.seed())
                .build();

        final WorldCreateEvent event = new WorldCreateEvent(worldContext, audience);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            if (event.isNotifyOnCancel()) {
                audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_CREATE_CANCELLED)
                        .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(worldCreator.name())));
            }
            return null;
        }
        final World world = worldCreator.createWorld();

        if (world == null) {
            return null;
        }

        worldContext.setKey(world.getKey());
        worldContext.setName(world.getName());
        worldContext.setEnvironment(WorldEnvironment.ofBukkit(world.getEnvironment()));
        worldContext.setType(WorldType.ofBukkit(worldCreator.type()));
        worldContext.setSeed(world.getSeed());

        audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_CREATE_FINISHING)
                .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(worldContext.getName())));

        this.repository.insert(worldContext);
        this.registry.register(worldContext);

        return worldContext;
    }

    @Override
    public @NotNull WorldDeletionResult delete(@NotNull final Audience audience, @NotNull final WorldContext context) {
        audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_DELETE_STARTING)
                .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(context.getName())));

        final WorldDeleteEvent event = new WorldDeleteEvent(context, audience);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            if (event.isNotifyOnCancel()) {
                audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_DELETE_CANCELLED)
                        .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(context.getName())));
            }
            return WorldDeletionResult.CANCELLED;
        }

        if (context.isLoaded()) {
            this.multiWorldApi.getWorldManager().unload(audience, context.getKey(), true);
        }

        try {
            FileUtils.deleteDirectory(context.getDirectory());

        } catch (final IOException exception) {
            audience.sendMessage(Component.text("An error has occurred. View the logs", NamedTextColor.RED));
            return WorldDeletionResult.DELETE_FAILED;
        }

        this.repository.delete(context);
        this.registry.unregister(context);

        audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_DELETE_FINISHING)
                .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(context.getName())));
        return WorldDeletionResult.SUCCESS;
    }

    @NotNull
    private NamespacedKey createWorldKey(@NotNull final String worldName) {
        return new NamespacedKey(this.multiWorldApi, worldName.toLowerCase(Locale.ROOT)
                .replace(' ', '_'));
    }

    @NotNull
    private WorldContext create(@NotNull final Audience audience, @NotNull final World world) {
        final WorldContext worldContext = DefaultWorldContext.builder()
                .setKey(world.getKey())
                .setName(world.getName())
                .setDisplayName(world.getName())
                .setColor(NamedTextColor.GRAY)
                .setIcon(Material.GRASS_BLOCK)
                .setCreatorName(audience.get(Identity.NAME).orElse("Unknown"))
                .setRegistrationTimestamp(System.currentTimeMillis())
                .setGroupId(this.groupRegistry.getDefaultGroup().getId())
                .setGroup(this.groupRegistry.getDefaultGroup())
                .setEnvironment(WorldEnvironment.ofBukkit(world.getEnvironment()))
                .setType(WorldType.ofBukkit(world.getWorldType()))
                .setSeed(world.getSeed())
                .build();

        this.repository.insert(worldContext);
        this.registry.register(worldContext);

        return worldContext;
    }

}
