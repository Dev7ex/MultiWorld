package com.dev7ex.multiworld.api.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Dev7ex
 * @since 30.05.2025
 */
public class ZipUtil {

    private static final String SESSION_LOCK = "session.lock";

    @NotNull
    public static File zipWorld(@NotNull final File worldDirectory, @NotNull final File targetDirectory, final long timestamp) throws IOException {
        if (!worldDirectory.isDirectory()) {
            throw new IOException(String.format("World directory not found: %s", worldDirectory));
        }
        Files.createDirectories(targetDirectory.toPath());

        final Path sourcePath = worldDirectory.toPath().toRealPath();
        final Path zipPath = targetDirectory.toPath().resolve(timestamp + ".zip");
        final Path temporaryPath = targetDirectory.toPath().resolve(timestamp + ".zip.tmp");
        final List<Path> files;

        if (Files.exists(zipPath)) {
            throw new FileAlreadyExistsException(zipPath.toString());
        }

        try (final Stream<Path> stream = Files.walk(sourcePath)) {
            files = stream.filter(path -> Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS))
                    .filter(path -> !path.getFileName().toString().equals(ZipUtil.SESSION_LOCK))
                    .toList();
        }

        final FileChannel channel = FileChannel.open(temporaryPath, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);

        try {
            try (channel; final ZipOutputStream zipOutputStream = new ZipOutputStream(Channels.newOutputStream(channel))) {
                for (final Path path : files) {
                    final String entryName = sourcePath.relativize(path).toString().replace(File.separatorChar, '/');
                    final InputStream inputStream;

                    try {
                        inputStream = Files.newInputStream(path);

                    } catch (final NoSuchFileException exception) {
                        continue;
                    }

                    try (inputStream) {
                        zipOutputStream.putNextEntry(new ZipEntry(entryName));
                        inputStream.transferTo(zipOutputStream);
                        zipOutputStream.closeEntry();
                    }
                }
                zipOutputStream.finish();
                channel.force(true);
            }

            try {
                Files.move(temporaryPath, zipPath, StandardCopyOption.ATOMIC_MOVE);

            } catch (final AtomicMoveNotSupportedException exception) {
                Files.move(temporaryPath, zipPath);
            }

        } catch (final IOException | RuntimeException exception) {
            try {
                Files.deleteIfExists(temporaryPath);

            } catch (final IOException deleteException) {
                exception.addSuppressed(deleteException);
            }
            throw exception;
        }
        return zipPath.toFile();
    }

    public static void unzipWorld(@NotNull final File zipFile, @NotNull final File targetDirectory) throws IOException {
        if ((!zipFile.isFile()) || (!zipFile.getName().endsWith(".zip"))) {
            throw new IOException(String.format("Invalid ZIP file: %s", zipFile.getName()));
        }
        final Path targetPath = targetDirectory.toPath().toAbsolutePath().normalize();
        Files.createDirectories(targetPath);

        try (final ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile.toPath()))) {
            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {
                final Path outputPath;

                try {
                    outputPath = targetPath.resolve(entry.getName()).normalize();

                } catch (final InvalidPathException exception) {
                    throw new IOException(String.format("Invalid ZIP entry: %s", entry.getName()), exception);
                }

                if (!outputPath.startsWith(targetPath)) {
                    throw new IOException(String.format("ZIP entry is outside of the target directory: %s", entry.getName()));
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(outputPath);

                } else {
                    Files.createDirectories(outputPath.getParent());

                    try (final OutputStream outputStream = Files.newOutputStream(outputPath)) {
                        zipInputStream.transferTo(outputStream);
                    }
                }
                zipInputStream.closeEntry();
            }
        }
    }

}
