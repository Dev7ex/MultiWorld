package com.dev7ex.multiworld.user;

import com.dev7ex.common.collect.map.ParsedMap;
import com.dev7ex.common.io.file.configuration.ConfigurationProvider;
import com.dev7ex.common.io.file.configuration.FileConfiguration;
import com.dev7ex.common.io.file.configuration.JsonConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUser;
import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUserConfiguration;
import com.dev7ex.multiworld.api.bukkit.world.location.BukkitWorldLocation;
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
public class UserConfiguration implements BukkitWorldUserConfiguration {

    private final File configurationFile;
    private final FileConfiguration fileConfiguration;

    @SneakyThrows
    public UserConfiguration(@NotNull final BukkitWorldUser user) {
        this.configurationFile = new File(MultiWorldPlugin.getInstance().getSubFolder("user")
                + File.separator + user.getUniqueId().toString() + ".json");

        if (!this.configurationFile.exists()) {
            this.configurationFile.createNewFile();
        }
        this.fileConfiguration = ConfigurationProvider.getProvider(JsonConfiguration.class).load(this.configurationFile);
        this.fileConfiguration.addDefault(WorldUserProperty.UNIQUE_ID.getStoragePath(), user.getUniqueId().toString());
        this.fileConfiguration.addDefault(WorldUserProperty.NAME.getStoragePath(), user.getName());
        this.fileConfiguration.addDefault(WorldUserProperty.LAST_LOCATION.getStoragePath(), null);
        this.fileConfiguration.addDefault(WorldUserProperty.FIRST_LOGIN.getStoragePath(), System.currentTimeMillis());
        this.fileConfiguration.addDefault(WorldUserProperty.LAST_LOGIN.getStoragePath(), System.currentTimeMillis());
        this.save();
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
                    userData.put(property, BukkitWorldLocation.builder()
                            .setWorldName(this.fileConfiguration.getString(property.getStoragePath() + ".world"))
                            .setX(this.fileConfiguration.getDouble(property.getStoragePath() + ".x"))
                            .setY(this.fileConfiguration.getDouble(property.getStoragePath() + ".y"))
                            .setZ(this.fileConfiguration.getDouble(property.getStoragePath() + ".z"))
                            .setPitch(this.fileConfiguration.getDouble(property.getStoragePath() + ".pitch"))
                            .setYaw(this.fileConfiguration.getDouble(property.getStoragePath() + ".yaw"))
                            .build());
                    break;

                case FIRST_LOGIN: case LAST_LOGIN:
                    userData.put(property, this.fileConfiguration.getLong(property.getStoragePath(), System.currentTimeMillis()));
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
                    userData.put(property, BukkitWorldLocation.builder()
                            .setWorldName(this.fileConfiguration.getString(property.getStoragePath() + ".world"))
                            .setX(this.fileConfiguration.getDouble(property.getStoragePath() + ".x"))
                            .setY(this.fileConfiguration.getDouble(property.getStoragePath() + ".y"))
                            .setZ(this.fileConfiguration.getDouble(property.getStoragePath() + ".z"))
                            .setPitch(this.fileConfiguration.getDouble(property.getStoragePath() + ".pitch"))
                            .setYaw(this.fileConfiguration.getDouble(property.getStoragePath() + ".yaw"))
                            .build());
                    break;

                case FIRST_LOGIN: case LAST_LOGIN:
                    userData.put(property, this.fileConfiguration.getLong(property.getStoragePath()));
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
                    final BukkitWorldLocation worldLocation = userData.getValue(property);
                    System.out.println(worldLocation.toString());

                    this.fileConfiguration.set(property.getStoragePath() + ".world", worldLocation.getWorldName());
                    this.fileConfiguration.set(property.getStoragePath() + ".x", worldLocation.getX());
                    this.fileConfiguration.set(property.getStoragePath() + ".y", worldLocation.getY());
                    this.fileConfiguration.set(property.getStoragePath() + ".z", worldLocation.getZ());
                    this.fileConfiguration.set(property.getStoragePath() + ".pitch", worldLocation.getPitch());
                    this.fileConfiguration.set(property.getStoragePath() + ".yaw", worldLocation.getYaw());
                    break;

                case FIRST_LOGIN: case LAST_LOGIN:
                    this.fileConfiguration.set(property.getStoragePath(), userData.getLong(property));
                    break;
            }
        }
        this.save();
    }

    @Override
    @SneakyThrows
    public void save() {
        ConfigurationProvider.getProvider(JsonConfiguration.class).save(this.fileConfiguration, this.configurationFile);
    }

}
