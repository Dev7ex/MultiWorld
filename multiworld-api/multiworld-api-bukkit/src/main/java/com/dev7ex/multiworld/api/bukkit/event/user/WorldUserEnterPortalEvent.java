package com.dev7ex.multiworld.api.bukkit.event.user;

import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.user.WorldUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.PortalType;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 29.06.2023
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class WorldUserEnterPortalEvent extends WorldUserEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private final BukkitWorldHolder currentWorldHolder;
    private final BukkitWorldHolder portalWorldHolder;
    private final PortalType portalType;

    public WorldUserEnterPortalEvent(@NotNull final WorldUser user, @NotNull final BukkitWorldHolder currentWorldHolder, @NotNull final BukkitWorldHolder portalWorldHolder, @NotNull final PortalType portalType) {
        super(user);
        this.currentWorldHolder = currentWorldHolder;
        this.portalWorldHolder = portalWorldHolder;
        this.portalType = portalType;
    }

    public static HandlerList getHandlerList() {
        return WorldUserEnterPortalEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldUserEnterPortalEvent.HANDLERS;
    }

}
