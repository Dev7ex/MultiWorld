package com.dev7ex.multiworld.world.backup;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.backup.WorldBackup;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Objects;

/**
 * @author Dev7ex
 * @since 13.04.2025
 */
@Getter(AccessLevel.PUBLIC)
@ToString
public class DefaultWorldBackup implements WorldBackup {

    private final WorldContext context;
    private final long timestamp;

    public DefaultWorldBackup(@NotNull final WorldContext context, final long timestamp) {
        this.context = context;
        this.timestamp = timestamp;
    }

    @Override
    public @NotNull String getName() {
        return String.valueOf(this.timestamp);
    }

    @Override
    public @NotNull Path getPath() {
        return Objects.requireNonNull(MultiWorldPlugin.getInstance().getBackupDirectory())
                .toPath()
                .resolve(this.context.getName())
                .resolve(this.getName() + ".zip");
    }

}