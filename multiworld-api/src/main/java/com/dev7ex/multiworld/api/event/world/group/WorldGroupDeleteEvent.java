package com.dev7ex.multiworld.api.event.world.group;

import com.dev7ex.multiworld.api.world.group.WorldGroup;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 16.10.2025
 */
public class WorldGroupDeleteEvent extends WorldGroupEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public WorldGroupDeleteEvent(@NotNull final WorldGroup group) {
        super(group);
    }

    public static HandlerList getHandlerList() {
        return WorldGroupDeleteEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldGroupDeleteEvent.HANDLERS;
    }

}
