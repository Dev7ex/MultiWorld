package com.dev7ex.multiworld.listener.player;

import com.dev7ex.common.collect.map.ParsedMap;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.event.MultiWorldListener;
import com.dev7ex.multiworld.api.bukkit.event.user.WorldUserLoginEvent;
import com.dev7ex.multiworld.api.bukkit.event.user.WorldUserLogoutEvent;
import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUser;
import com.dev7ex.multiworld.api.user.WorldUserConfiguration;
import com.dev7ex.multiworld.api.user.WorldUserProperty;
import com.dev7ex.multiworld.user.User;
import com.dev7ex.multiworld.user.UserConfiguration;
import com.dev7ex.multiworld.util.PluginUpdater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Handles player connection events such as login, join, and quit.
 * This class listens to relevant events and manages user data accordingly.
 *
 * @since 18.06.2023
 */
public class PlayerConnectionListener extends MultiWorldListener {

    /**
     * Constructs a new PlayerConnectionListener.
     *
     * @param multiWorldApi The MultiWorldBukkitApi instance.
     */
    public PlayerConnectionListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        super(multiWorldApi);
    }

    /**
     * Handles the player login event.
     * Registers the player as a BukkitWorldUser and loads their configuration.
     *
     * @param event The PlayerLoginEvent.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerLogin(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        final BukkitWorldUser user = new User(player.getUniqueId(), player.getName());
        final WorldUserConfiguration userConfiguration = new UserConfiguration(user);
        final ParsedMap<WorldUserProperty, Object> userData = userConfiguration.read();

        user.setLastLocation(userData.getValue(WorldUserProperty.LAST_LOCATION));
        user.setFirstLogin(userData.getLong(WorldUserProperty.FIRST_LOGIN));
        user.setLastLogin(userData.getLong(WorldUserProperty.LAST_LOGIN));
        user.setConfiguration(userConfiguration);

        super.getUserProvider().registerUser(user);

        Bukkit.getPluginManager().callEvent(new WorldUserLoginEvent(user));
    }

    /**
     * Handles the player join event.
     * Sends an update message to the player if updates are available and they have the necessary permission.
     *
     * @param event The PlayerJoinEvent.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final PluginUpdater updater = MultiWorldPlugin.getInstance().getUpdater();

        if ((updater.isUpdateAvailable()) && (player.isOp())) {
            player.sendMessage(super.getConfiguration().getPrefix() + ChatColor.GRAY + " A new update for MultiWorld is available");
            player.sendMessage(super.getConfiguration().getPrefix() + ChatColor.GRAY + " Please note that if you do not do the update then there may be problems or individual functions may be disabled");
            player.sendMessage(super.getConfiguration().getPrefix() + ChatColor.GRAY + " https://modrinth.com/plugin/multiworld-bukkit");
        }
    }

    /**
     * Handles the player quit event.
     * Saves the player's data and unregisters them from the user provider.
     *
     * @param event The PlayerQuitEvent.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final BukkitWorldUser user = super.getUserProvider()
                .getUser(player.getUniqueId())
                .orElseThrow();

        super.getUserProvider().saveUser(user, WorldUserProperty.LAST_LOGIN,
                WorldUserProperty.LAST_LOCATION);
        super.getUserProvider().unregisterUser(player.getUniqueId());

        Bukkit.getPluginManager().callEvent(new WorldUserLogoutEvent(user));
    }

}
