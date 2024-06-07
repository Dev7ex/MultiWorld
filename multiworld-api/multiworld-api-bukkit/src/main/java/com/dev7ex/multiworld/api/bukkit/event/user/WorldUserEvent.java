package com.dev7ex.multiworld.api.bukkit.event.user;

import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUser;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an abstract event related to a world user.
 * All events involving world users should extend this class.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
abstract class WorldUserEvent extends Event {

    private final BukkitWorldUser user;

    /**
     * Constructs a new WorldUserEvent.
     *
     * @param user The WorldUser associated with the event.
     */
    WorldUserEvent(@NotNull final BukkitWorldUser user) {
        this.user = user;
    }

}
