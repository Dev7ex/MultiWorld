package com.dev7ex.multiworld.user;

import com.dev7ex.common.bukkit.plugin.module.PluginModule;
import com.dev7ex.common.collect.map.ParsedMap;
import com.dev7ex.multiworld.api.user.WorldUser;
import com.dev7ex.multiworld.api.user.WorldUserConfiguration;
import com.dev7ex.multiworld.api.user.WorldUserProperty;
import com.dev7ex.multiworld.api.user.WorldUserProvider;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public class UserService implements PluginModule, WorldUserProvider {

    private final Map<UUID, WorldUser> users = new HashMap<>();

    @Override
    public void onEnable() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final WorldUser user = new User(player.getUniqueId(), player.getName());
            final WorldUserConfiguration userConfiguration = new UserConfiguration(user);
            final ParsedMap<WorldUserProperty, Object> userData = userConfiguration.read();

            user.setLastLogin(userData.getLong(WorldUserProperty.LAST_LOGIN));
            user.setLastLocation(userData.getValue(WorldUserProperty.LAST_LOCATION));
            user.setConfiguration(userConfiguration);

            this.registerUser(user);
        }
    }

    @Override
    public void onDisable() {
        for (final WorldUser user : this.users.values()) {
            this.saveUser(user, WorldUserProperty.LAST_LOCATION);
        }
        this.users.clear();
    }

    @Override
    public void registerUser(@NotNull final WorldUser user) {
        this.users.put(user.getUniqueId(), user);
    }

    @Override
    public void unregisterUser(@NotNull final UUID uniqueId) {
        this.users.remove(uniqueId);
    }

    @Override
    public Optional<WorldUser> getUser(@NotNull final UUID uniqueId) {
        return Optional.ofNullable(this.users.get(uniqueId));
    }

    @Override
    public Optional<WorldUser> getUser(@NotNull final String name) {
        return this.users.values().stream().filter(user -> user.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public void saveUser(@NotNull final WorldUser user) {
        this.saveUser(user, WorldUserProperty.UNIQUE_ID,
                WorldUserProperty.NAME,
                WorldUserProperty.LAST_LOCATION,
                WorldUserProperty.LAST_LOGIN);
    }

    @Override
    public void saveUser(@NotNull final WorldUser user, @NotNull final WorldUserProperty... properties) {
        final ParsedMap<WorldUserProperty, Object> data = new ParsedMap<>();

        for (final WorldUserProperty property : properties) {
            switch (property) {
                case UNIQUE_ID:
                    data.put(property, user.getUniqueId());
                    break;

                case NAME:
                    data.put(property, user.getName());
                    break;

                case LAST_LOCATION:
                    data.put(property, user.getLastLocation());
                    break;

                case LAST_LOGIN:
                    data.put(property, user.getLastLogin());
                    break;

                default:
                    this.saveUser(user);
                    break;
            }
        }
        user.getConfiguration().write(data);
    }

}
