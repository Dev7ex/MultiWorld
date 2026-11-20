package com.dev7ex.multiworld.world.backup;

import com.dev7ex.multicommon.api.util.Files;
import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.WorldRegistry;
import com.dev7ex.multiworld.api.world.backup.WorldBackup;
import com.dev7ex.multiworld.api.world.backup.WorldBackupRegistry;
import com.dev7ex.multiworld.module.DefaultWorldModule;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dev7ex
 * @since 13.04.2025
 */
@Getter(AccessLevel.PUBLIC)
public class DefaultWorldBackupRegistry extends DefaultWorldModule implements WorldBackupRegistry {

    private final Map<WorldContext, NavigableSet<WorldBackup>> worldBackups = new HashMap<>();
    private final Set<NamespacedKey> lockedWorlds = ConcurrentHashMap.newKeySet();
    private final WorldLocalizationRegistry localizationRegistry;
    private final WorldRegistry worldRegistry;
    private final File backupDirectory;

    public DefaultWorldBackupRegistry(@NotNull final MultiWorldApi multiWorldApi) {
        super(multiWorldApi);

        this.localizationRegistry = multiWorldApi.getLocalizationRegistry();
        this.worldRegistry = multiWorldApi.getWorldRegistry();
        this.backupDirectory = multiWorldApi.getBackupDirectory();
    }

    @Override
    public void onEnable() {
        // Initialize backup directories and tracking structures for all existing worlds.
        for (final WorldContext worldContext : this.worldRegistry.getWorlds()) {
            final File worldBackupDirectory = new File(this.backupDirectory, worldContext.getName());

            if (!worldBackupDirectory.exists()) {
                worldBackupDirectory.mkdirs();
            }

            this.worldBackups.putIfAbsent(worldContext, new TreeSet<>(Comparator.comparing(WorldBackup::getCreatedAt)));
        }

        // Load existing backups.
        final File[] worldFolders = this.backupDirectory.listFiles(File::isDirectory);

        if (worldFolders == null) {
            return;
        }

        for (final File worldFolder : worldFolders) {
            final String worldName = worldFolder.getName();
            final Optional<WorldContext> optionalContext = this.worldRegistry.find(worldName);

            if (optionalContext.isEmpty()) {
                continue;
            }

            final WorldContext worldContext = optionalContext.get();
            final File[] backupFiles = worldFolder.listFiles(file -> file.isFile() && file.getName().endsWith(".zip"));

            if (backupFiles == null) {
                continue;
            }

            final NavigableSet<WorldBackup> backups = this.worldBackups.computeIfAbsent(
                    worldContext,
                    context -> new TreeSet<>(Comparator.comparing(WorldBackup::getCreatedAt))
            );

            for (final File file : backupFiles) {
                final String fileName = file.getName();
                final long timeStamp;

                try {
                    final String timestampPart = fileName.substring(0, fileName.length() - 4);
                    timeStamp = Long.parseLong(timestampPart);
                } catch (final NumberFormatException exception) {
                    continue;
                }

                final DefaultWorldBackup worldBackup = new DefaultWorldBackup(worldContext, timeStamp);
                backups.add(worldBackup);
            }
        }

        super.getMultiWorldApi().getLogger().info(
                "Found %d backup folder(s).".formatted(
                        Files.getFiles(Objects.requireNonNull(this.backupDirectory)).size()
                )
        );
    }

    @Override
    public void onDisable() {
        this.lockedWorlds.clear();
        this.worldBackups.clear();
    }

    @Override
    public void register(@NotNull final WorldContext context, @NotNull final WorldBackup backup) {
        this.worldBackups
                .computeIfAbsent(context, holder -> new TreeSet<>(Comparator.comparing(WorldBackup::getCreatedAt)))
                .add(backup);
    }

    @Override
    public void unregister(@NotNull final WorldContext context, @NotNull final WorldBackup backup) {
        final NavigableSet<WorldBackup> backups = this.worldBackups.get(context);

        if (backups != null) {
            backups.remove(backup);
        }
    }

    @Override
    public void unregisterAll(@NotNull final WorldContext context) {
        final NavigableSet<WorldBackup> backups = this.worldBackups.get(context);

        if (backups != null) {
            backups.clear();
        }
    }

    @Override
    public boolean hasBackups(@NotNull final WorldContext context) {
        final NavigableSet<WorldBackup> backups = this.worldBackups.get(context);

        return backups != null && !backups.isEmpty();
    }

    @Override
    public int getBackupCount(@NotNull final WorldContext context) {
        return this.getBackups(context).size();
    }

    @Override
    public @NonNull Optional<WorldBackup> find(@NotNull final Audience audience, @NotNull final WorldContext context, @NotNull final String formattedName) {
        final DateTimeFormatter formatter = this.localizationRegistry.getDateTimeFormat(audience);

        return this.getBackups(context)
                .stream()
                .filter(worldBackup -> worldBackup.getFormattedName(formatter).equalsIgnoreCase(formattedName))
                .findFirst();
    }

    @Override
    public @NonNull Optional<WorldBackup> findLatest(@NotNull final WorldContext context) {
        final NavigableSet<WorldBackup> backups = this.worldBackups.get(context);

        if (backups == null || backups.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(backups.last());
    }

    @Override
    public @NonNull NavigableSet<WorldBackup> getBackups(@NotNull final WorldContext context) {
        return this.worldBackups.get(context) == null ? Collections.emptyNavigableSet() : this.worldBackups.get(context);
    }

    @Override
    public @NonNull Set<WorldContext> getWorldsWithBackups() {
        return this.worldBackups.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparing(WorldContext::getName))
                .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);
    }

    @Override
    public boolean lock(@NotNull final WorldContext context) {
        return this.lockedWorlds.add(context.getKey());
    }

    @Override
    public void unlock(@NotNull final WorldContext context) {
        this.lockedWorlds.remove(context.getKey());
    }

    @Override
    public boolean isLocked(@NotNull final WorldContext context) {
        return this.lockedWorlds.contains(context.getKey());
    }

}
