package com.dev7ex.multiworld.api.event.world;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.io.File;

/**
 * @author Dev7ex
 * @since 30.12.2022
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class WorldCloneEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;

    private final CommandSender commandSender;
    private final String worldName;
    private final String clonedName;
    private final File sourceFolder;
    private final File destinationFolder;

    public WorldCloneEvent(final CommandSender commandSender, final String worldName, final String clonedName, final File sourceFolder, final File destinationFolder) {
        this.commandSender = commandSender;
        this.worldName = worldName;
        this.clonedName = clonedName;
        this.sourceFolder = sourceFolder;
        this.destinationFolder = destinationFolder;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
