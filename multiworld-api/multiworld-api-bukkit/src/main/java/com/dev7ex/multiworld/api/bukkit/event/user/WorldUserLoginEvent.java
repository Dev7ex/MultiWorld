package com.dev7ex.multiworld.api.bukkit.event.user;

import com.dev7ex.multiworld.api.user.WorldUser;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public class WorldUserLoginEvent extends WorldUserEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public WorldUserLoginEvent(@NotNull final WorldUser user) {
        super(user);
    }

    public static HandlerList getHandlerList() {
        return WorldUserLoginEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldUserLoginEvent.HANDLERS;
    }

}
