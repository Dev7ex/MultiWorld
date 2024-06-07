package com.dev7ex.multiworld.api.bukkit.event.world;

import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Represents an event that occurs when a world is cloned.
 * This event is called before the world is cloned.
 * If the event is cancelled, the world will not be cloned.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class WorldCloneEvent extends WorldEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;

    private final String clonedName;
    private final File sourceFolder;
    private final File destinationFolder;

    /**
     * Constructs a new WorldCloneEvent.
     *
     * @param worldHolder       The BukkitWorldHolder of the world being cloned.
     * @param commandSender     The command sender initiating the clone operation.
     * @param clonedName        The name of the cloned world.
     * @param sourceFolder      The source folder of the world being cloned.
     * @param destinationFolder The destination folder where the cloned world will be placed.
     */
    public WorldCloneEvent(@NotNull final BukkitWorldHolder worldHolder, @NotNull final CommandSender commandSender,
                           @NotNull final String clonedName, @NotNull final File sourceFolder,
                           @NotNull final File destinationFolder) {
        super(worldHolder, commandSender);
        this.clonedName = clonedName;
        this.sourceFolder = sourceFolder;
        this.destinationFolder = destinationFolder;
    }

    /**
     * Gets the handler list for this event.
     *
     * @return The handler list.
     */
    public static HandlerList getHandlerList() {
        return WorldCloneEvent.HANDLERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldCloneEvent.HANDLERS;
    }

}