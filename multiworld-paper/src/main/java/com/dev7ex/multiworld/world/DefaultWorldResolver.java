package com.dev7ex.multiworld.world;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.WorldResolver;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

/**
 * @author MGG
 * @since 20.11.2026
 */
public class DefaultWorldResolver implements WorldResolver {

    private static final String DIMENSIONS_DIRECTORY = "dimensions";
    private static final String LEVEL_FILE = "level.dat";

    private final MultiWorldApi multiWorldApi;
    private final Method levelDirectoryMethod;

    public DefaultWorldResolver(@NotNull final MultiWorldApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
        this.levelDirectoryMethod = DefaultWorldResolver.findLevelDirectoryMethod();

        multiWorldApi.getLogger().info(this.isDimensionLayout()
                ? "Detected world layout: dimensions (<level>/dimensions/<namespace>/<world>)"
                : "Detected world layout: legacy (<container>/<world>)");
    }

    @Override
    public @NotNull File resolveDirectory(@NotNull final WorldContext context) {
        final World world = Bukkit.getWorld(context.getName());

        if (world != null) {
            return world.getWorldFolder();
        }
        final File legacyDirectory = new File(Bukkit.getWorldContainer(), context.getName());
        final Path levelDirectory = this.getLevelDirectory();

        if (levelDirectory == null) {
            return legacyDirectory;
        }

        if ((new File(legacyDirectory, DefaultWorldResolver.LEVEL_FILE).isFile()) && (!this.isSamePath(legacyDirectory.toPath(), levelDirectory))) {
            return legacyDirectory;
        }
        final NamespacedKey key = context.getKey();

        return levelDirectory.resolve(DefaultWorldResolver.DIMENSIONS_DIRECTORY)
                .resolve(key.getNamespace())
                .resolve(key.getKey())
                .toFile();
    }

    @Override
    public boolean isDimensionLayout() {
        return this.levelDirectoryMethod != null;
    }

    @Override
    public boolean isLevelDirectory(@NotNull final File directory) {
        final Path levelDirectory = this.getLevelDirectory();

        return (levelDirectory != null) && (this.isSamePath(directory.toPath(), levelDirectory));
    }

    @Override
    public boolean isDeletable(@NotNull final File directory) {
        final Path path = this.normalize(directory.toPath());
        final Path levelDirectory = this.getLevelDirectory();
        boolean worldDirectory = (this.normalize(Bukkit.getWorldContainer().toPath()).equals(path.getParent()))
                && ((!directory.exists()) || (new File(directory, DefaultWorldResolver.LEVEL_FILE).isFile()));

        if (levelDirectory != null) {
            final Path dimensionsDirectory = this.normalize(levelDirectory).resolve(DefaultWorldResolver.DIMENSIONS_DIRECTORY);

            if ((path.startsWith(dimensionsDirectory)) && (path.getNameCount() == dimensionsDirectory.getNameCount() + 2)) {
                worldDirectory = true;
            }

            if (this.isSamePath(path, levelDirectory)) {
                return false;
            }
        }

        if (!worldDirectory) {
            return false;
        }

        for (final World world : Bukkit.getWorlds()) {
            if (this.normalize(world.getWorldFolder().toPath()).startsWith(path)) {
                return false;
            }
        }
        return true;
    }

    @NotNull
    private Path normalize(@NotNull final Path path) {
        return path.toAbsolutePath().normalize();
    }

    private boolean isSamePath(@NotNull final Path first, @NotNull final Path second) {
        if ((Files.exists(first)) && (Files.exists(second))) {
            try {
                return Files.isSameFile(first, second);

            } catch (final IOException exception) {
                this.multiWorldApi.getLogger().log(Level.WARNING, "Can't compare world directories, falling back to path comparison", exception);
            }
        }
        return this.normalize(first).equals(this.normalize(second));
    }

    @Nullable
    private Path getLevelDirectory() {
        if (this.levelDirectoryMethod == null) {
            return null;
        }

        try {
            return (Path) this.levelDirectoryMethod.invoke(Bukkit.getServer());

        } catch (final ReflectiveOperationException exception) {
            this.multiWorldApi.getLogger().log(Level.WARNING, "Can't resolve the level directory, falling back to the legacy world layout", exception);
            return null;
        }
    }

    @Nullable
    private static Method findLevelDirectoryMethod() {
        try {
            final Method method = Server.class.getMethod("getLevelDirectory");

            return Path.class.isAssignableFrom(method.getReturnType()) ? method : null;

        } catch (final NoSuchMethodException exception) {
            return null;
        }
    }

}
