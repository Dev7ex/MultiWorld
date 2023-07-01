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

    public WorldCloneEvent(@NotNull final BukkitWorldHolder worldHolder, @NotNull final CommandSender commandSender,
                           @NotNull final String clonedName, @NotNull final File sourceFolder,
                           @NotNull final File destinationFolder) {
        super(worldHolder, commandSender);
        this.clonedName = clonedName;
        this.sourceFolder = sourceFolder;
        this.destinationFolder = destinationFolder;
    }

    public static HandlerList getHandlerList() {
        return WorldCloneEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldCloneEvent.HANDLERS;
    }

}
