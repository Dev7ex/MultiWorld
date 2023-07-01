package com.dev7ex.multiworld.api.bukkit.event.user;

import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.user.WorldUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class WorldUserTeleportWorldEvent extends WorldUserEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private BukkitWorldHolder lastWorldHolder;
    private BukkitWorldHolder nextWorldHolder;

    public WorldUserTeleportWorldEvent(@NotNull final WorldUser user, @NotNull final BukkitWorldHolder lastWorldHolder, @NotNull final BukkitWorldHolder nextWorldHolder) {
        super(user);
        this.lastWorldHolder = lastWorldHolder;
        this.nextWorldHolder = nextWorldHolder;
    }

    public static HandlerList getHandlerList() {
        return WorldUserTeleportWorldEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldUserTeleportWorldEvent.HANDLERS;
    }

}
