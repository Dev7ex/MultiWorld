package com.dev7ex.multiworld.api.event.world.group;

import com.dev7ex.multiworld.api.world.group.WorldGroup;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 19.07.2025
 */
@Getter(AccessLevel.PUBLIC)
public abstract class WorldGroupEvent extends Event {

    private final WorldGroup group;

    public WorldGroupEvent(@NotNull final WorldGroup group) {
        this.group = group;
    }

}
