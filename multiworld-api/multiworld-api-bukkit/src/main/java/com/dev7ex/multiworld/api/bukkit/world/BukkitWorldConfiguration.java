package com.dev7ex.multiworld.api.bukkit.world;

import com.dev7ex.multiworld.api.world.WorldConfiguration;

/**
 * Represents the configuration of a Bukkit world in a multi-world environment.
 * Extends WorldConfiguration with BukkitWorldHolder as the generic type parameter.
 * This interface provides methods for configuring Bukkit worlds.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface BukkitWorldConfiguration extends WorldConfiguration<BukkitWorldHolder> {}
