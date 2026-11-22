
package com.dev7ex.multiworld.world;

import com.dev7ex.multicommon.api.configuration.*;
import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.util.FileUtil;
import com.dev7ex.multiworld.api.util.ParserUtil;
import com.dev7ex.multiworld.api.world.*;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @author Dev7ex
 * @since 13.04.2025
 */
@ConfigurationProperties(fileName = "world.json", provider = JsonConfiguration.class)
public final class DefaultWorldRepository extends Configuration implements WorldRepository {

    private final MultiWorldApi multiWorldApi;
    private boolean backupPending;

    public DefaultWorldRepository(@NotNull final MultiWorldApi multiWorldApi) {
        super(multiWorldApi);
        this.multiWorldApi = multiWorldApi;
    }


    @Override
    public synchronized void saveFile() {
        final File file = super.getConfigurationFile();
        final File temporaryFile = new File(file.getParentFile(), file.getName() + ".tmp");

        try {
            if (this.backupPending) {
                final File backup = this.createBackup();

                this.multiWorldApi.getLogger().log(Level.INFO, "Created backup {0} before saving {1}", new Object[]{backup.getName(), file.getName()});
                this.backupPending = false;
            }
            ConfigurationProvider.getProvider(super.getProvider()).save(super.getFileConfiguration(), temporaryFile);
            FileUtil.moveFile(temporaryFile, file);

        } catch (final IOException exception) {
            throw new UncheckedIOException("Can't save file " + file.getName(), exception);

        } finally {
            try {
                Files.deleteIfExists(temporaryFile.toPath());

            } catch (final IOException exception) {
                this.multiWorldApi.getLogger().log(
                        Level.WARNING,
                        exception,
                        () -> "Could not delete temporary file " + temporaryFile.getName()
                );
            }
        }
    }


    @Override
    public void insert(@NotNull final WorldContext context) {
        Objects.requireNonNull(context, "context");

        if (this.exists(context)) {
            throw new IllegalStateException("World already exists: " + context.getKey());
        }
        this.write(context);
        super.saveFile();
    }

    @Override
    public void delete(@NotNull final WorldContext context) {
        this.delete(Objects.requireNonNull(context, "context").getKey());
    }

    @Override
    public void delete(@NotNull final NamespacedKey worldKey) {
        Objects.requireNonNull(worldKey, "worldKey");

        super.getFileConfiguration().set(this.path(worldKey), null);
        super.saveFile();
    }

    @Override
    public boolean exists(@NotNull final WorldContext context) {
        return this.exists(Objects.requireNonNull(context, "context").getKey());
    }

    @Override
    public boolean exists(@NotNull final NamespacedKey worldKey) {
        return super.getFileConfiguration().contains(this.path(Objects.requireNonNull(worldKey, "worldKey")));
    }

    @Override
    public void deleteAll() {
        for (final String key : super.getFileConfiguration().getKeys()) {
            super.getFileConfiguration().set(key, null);
        }
        super.saveFile();
    }

    @Override
    public void update(@NotNull final WorldContext context, @NotNull final WorldProperty property, @NotNull final Object value) {
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(property, "property");
        Objects.requireNonNull(value, "value");

        if (!this.exists(context)) {
            throw new IllegalStateException("World does not exist: " + context.getKey());
        }
        super.getFileConfiguration().set(this.path(context.getKey()) + "." + property.getKey(), value);
        super.saveFile();
    }

    @Override
    @NotNull
    public Optional<WorldContext> find(@NotNull final NamespacedKey worldKey) {
        Objects.requireNonNull(worldKey, "worldKey");

        if (!this.exists(worldKey)) {
            return Optional.empty();
        }

        final String path = this.path(worldKey) + ".";
        final FileConfiguration configuration = super.getFileConfiguration();

        try {
            return Optional.of(DefaultWorldContext.builder()
                    .setKey(worldKey)
                    .setName(configuration.getString(path + WorldProperty.NAME.getKey(), worldKey.getKey()))
                    .setDisplayName(configuration.getString(path + WorldProperty.DISPLAY_NAME.getKey(), worldKey.getKey()))
                    .setColor(ParserUtil.parseTextColor(configuration.getString(path + WorldProperty.COLOR.getKey())))
                    .setIcon(ParserUtil.parseMaterial(configuration.getString(path + WorldProperty.ICON.getKey())))
                    .setCreatorName(configuration.getString(path + WorldProperty.CREATOR_NAME.getKey(), "Unknown"))
                    .setRegistrationTimestamp(configuration.getLong(path + WorldProperty.REGISTRATION_TIMESTAMP.getKey(), System.currentTimeMillis()))
                    .setGroupId(configuration.getInt(path + WorldProperty.GROUP_ID.getKey(), 0))
                    .setEnvironment(WorldEnvironment.valueOf(configuration.getString(path + WorldProperty.ENVIRONMENT.getKey(), "NORMAL")))
                    .setType(WorldType.valueOf(configuration.getString(path + WorldProperty.TYPE.getKey(), "NORMAL")))
                    .setSeed(configuration.getLong(path + WorldProperty.SEED.getKey(), 0))
                    .setGeneratorName(configuration.getString(path + WorldProperty.GENERATOR_NAME.getKey(), "Unknown"))
                    .build());
        } catch (final RuntimeException exception) {
            return Optional.empty();
        }
    }

    @Override
    @NotNull
    public Map<NamespacedKey, WorldContext> findAll() {
        return super.getFileConfiguration().getKeys().stream()
                .map(NamespacedKey::fromString)
                .filter(Objects::nonNull)
                .flatMap(key -> this.find(key)
                        .map(context -> Map.entry(key, context))
                        .stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void cleanup() {

    }

    private @NotNull File createBackup() throws IOException {
        final File file = super.getConfigurationFile();
        final File backupFile = new File(file.getParentFile(), String.format("%s.%d.bak", file.getName(), System.currentTimeMillis()));

        Files.copy(file.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return backupFile;
    }

    private void write(@NotNull final WorldContext context) {
        final String path = this.path(context.getKey()) + ".";
        final FileConfiguration configuration = super.getFileConfiguration();

        configuration.set(path + WorldProperty.NAME.getKey(), context.getName());
        configuration.set(path + WorldProperty.DISPLAY_NAME.getKey(), context.getDisplayName());
        configuration.set(path + WorldProperty.COLOR.getKey(), context.getColor().asHexString());
        configuration.set(path + WorldProperty.ICON.getKey(), context.getIcon().toString());
        configuration.set(path + WorldProperty.CREATOR_NAME.getKey(), context.getCreatorName());
        configuration.set(path + WorldProperty.REGISTRATION_TIMESTAMP.getKey(), context.getRegistrationTimestamp());
        configuration.set(path + WorldProperty.GROUP_ID.getKey(), context.getGroupId());
        configuration.set(path + WorldProperty.ENVIRONMENT.getKey(), context.getEnvironment().toString());
        configuration.set(path + WorldProperty.GENERATOR_NAME.getKey(), context.getGeneratorName());
        configuration.set(path + WorldProperty.SEED.getKey(), context.getSeed());
        configuration.set(path + WorldProperty.TYPE.getKey(), context.getType().toString());
    }

    @NotNull
    private String path(@NotNull final NamespacedKey worldKey) {
        return worldKey.asString();
    }
}
