package com.dev7ex.multiworld.user;

import com.dev7ex.common.bukkit.plugin.module.PluginModule;
import com.dev7ex.common.collect.map.ParsedMap;
import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUser;
import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUserProvider;
import com.dev7ex.multiworld.api.user.WorldUserConfiguration;
import com.dev7ex.multiworld.api.user.WorldUserProperty;
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
 * Provides management for BukkitWorldUser instances within the MultiWorld system.
 * This class handles user registration, retrieval, and saving of user data.
 *
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public class UserProvider implements PluginModule, BukkitWorldUserProvider {

    private final Map<UUID, BukkitWorldUser> users = new HashMap<>();

    /**
     * Enables the user provider module.
     * Registers all currently online players and loads their configuration.
     */
    @Override
    public void onEnable() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final BukkitWorldUser user = new User(player.getUniqueId(), player.getName());
            final WorldUserConfiguration userConfiguration = new UserConfiguration(user);
            final ParsedMap<WorldUserProperty, Object> userData = userConfiguration.read();

            user.setLastLogin(userData.getLong(WorldUserProperty.LAST_LOGIN));
            user.setLastLocation(userData.getValue(WorldUserProperty.LAST_LOCATION));
            user.setConfiguration(userConfiguration);

            this.registerUser(user);
        }
    }

    /**
     * Disables the user provider module.
     * Saves the state of all registered users and clears the user list.
     */
    @Override
    public void onDisable() {
        for (final BukkitWorldUser user : this.users.values()) {
            this.saveUser(user, WorldUserProperty.LAST_LOCATION);
        }
        this.users.clear();
    }

    /**
     * Registers a new user in the system.
     *
     * @param user The user to register.
     */
    @Override
    public void registerUser(@NotNull final BukkitWorldUser user) {
        this.users.put(user.getUniqueId(), user);
    }

    /**
     * Unregisters a user from the system using their unique ID.
     *
     * @param uniqueId The unique ID of the user to unregister.
     */
    @Override
    public void unregisterUser(@NotNull final UUID uniqueId) {
        this.users.remove(uniqueId);
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param uniqueId The unique ID of the user.
     * @return An Optional containing the user if found, or an empty Optional if not found.
     */
    @Override
    public Optional<BukkitWorldUser> getUser(@NotNull final UUID uniqueId) {
        return Optional.ofNullable(this.users.get(uniqueId));
    }

    /**
     * Retrieves a user by their name.
     *
     * @param name The name of the user.
     * @return An Optional containing the user if found, or an empty Optional if not found.
     */
    @Override
    public Optional<BukkitWorldUser> getUser(@NotNull final String name) {
        return this.users.values().stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    /**
     * Saves the state of the specified user.
     *
     * @param user The user whose state is to be saved.
     */
    @Override
    public void saveUser(@NotNull final BukkitWorldUser user) {
        this.saveUser(user, WorldUserProperty.UNIQUE_ID,
                WorldUserProperty.NAME,
                WorldUserProperty.LAST_LOCATION,
                WorldUserProperty.LAST_LOGIN);
    }

    /**
     * Saves specific properties of the specified user.
     *
     * @param user The user whose properties are to be saved.
     * @param properties The properties to save.
     */
    @Override
    public void saveUser(@NotNull final BukkitWorldUser user, @NotNull final WorldUserProperty... properties) {
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
