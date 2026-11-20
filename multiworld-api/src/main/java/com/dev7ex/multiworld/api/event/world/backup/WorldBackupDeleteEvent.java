package com.dev7ex.multiworld.api.event.world.backup;

import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.backup.WorldBackup;
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
public class WorldBackupDeleteEvent extends WorldBackupEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;
    private boolean notifyOnCancel = true;

    public WorldBackupDeleteEvent(@NotNull final WorldContext worldContext, @NotNull final Audience audience, @NotNull final WorldBackup worldBackup) {
        super(worldContext, audience, worldBackup);
    }

    public static HandlerList getHandlerList() {
        return WorldBackupDeleteEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldBackupDeleteEvent.HANDLERS;
    }

}

