package com.dev7ex.multiworld.api.bukkit.event.plugin;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
abstract class MultiWorldEvent extends Event {

    private final MultiWorldBukkitApi multiWorldApi;

    MultiWorldEvent(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }

}
