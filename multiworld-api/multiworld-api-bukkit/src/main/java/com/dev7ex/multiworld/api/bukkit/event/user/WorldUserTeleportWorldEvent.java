package com.dev7ex.multiworld.api.bukkit.event.user;

import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUser;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a user is teleported between worlds.
 * It extends WorldUserEvent and implements Cancellable.
 * It provides information about the user, the world they were in before the teleportation, and the world they will be teleported to.
 *
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

    /**
     * Constructs a new WorldUserTeleportWorldEvent.
     *
     * @param user            The user being teleported.
     * @param lastWorldHolder The BukkitWorldHolder of the world the user was in before the teleportation.
     * @param nextWorldHolder The BukkitWorldHolder of the world the user will be teleported to.
     */
    public WorldUserTeleportWorldEvent(@NotNull final BukkitWorldUser user, @NotNull final BukkitWorldHolder lastWorldHolder, @NotNull final BukkitWorldHolder nextWorldHolder) {
        super(user);
        this.lastWorldHolder = lastWorldHolder;
        this.nextWorldHolder = nextWorldHolder;
    }

    /**
     * Gets the HandlerList for this event.
     *
     * @return The HandlerList for this event.
     */
    public static HandlerList getHandlerList() {
        return WorldUserTeleportWorldEvent.HANDLERS;
    }

    /**
     * Gets the handlers for this event.
     *
     * @return The handlers for this event.
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldUserTeleportWorldEvent.HANDLERS;
    }

}
