package com.dev7ex.multiworld.util;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author MGG
 * @since 20.11.2026
 */
public class ZipUtilTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void zipAndUnzipKeepsAllFilesExceptSessionLock() throws IOException {
        final Path world = this.createWorld();
        final Path backups = this.temporaryDirectory.resolve("backups");

        final File zipFile = ZipUtil.zipWorld(world.toFile(), backups.toFile(), 123L);

        assertEquals(backups.resolve("123.zip").toFile(), zipFile);
        assertFalse(Files.exists(backups.resolve("123.zip.tmp")));

        final Path restored = this.temporaryDirectory.resolve("restored");
        ZipUtil.unzipWorld(zipFile, restored.toFile());

        assertEquals("level", Files.readString(restored.resolve("level.dat")));
        assertEquals("region", Files.readString(restored.resolve("region/r.0.0.mca")));
        assertEquals("nested", Files.readString(restored.resolve("data/paper/metadata.dat")));
        assertFalse(Files.exists(restored.resolve("session.lock")));
    }

    @Test
    void zipFollowsSymlinkedWorldDirectory() throws IOException {
        final Path world = this.createWorld();
        final Path link = this.temporaryDirectory.resolve("linked-world");

        try {
            Files.createSymbolicLink(link, world);
        } catch (final UnsupportedOperationException | IOException exception) {
            Assumptions.abort("Symbolic links are not supported");
        }
        final File zipFile = ZipUtil.zipWorld(link.toFile(), this.temporaryDirectory.resolve("backups").toFile(), 2L);
        final List<String> names = new ArrayList<>();

        try (final ZipFile file = new ZipFile(zipFile)) {
            file.stream().map(ZipEntry::getName).forEach(names::add);
        }

        assertTrue(names.contains("level.dat"));
        assertTrue(names.contains("region/r.0.0.mca"));
    }

    @Test
    void zipEntriesUseForwardSlashes() throws IOException {
        final File zipFile = ZipUtil.zipWorld(this.createWorld().toFile(), this.temporaryDirectory.resolve("backups").toFile(), 1L);
        final List<String> names = new ArrayList<>();

        try (final ZipFile file = new ZipFile(zipFile)) {
            file.stream().map(ZipEntry::getName).forEach(names::add);
        }

        assertTrue(names.contains("region/r.0.0.mca"));
        assertTrue(names.contains("data/paper/metadata.dat"));
        assertTrue(names.stream().noneMatch(name -> name.contains("\\")));
    }

    @Test
    void zipFailsForMissingWorldDirectory() {
        final Path backups = this.temporaryDirectory.resolve("backups");

        assertThrows(IOException.class, () -> ZipUtil.zipWorld(this.temporaryDirectory.resolve("missing").toFile(), backups.toFile(), 1L));
        assertFalse(Files.exists(backups.resolve("1.zip")));
    }

    @Test
    void failedZipLeavesNoPartialFiles() throws IOException {
        final Path world = this.createWorld();
        final File unreadable = world.resolve("region/r.0.0.mca").toFile();
        Assumptions.assumeTrue(unreadable.setReadable(false) && !unreadable.canRead());

        final Path backups = this.temporaryDirectory.resolve("backups");

        try {
            assertThrows(IOException.class, () -> ZipUtil.zipWorld(world.toFile(), backups.toFile(), 7L));
        } finally {
            unreadable.setReadable(true);
        }

        try (final Stream<Path> stream = Files.list(backups)) {
            assertEquals(0, stream.count());
        }
    }

    @Test
    void zipDoesNotOverwriteExistingBackup() throws IOException {
        final Path backups = Files.createDirectories(this.temporaryDirectory.resolve("backups"));
        Files.writeString(backups.resolve("5.zip"), "old");

        assertThrows(FileAlreadyExistsException.class, () -> ZipUtil.zipWorld(this.createWorld().toFile(), backups.toFile(), 5L));
        assertEquals("old", Files.readString(backups.resolve("5.zip")));
    }

    @Test
    void zipKeepsTemporaryFileOfAnotherBackup() throws IOException {
        final Path backups = Files.createDirectories(this.temporaryDirectory.resolve("backups"));
        Files.writeString(backups.resolve("6.zip.tmp"), "other");

        assertThrows(FileAlreadyExistsException.class, () -> ZipUtil.zipWorld(this.createWorld().toFile(), backups.toFile(), 6L));
        assertEquals("other", Files.readString(backups.resolve("6.zip.tmp")));
        assertFalse(Files.exists(backups.resolve("6.zip")));
    }

    @Test
    void unzipRejectsParentTraversal() throws IOException {
        final File zipFile = this.createZip("evil.zip", "../escaped.txt");
        final Path target = this.temporaryDirectory.resolve("target");

        assertThrows(IOException.class, () -> ZipUtil.unzipWorld(zipFile, target.toFile()));
        assertFalse(Files.exists(this.temporaryDirectory.resolve("escaped.txt")));
    }

    @Test
    void unzipRejectsAbsolutePaths() throws IOException {
        final Path outside = this.temporaryDirectory.resolve("absolute.txt").toAbsolutePath();
        final File zipFile = this.createZip("absolute.zip", outside.toString());

        assertThrows(IOException.class, () -> ZipUtil.unzipWorld(zipFile, this.temporaryDirectory.resolve("target").toFile()));
        assertFalse(Files.exists(outside));
    }

    @Test
    void unzipRejectsNonZipFile() throws IOException {
        final Path file = Files.writeString(this.temporaryDirectory.resolve("backup.txt"), "x");

        assertThrows(IOException.class, () -> ZipUtil.unzipWorld(file.toFile(), this.temporaryDirectory.resolve("target").toFile()));
    }

    private Path createWorld() throws IOException {
        final Path world = this.temporaryDirectory.resolve("world");
        Files.createDirectories(world.resolve("region"));
        Files.createDirectories(world.resolve("data/paper"));
        Files.writeString(world.resolve("level.dat"), "level");
        Files.writeString(world.resolve("session.lock"), "lock");
        Files.writeString(world.resolve("region/r.0.0.mca"), "region");
        Files.writeString(world.resolve("data/paper/metadata.dat"), "nested");
        return world;
    }

    private File createZip(final String fileName, final String entryName) throws IOException {
        final Path zipPath = this.temporaryDirectory.resolve(fileName);

        try (final ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            zipOutputStream.putNextEntry(new ZipEntry(entryName));
            zipOutputStream.write("payload".getBytes(StandardCharsets.UTF_8));
            zipOutputStream.closeEntry();
        }
        return zipPath.toFile();
    }

}
