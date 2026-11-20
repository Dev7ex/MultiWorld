package com.dev7ex.multiworld.api.event;

import com.dev7ex.multiworld.api.MultiWorldApi;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public abstract class MultiWorldListener implements Listener {

    private final MultiWorldApi multiWorldApi;

    protected MultiWorldListener(@NotNull final MultiWorldApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }



}
