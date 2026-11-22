package com.dev7ex.multiworld.world;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.event.world.WorldDeleteEvent;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.world.*;
import com.dev7ex.multiworld.api.world.WorldType;
import com.dev7ex.multiworld.api.world.group.WorldGroupRegistry;
import com.dev7ex.multiworld.api.world.result.WorldDeletionResult;
import com.dev7ex.multiworld.api.world.result.WorldLifecycleResult;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

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
        return this.multiWorldApi.getWorldManager().create(audience, worldCreator);
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

        if (this.multiWorldApi.getBackupRegistry().isLocked(context)) {
            audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, WorldLocalizationEntry.MESSAGE_WORLD_BACKUP_IN_PROGRESS)
                    .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(context.getName())));
            return WorldDeletionResult.CANCELLED;
        }
        final WorldResolver resolver = this.multiWorldApi.getWorldResolver();
        final File directory = resolver.resolveDirectory(context);
        final boolean loaded = Bukkit.getWorld(context.getKey()) != null;

        if ((loaded) && (this.multiWorldApi.getWorldManager().unload(audience, context.getKey(), true) != WorldLifecycleResult.SUCCESS)) {
            this.sendDeleteFailed(audience, context);
            return WorldDeletionResult.UNLOAD_FAILED;
        }

        if (!resolver.isDeletable(directory)) {
            this.multiWorldApi.getLogger().severe(String.format("Refusing to delete World %s: unsafe directory %s", context.getName(), directory));
            this.restoreAfterFailedDelete(audience, context, loaded);
            return WorldDeletionResult.DELETE_FAILED;
        }
        final File trashDirectory = new File(directory.getAbsoluteFile().getParentFile(), String.format("%s.deleted-%d", directory.getName(), System.currentTimeMillis()));

        if (directory.exists()) {
            try {
                Files.move(directory.toPath(), trashDirectory.toPath());

            } catch (final IOException exception) {
                this.multiWorldApi.getLogger().log(Level.SEVERE, String.format("Can't delete World %s", context.getName()), exception);
                this.restoreAfterFailedDelete(audience, context, loaded);
                return WorldDeletionResult.DELETE_FAILED;
            }
        }
        this.repository.delete(context);
        this.registry.unregister(context);

        if (trashDirectory.exists()) {
            this.multiWorldApi.getScheduler().runTaskAsynchronously(() -> {
                try {
                    FileUtils.deleteDirectory(trashDirectory);

                } catch (final IOException exception) {
                    this.multiWorldApi.getLogger().log(Level.SEVERE, String.format("Can't remove deleted World directory %s", trashDirectory), exception);
                }
            });
        }

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
                .setDirectory(new WorldDirectory(world.getWorldFolder()))
                .build();

        return this.register(worldContext);
    }

    @NotNull
    private WorldContext register(@NotNull final WorldContext context) {
        final Optional<WorldContext> registeredContext = this.registry.find(context.getKey());

        if (registeredContext.isPresent()) {
            return registeredContext.get();
        }

        if (this.repository.exists(context)) {
            this.repository.delete(context);
        }
        this.repository.insert(context);
        this.registry.register(context);
        return context;
    }

    private void restoreAfterFailedDelete(@NotNull final Audience audience, @NotNull final WorldContext context, final boolean loaded) {
        if (loaded) {
            this.multiWorldApi.getWorldManager().load(audience, context.getKey());
        }
        this.sendDeleteFailed(audience, context);
    }

    private void sendDeleteFailed(@NotNull final Audience audience, @NotNull final WorldContext context) {
        audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, WorldLocalizationEntry.COMMAND_WORLD_DELETE_FAILED)
                .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(context.getName())));
    }

}
