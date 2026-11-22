package com.dev7ex.multiworld.world.group;

import com.dev7ex.multicommon.api.configuration.Configuration;
import com.dev7ex.multicommon.api.configuration.ConfigurationHolder;
import com.dev7ex.multicommon.api.configuration.ConfigurationProperties;
import com.dev7ex.multicommon.api.configuration.JsonConfiguration;
import com.dev7ex.multiworld.api.world.group.WorldGroup;
import com.dev7ex.multiworld.api.world.group.WorldGroupProperty;
import com.dev7ex.multiworld.api.world.group.WorldGroupRepository;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Dev7ex
 * @since 09.07.2025
 */
@ConfigurationProperties(fileName = "group.json", provider = JsonConfiguration.class)
public class DefaultWorldGroupRepository extends Configuration implements WorldGroupRepository {

    public DefaultWorldGroupRepository(@NotNull final ConfigurationHolder configurationHolder) {
        super(configurationHolder);
    }

    @Override
    public void insert(@NotNull final WorldGroup group) {
        final String path = group.getId() + ".";

        super.getFileConfiguration().set(path + WorldGroupProperty.NAME.getKey(), group.getName());
        super.getFileConfiguration().set(path + WorldGroupProperty.DISPLAY_NAME.getKey(), group.getDisplayName());
        super.getFileConfiguration().set(path + WorldGroupProperty.COLOR.getKey(), group.getColor().asHexString());
        super.getFileConfiguration().set(path + WorldGroupProperty.ICON.getKey(), group.getIcon().toString());
        super.getFileConfiguration().set(path + WorldGroupProperty.CREATOR_NAME.getKey(), group.getCreatorName());
        super.getFileConfiguration().set(path + WorldGroupProperty.REGISTRATION_TIMESTAMP.getKey(), group.getRegistrationTimestamp());
        super.saveFile();
    }

    @Override
    public void delete(@NotNull final WorldGroup group) {
        super.getFileConfiguration().set(String.valueOf(group.getId()), null);
        super.saveFile();
    }

    @Override
    public void delete(final int id) {
        super.getFileConfiguration().set(String.valueOf(id), null);
        super.saveFile();
    }

    @Override
    public boolean exists(@NotNull final WorldGroup group) {
        return super.getFileConfiguration().contains(String.valueOf(group.getId()));
    }

    @Override
    public boolean exists(final int id) {
        return super.getFileConfiguration().contains(String.valueOf(id));
    }

    @Override
    public void clear() {
        super.getFileConfiguration().getKeys().forEach(key ->
                super.getFileConfiguration().set(key, null)
        );
        super.saveFile();
    }

    @Override
    public void update(@NotNull final WorldGroup group, @NotNull final WorldGroupProperty property, @NotNull final Object value) {
        super.getFileConfiguration().set(group.getName() + property.getKey(), value);
        super.saveFile();
    }

    @Override
    public @NonNull Optional<WorldGroup> find(final int id) {
        if (!this.exists(id)) {
            return Optional.empty();
        }
        try {
            return Optional.of(new DefaultWorldGroupBuilder()
                    .setId(id)
                    .setName(super.getFileConfiguration().getString(id + "." + WorldGroupProperty.NAME.getKey()))
                    .setDisplayName(super.getFileConfiguration().getString(id + "." + WorldGroupProperty.DISPLAY_NAME.getKey()))
                    .setColor(TextColor.fromHexString(super.getFileConfiguration().getString(id + "." + WorldGroupProperty.COLOR.getKey())))
                    .setIcon(Material.valueOf(super.getFileConfiguration().getString(id + "." + WorldGroupProperty.ICON.getKey())))
                    .setCreatorName(super.getFileConfiguration().getString(id + "." + WorldGroupProperty.CREATOR_NAME.getKey()))
                    .setRegistrationTimestamp(super.getFileConfiguration().getLong(id + "." + WorldGroupProperty.REGISTRATION_TIMESTAMP.getKey()))
                    .build()
            );
        } catch (final Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public @NotNull Map<Integer, WorldGroup> findAll() {
        return super.getFileConfiguration().getKeys().stream()
                .flatMap(key -> this.find(Integer.parseInt(key))
                        .map(group -> Map.entry(Integer.parseInt(key), group))
                        .stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
