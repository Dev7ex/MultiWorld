package com.dev7ex.multiworld.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public final class WorldUserProperties {

    private String lastWorld;
    private Location lastWorldLocation;

}
