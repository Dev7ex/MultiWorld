package com.dev7ex.multiworld.api.event.world.group;

import com.dev7ex.multiworld.api.world.group.WorldGroup;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 19.07.2025
 */
@Getter(AccessLevel.PUBLIC)
public class WorldGroupCreateEvent extends WorldGroupEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public WorldGroupCreateEvent(@NotNull final WorldGroup group) {
        super(group);
    }

    public static HandlerList getHandlerList() {
        return WorldGroupCreateEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldGroupCreateEvent.HANDLERS;
    }

}
