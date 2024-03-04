package com.dev7ex.multiworld.user;

import com.dev7ex.common.collect.map.ParsedMap;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.location.BukkitWorldLocation;
import com.dev7ex.multiworld.api.user.WorldUser;
import com.dev7ex.multiworld.api.user.WorldUserConfiguration;
import com.dev7ex.multiworld.api.user.WorldUserProperty;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public class UserConfiguration implements WorldUserConfiguration {

    private final File configurationFile;
    private final YamlConfiguration fileConfiguration;

    @SneakyThrows
    public UserConfiguration(@NotNull final WorldUser user) {
        this.configurationFile = new File(MultiWorldPlugin.getInstance().getSubFolder("user")
                + File.separator + user.getUniqueId().toString() + ".yml");

        if (!this.configurationFile.exists()) {
            this.configurationFile.createNewFile();
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configurationFile);
        this.fileConfiguration.addDefault(WorldUserProperty.UNIQUE_ID.getStoragePath(), user.getUniqueId().toString());
        this.fileConfiguration.addDefault(WorldUserProperty.NAME.getStoragePath(), user.getName());
        this.fileConfiguration.addDefault(WorldUserProperty.LAST_LOCATION.getStoragePath(), null);
        this.fileConfiguration.options().copyDefaults(true);
        this.saveFile();
    }

    @Override
    public ParsedMap<WorldUserProperty, Object> read() {
        final ParsedMap<WorldUserProperty, Object> userData = new ParsedMap<>();

        Arrays.stream(WorldUserProperty.values()).forEach(property -> {
            switch (property) {
                case UNIQUE_ID:
                    userData.put(property, UUID.fromString(Objects.requireNonNull(this.fileConfiguration.getString(property.getStoragePath()))));
                    break;

                case NAME:
                    userData.put(property, this.fileConfiguration.getString(property.getStoragePath()));
                    break;

                case LAST_LOCATION:
                    userData.put(property, this.fileConfiguration.getSerializable(property.getStoragePath(), BukkitWorldLocation.class));
                    break;
            }
        });
        return userData;
    }

    @Override
    public ParsedMap<WorldUserProperty, Object> read(final WorldUserProperty... properties) {
        if (properties == null) {
            return this.read();
        }
        final ParsedMap<WorldUserProperty, Object> userData = new ParsedMap<>();

        for (final WorldUserProperty property : properties) {
            switch (property) {
                case UNIQUE_ID:
                    userData.put(property, UUID.fromString(Objects.requireNonNull(this.fileConfiguration.getString(property.getStoragePath()))));
                    break;

                case NAME:
                    userData.put(property, this.fileConfiguration.getString(property.getStoragePath()));
                    break;

                case LAST_LOCATION:
                    userData.put(property, this.fileConfiguration.getSerializable(property.getStoragePath(), BukkitWorldLocation.class));
                    break;

                default:
                    break;
            }
        }
        return userData;
    }

    @Override
    public void write(final ParsedMap<WorldUserProperty, Object> userData) {
        for (final WorldUserProperty property : userData.keySet()) {
            switch (property) {
                case UNIQUE_ID:
                    this.fileConfiguration.set(property.getStoragePath(), userData.getUUID(property).toString());
                    break;

                case NAME:
                    this.fileConfiguration.set(property.getStoragePath(), userData.getString(property));
                    break;

                case LAST_LOCATION:
                    this.fileConfiguration.set(property.getStoragePath(), userData.get(property));
                    break;
            }
        }
        this.saveFile();
    }

    @Override
    @SneakyThrows
    public void saveFile() {
        this.fileConfiguration.save(this.configurationFile);
    }

}
