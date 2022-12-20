package com.dev7ex.multiworld.api.event.plugin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author itsTyrion
 * @since 29.11.2022
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class MultiWorldStartupCompleteEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private long startupDuration;

    public MultiWorldStartupCompleteEvent(final long startupDuration) {
        this.startupDuration = startupDuration;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
