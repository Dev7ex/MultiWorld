package com.dev7ex.multiworld.api.bukkit.event.world;

import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.world.WorldFlag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event triggered when a world flag is changed.
 * This event can be cancelled.
 *
 * @author Dev7ex
 * @since 29.06.2023
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class WorldFlagChangeEvent extends WorldEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private WorldFlag flag;
    private String value;

    /**
     * Constructs a new WorldFlagChangeEvent.
     *
     * @param worldHolder   The BukkitWorldHolder associated with the event.
     * @param commandSender The command sender related to the event.
     * @param flag          The flag that was changed.
     * @param value         The new value of the flag.
     */
    public WorldFlagChangeEvent(@NotNull final BukkitWorldHolder worldHolder, @NotNull final CommandSender commandSender, @NotNull final WorldFlag flag, @NotNull final String value) {
        super(worldHolder, commandSender);
        this.flag = flag;
        this.value = value;
    }

    /**
     * Returns the handler list.
     *
     * @return The handler list.
     */
    public static HandlerList getHandlerList() {
        return WorldFlagChangeEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldFlagChangeEvent.HANDLERS;
    }

}