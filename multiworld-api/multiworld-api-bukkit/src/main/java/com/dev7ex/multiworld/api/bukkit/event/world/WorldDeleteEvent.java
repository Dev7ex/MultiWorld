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
 * Represents an event that occurs when a world is deleted.
 * This event is called before the world is deleted.
 * If the event is cancelled, the world deletion process will be halted.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class WorldDeleteEvent extends WorldEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;

    /**
     * Constructs a new WorldDeleteEvent.
     *
     * @param worldHolder   The BukkitWorldHolder of the world being deleted.
     * @param commandSender The command sender initiating the world deletion.
     */
    public WorldDeleteEvent(@NotNull final BukkitWorldHolder worldHolder, @NotNull final CommandSender commandSender) {
        super(worldHolder, commandSender);
    }

    /**
     * Gets the handler list for this event.
     *
     * @return The handler list.
     */
    public static HandlerList getHandlerList() {
        return WorldDeleteEvent.HANDLERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldDeleteEvent.HANDLERS;
    }

}