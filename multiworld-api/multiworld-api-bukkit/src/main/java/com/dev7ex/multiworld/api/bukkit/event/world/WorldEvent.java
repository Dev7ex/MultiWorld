package com.dev7ex.multiworld.api.bukkit.event.world;

import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
abstract class WorldEvent extends Event {

    private final BukkitWorldHolder worldHolder;
    private final CommandSender commandSender;

    WorldEvent(@NotNull final BukkitWorldHolder worldHolder, @NotNull final CommandSender commandSender) {
        this.worldHolder = worldHolder;
        this.commandSender = commandSender;
    }

}
