package com.dev7ex.multiworld.api.event.user;

import com.dev7ex.multiworld.api.event.user.UserEvent;
import com.dev7ex.multiworld.user.WorldUser;
import org.bukkit.event.HandlerList;

/**
 * @author Dev7ex
 * @since 20.12.2022
 */
public class UserLoginEvent extends UserEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public UserLoginEvent(final WorldUser user) {
        super(user);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
