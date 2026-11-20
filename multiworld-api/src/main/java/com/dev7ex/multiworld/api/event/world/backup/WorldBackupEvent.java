package com.dev7ex.multiworld.api.event.world.backup;

import com.dev7ex.multiworld.api.event.world.WorldEvent;
import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.backup.WorldBackup;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 20.06.2025
 */
@Getter(AccessLevel.PUBLIC)
public abstract class WorldBackupEvent extends WorldEvent {

    private final WorldBackup worldBackup;

    public WorldBackupEvent(@NotNull final WorldContext worldContext, @NotNull final Audience audience, @NotNull final WorldBackup worldBackup) {
        super(worldContext, audience);
        this.worldBackup = worldBackup;
    }

}
