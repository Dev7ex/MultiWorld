package com.dev7ex.multiworld.api.world.backup;

import com.dev7ex.multiworld.api.world.WorldContext;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author Dev7ex
 * @since 13.04.2025
 */
public interface WorldBackup extends Comparable<WorldBackup> {

    @NotNull
    WorldContext getContext();

    long getTimestamp();

    @NotNull
    String getName();

    @NotNull
    Path getPath();

    @NotNull
    default Instant getCreatedAt() {
        return Instant.ofEpochMilli(this.getTimestamp());
    }

    @NotNull
    default String getFormattedName(@NotNull final DateTimeFormatter formatter) {
        return formatter.format(this.getCreatedAt().atZone(ZoneId.systemDefault()));
    }

    @Override
    default int compareTo(@NotNull final WorldBackup backup) {
        final int comparison = Long.compare(this.getTimestamp(), backup.getTimestamp());

        if (comparison != 0) {
            return comparison;
        }
        return this.getPath().compareTo(backup.getPath());
    }

}