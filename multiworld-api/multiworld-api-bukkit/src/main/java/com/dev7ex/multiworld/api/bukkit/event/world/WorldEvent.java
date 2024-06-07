package com.dev7ex.multiworld.api.bukkit.event.world;

import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a base class for events related to worlds.
 * This class should be extended by specific world-related events.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
abstract class WorldEvent extends Event {

    private final BukkitWorldHolder worldHolder;
    private final CommandSender commandSender;

    /**
     * Constructs a new WorldEvent.
     *
     * @param worldHolder   The BukkitWorldHolder associated with the event.
     * @param commandSender The command sender related to the event.
     */
    WorldEvent(@NotNull final BukkitWorldHolder worldHolder, @NotNull final CommandSender commandSender) {
        this.worldHolder = worldHolder;
        this.commandSender = commandSender;
    }

}