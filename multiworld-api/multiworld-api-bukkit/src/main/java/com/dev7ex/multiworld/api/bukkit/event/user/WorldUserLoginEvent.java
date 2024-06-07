package com.dev7ex.multiworld.api.bukkit.event.user;

import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUser;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event triggered when a world user logs in.
 * This event is called after the user successfully logs in.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public class WorldUserLoginEvent extends WorldUserEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Constructs a new WorldUserLoginEvent.
     *
     * @param user The WorldUser who logged in.
     */
    public WorldUserLoginEvent(@NotNull final BukkitWorldUser user) {
        super(user);
    }

    /**
     * Gets the handler list for this event.
     *
     * @return The handler list.
     */
    public static HandlerList getHandlerList() {
        return WorldUserLoginEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldUserLoginEvent.HANDLERS;
    }

}
