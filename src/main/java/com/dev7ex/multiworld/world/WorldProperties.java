package com.dev7ex.multiworld.world;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Dev7ex
 * @since 20.05.2021
 *
 */

@Getter @Setter
public final class WorldProperties {

    private final String worldName;
    private String worldCreator;
    private long creationTime;
    private long lastWorldInteraction;
    private boolean loaded;

    public WorldProperties(final String worldName, final String worldCreator, final long creationTime, final long lastWorldInteraction) {
        this.worldName = worldName;
        this.worldCreator = worldCreator;
        this.creationTime = creationTime;
        this.lastWorldInteraction = lastWorldInteraction;
    }

    public final String formatCreationDate() {
        return new SimpleDateFormat("dd.MM.yyyy").format(new Date());
    }

}
