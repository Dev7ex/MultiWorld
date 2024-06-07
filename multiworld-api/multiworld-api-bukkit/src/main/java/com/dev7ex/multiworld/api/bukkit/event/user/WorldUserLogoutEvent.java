package com.dev7ex.multiworld.api.bukkit.event.user;

import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUser;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event triggered when a world user logs out.
 * This event is called after the user successfully logs out.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public class WorldUserLogoutEvent extends WorldUserEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Constructs a new WorldUserLogoutEvent.
     *
     * @param user The WorldUser who logged out.
     */
    public WorldUserLogoutEvent(@NotNull final BukkitWorldUser user) {
        super(user);
    }

    /**
     * Gets the handler list for this event.
     *
     * @return The handler list.
     */
    public static HandlerList getHandlerList() {
        return WorldUserLogoutEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldUserLogoutEvent.HANDLERS;
    }

}
