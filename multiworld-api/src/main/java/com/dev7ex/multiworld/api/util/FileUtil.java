
package com.dev7ex.multiworld.api.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * @author Dev7ex
 * @since 22.11.2026
 */
@UtilityClass
public final class FileUtil {

    /**
     * Moves a file to the specified target location.
     * Attempts an atomic move first and falls back to a regular move
     * if atomic moves are not supported by the file system.
     * Replaces the target file if it already exists.
     *
     * @param source The source file to move
     * @param target The target file
     * @throws IOException If an I/O error occurs while moving the file
     * @author MGG
     * @since 22.11.2026
     */
    public static void moveFile(@NotNull final File source, @NotNull final File target) throws IOException {
        try {
            Files.move(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);

        } catch (final AtomicMoveNotSupportedException exception) {
            Files.move(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

}
