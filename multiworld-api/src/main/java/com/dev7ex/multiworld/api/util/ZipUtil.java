package com.dev7ex.multiworld.util;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Dev7ex
 * @since 30.05.2025
 */
public class ZipUtil {

    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss");

    public static void zipWorld(@NotNull final File worldDirectory, @NotNull final File targetDirectory, final long timestamp) {
        if (!worldDirectory.isDirectory()) {
            throw new IllegalArgumentException("worldFolder must be a directory");
        }
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        }

        final String zipFileName = timestamp + ".zip";
        final File zipFile = new File(targetDirectory, zipFileName);

        try (final FileOutputStream fileOutputStream = new FileOutputStream(zipFile)) {
            try (final ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
                final Path sourcePath = worldDirectory.toPath();
                Files.walk(sourcePath)
                        .filter(path -> !Files.isDirectory(path))
                        .filter(path -> !path.getFileName().toString().equals("session.lock"))
                        .forEach(path -> {
                            final ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());

                            try (final InputStream inputStream = Files.newInputStream(path)) {
                                zipOutputStream.putNextEntry(zipEntry);
                                inputStream.transferTo(zipOutputStream);
                                zipOutputStream.closeEntry();

                            } catch (final IOException exception) {
                                exception.printStackTrace();
                            }
                        });
            }

        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void unzipWorld(final File zipFile, final File targetDir) throws IOException {
        if (!zipFile.exists() || !zipFile.getName().endsWith(".zip")) {
            throw new IllegalArgumentException("Invalid ZIP file: " + zipFile.getName());
        }

        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        try (final ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                final File outFile = new File(targetDir, entry.getName());
                if (entry.isDirectory()) {
                    outFile.mkdirs();
                } else {
                    outFile.getParentFile().mkdirs();
                    try (final OutputStream os = new FileOutputStream(outFile)) {
                        zis.transferTo(os);
                    }
                }
                zis.closeEntry();
            }
        }
    }

}
