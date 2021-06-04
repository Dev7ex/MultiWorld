package com.dev7ex.multiworld.event.world;

import com.dev7ex.multiworld.world.WorldOption;
import com.dev7ex.multiworld.world.WorldProperties;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Dev7ex
 * @since 24.05.2021
 */
@Getter
public final class WorldOptionUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final WorldProperties worldProperties;
    private final WorldOption worldOption;
    private final String value;

    public WorldOptionUpdateEvent(final WorldProperties worldProperties, final WorldOption worldOption, final String value) {
        this.worldProperties = worldProperties;
        this.worldOption = worldOption;
        this.value = value;
    }

    @Override
    public final HandlerList getHandlers() {
        return WorldOptionUpdateEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return WorldOptionUpdateEvent.handlers;
    }

}
