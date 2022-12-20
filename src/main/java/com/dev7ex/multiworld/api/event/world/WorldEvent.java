package com.dev7ex.multiworld.api.event.world;

import com.dev7ex.multiworld.world.WorldProperties;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.Event;

/**
 * @author Dev7ex
 * @since 20.12.2022
 */
@Getter(AccessLevel.PUBLIC)
public abstract class WorldEvent extends Event {

    private final WorldProperties worldProperties;

    public WorldEvent(final WorldProperties worldProperties) {
        this.worldProperties = worldProperties;
    }

}
