package com.dev7ex.multiworld.api.bukkit.world.generator;

import com.dev7ex.multiworld.api.world.generator.WorldGeneratorHolder;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Bukkit implementation of a WorldGeneratorHolder.
 * It holds a reference to the Bukkit plugin that provides the world generator.
 *
 * @author Dev7ex
 * @since 06.06.2024
 */
@Getter(AccessLevel.PUBLIC)
public class BukkitWorldGeneratorHolder implements WorldGeneratorHolder {

    private final Plugin plugin;

    /**
     * Constructs a new BukkitWorldGeneratorHolder with the given plugin.
     *
     * @param plugin The Bukkit plugin providing the world generator.
     */
    public BukkitWorldGeneratorHolder(@NotNull final Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets the name of the plugin providing the world generator.
     *
     * @return The name of the plugin.
     */
    @Override
    public String getName() {
        return this.plugin.getName();
    }

}
