package com.dev7ex.multiworld.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author itsTyrion
 * @since 29.11.2022
 */
public class MultiWorldStartupCompleteEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
