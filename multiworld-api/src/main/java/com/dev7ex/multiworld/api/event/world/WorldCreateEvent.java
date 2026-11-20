package com.dev7ex.multiworld.api.event.world;

import com.dev7ex.multiworld.api.world.WorldContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 13.04.2025
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class WorldCreateEvent extends WorldEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private boolean notifyOnCancel = true;

    public WorldCreateEvent(@NotNull final WorldContext context, @NotNull final Audience audience) {
        super(context, audience);
    }

    public static HandlerList getHandlerList() {
        return WorldCreateEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldCreateEvent.HANDLERS;
    }

}
