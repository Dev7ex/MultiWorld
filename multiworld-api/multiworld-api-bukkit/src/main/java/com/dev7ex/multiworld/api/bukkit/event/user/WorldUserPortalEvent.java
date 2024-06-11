package com.dev7ex.multiworld.api.bukkit.event.user;

import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUser;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.PortalType;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a user enters a portal in a world.
 * It extends WorldUserEvent.
 * It provides information about the user, the current world they are in, the portal world they will be teleported to, and the type of portal.
 *
 * @author Dev7ex
 * @since 29.06.2023
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class WorldUserPortalEvent extends WorldUserEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private final BukkitWorldHolder currentWorldHolder;
    private final BukkitWorldHolder portalWorldHolder;
    private final PortalType portalType;

    /**
     * Constructs a new WorldUserEnterPortalEvent.
     *
     * @param user               The user entering the portal.
     * @param currentWorldHolder The BukkitWorldHolder of the current world the user is in.
     * @param portalWorldHolder  The BukkitWorldHolder of the portal world the user will be teleported to.
     * @param portalType         The type of portal the user is entering.
     */
    public WorldUserPortalEvent(@NotNull final BukkitWorldUser user, @NotNull final BukkitWorldHolder currentWorldHolder, @NotNull final BukkitWorldHolder portalWorldHolder, @NotNull final PortalType portalType) {
        super(user);
        this.currentWorldHolder = currentWorldHolder;
        this.portalWorldHolder = portalWorldHolder;
        this.portalType = portalType;
    }

    /**
     * Gets the HandlerList for this event.
     *
     * @return The HandlerList for this event.
     */
    public static HandlerList getHandlerList() {
        return WorldUserPortalEvent.HANDLERS;
    }

    /**
     * Gets the handlers for this event.
     *
     * @return The handlers for this event.
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldUserPortalEvent.HANDLERS;
    }

}
