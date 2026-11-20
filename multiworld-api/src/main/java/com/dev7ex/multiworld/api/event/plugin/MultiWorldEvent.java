package com.dev7ex.multiworld.api.event.plugin;

import com.dev7ex.multiworld.api.MultiWorldApi;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 13.04.2025
 */
@Getter(AccessLevel.PUBLIC)
public abstract class MultiWorldEvent extends Event {

    private final MultiWorldApi multiWorldApi;

    protected MultiWorldEvent(@NotNull final MultiWorldApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }

}
