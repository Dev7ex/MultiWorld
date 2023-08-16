package com.dev7ex.multiworld.api.bukkit.event.plugin;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author itsTyrion
 * @since 29.11.2022
 *
 * Called when MultiWorld has finished loading
 *
 */
@Getter(AccessLevel.PUBLIC)
public class MultiWorldStartupCompleteEvent extends MultiWorldEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final long startupDuration;

    public MultiWorldStartupCompleteEvent(@NotNull final MultiWorldBukkitApi multiWorldApi, final long startupDuration) {
        super(multiWorldApi);
        this.startupDuration = startupDuration;
    }

    public static HandlerList getHandlerList() {
        return MultiWorldStartupCompleteEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return MultiWorldStartupCompleteEvent.HANDLERS;
    }

}
