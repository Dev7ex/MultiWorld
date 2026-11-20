package com.dev7ex.multiworld.api.event.world.backup;

import com.dev7ex.multiworld.api.event.world.WorldEvent;
import com.dev7ex.multiworld.api.world.WorldContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 22.06.2025
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class WorldBackupClearEvent extends WorldEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;
    private boolean notifyOnCancel = true;

    public WorldBackupClearEvent(@NotNull final WorldContext worldContext, @NotNull final Audience audience) {
        super(worldContext, audience);
    }

    public static HandlerList getHandlerList() {
        return WorldBackupClearEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldBackupClearEvent.HANDLERS;
    }

}
