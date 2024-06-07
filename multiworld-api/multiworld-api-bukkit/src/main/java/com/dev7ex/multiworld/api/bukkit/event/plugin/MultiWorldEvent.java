package com.dev7ex.multiworld.api.bukkit.event.plugin;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for events related to the MultiWorld plugin.
 * Provides access to the MultiWorldBukkitApi instance.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
abstract class MultiWorldEvent extends Event {

    private final MultiWorldBukkitApi multiWorldApi;

    /**
     * Constructs a MultiWorldEvent with the given MultiWorldBukkitApi instance.
     *
     * @param multiWorldApi The MultiWorldBukkitApi instance.
     */
    MultiWorldEvent(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }

}
