package com.dev7ex.multiworld.api.event.world;

import com.dev7ex.multiworld.api.world.WorldContext;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 13.04.2025
 */
@Getter(AccessLevel.PUBLIC)
public abstract class WorldEvent extends Event {

    private final WorldContext context;
    private final Audience audience;

    public WorldEvent(@NotNull final WorldContext context, @NotNull final Audience audience) {
        this.context = context;
        this.audience = audience;
    }

}
