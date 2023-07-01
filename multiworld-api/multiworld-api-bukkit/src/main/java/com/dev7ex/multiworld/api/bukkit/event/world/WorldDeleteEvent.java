package com.dev7ex.multiworld.api.bukkit.event.world;

import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class WorldDeleteEvent extends WorldEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;

    public WorldDeleteEvent(@NotNull final BukkitWorldHolder worldHolder, @NotNull final CommandSender commandSender) {
        super(worldHolder, commandSender);
    }

    public static HandlerList getHandlerList() {
        return WorldDeleteEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldDeleteEvent.HANDLERS;
    }

}
