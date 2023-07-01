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

    public WorldFlagChangeEvent(@NotNull final BukkitWorldHolder worldHolder, @NotNull final CommandSender commandSender, @NotNull final WorldFlag flag, @NotNull final String value) {
        super(worldHolder, commandSender);
        this.flag = flag;
        this.value = value;
    }

    public static HandlerList getHandlerList() {
        return WorldFlagChangeEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldFlagChangeEvent.HANDLERS;
    }

}
