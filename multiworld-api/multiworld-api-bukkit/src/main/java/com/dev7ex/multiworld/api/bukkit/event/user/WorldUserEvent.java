package com.dev7ex.multiworld.api.bukkit.event.user;

import com.dev7ex.multiworld.api.user.WorldUser;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
abstract class WorldUserEvent extends Event {

    private final WorldUser user;

    WorldUserEvent(@NotNull final WorldUser user) {
        this.user = user;
    }

}
