package com.dev7ex.multiworld.api.event.world;

import com.dev7ex.multiworld.world.WorldProperties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * @author Dev7ex
 * @since 20.12.2022
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class WorldDeleteEvent extends WorldEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;

    public WorldDeleteEvent(final WorldProperties worldProperties) {
        super(worldProperties);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
