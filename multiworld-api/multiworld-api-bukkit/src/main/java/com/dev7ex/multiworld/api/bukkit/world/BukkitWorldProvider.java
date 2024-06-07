package com.dev7ex.multiworld.api.bukkit.world;

import com.dev7ex.multiworld.api.world.WorldProvider;

/**
 * Represents a provider for Bukkit worlds in a multi-world environment.
 * Extends WorldProvider interface with BukkitWorldHolder as the generic type parameter.
 * This interface provides functionality for managing Bukkit worlds.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface BukkitWorldProvider extends WorldProvider<BukkitWorldHolder> {}
