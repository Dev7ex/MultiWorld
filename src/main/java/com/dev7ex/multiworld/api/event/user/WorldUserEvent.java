package com.dev7ex.multiworld.api.event.user;

import com.dev7ex.multiworld.user.WorldUser;

import lombok.AccessLevel;
import lombok.Getter;

import org.bukkit.event.Event;

/**
 * @author Dev7ex
 * @since 20.12.2022
 */
@Getter(AccessLevel.PUBLIC)
public abstract class WorldUserEvent extends Event {

    private final WorldUser user;

    public WorldUserEvent(final WorldUser user) {
        this.user = user;
    }

}
