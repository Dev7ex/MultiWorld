package com.dev7ex.multiworld.listener.player;

import com.dev7ex.common.collect.map.ParsedMap;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApiConfiguration;
import com.dev7ex.multiworld.api.bukkit.event.MultiWorldListener;
import com.dev7ex.multiworld.api.user.WorldUser;
import com.dev7ex.multiworld.api.user.WorldUserConfiguration;
import com.dev7ex.multiworld.api.user.WorldUserProperty;
import com.dev7ex.multiworld.user.User;
import com.dev7ex.multiworld.user.UserConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public class PlayerConnectionListener extends MultiWorldListener {

    public PlayerConnectionListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        super(multiWorldApi);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerLogin(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        final WorldUser user = new User(player.getUniqueId(), player.getName());
        final WorldUserConfiguration userConfiguration = new UserConfiguration(user);
        final ParsedMap<WorldUserProperty, Object> userData = userConfiguration.read();

        user.setLastLocation(userData.getValue(WorldUserProperty.LAST_LOCATION));
        user.setLastLogin(userData.getLong(WorldUserProperty.LAST_LOGIN));
        user.setConfiguration(userConfiguration);

        super.getUserProvider().registerUser(user);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerLogin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if ((super.getConfiguration().getBoolean("settings.receive-update-message"))
                && (player.hasPermission("multiworld.update.notify"))
                && (MultiWorldPlugin.getInstance().getUpdateChecker().isUpdateAvailable())) {

            player.sendMessage(super.getConfiguration().getString(MultiWorldBukkitApiConfiguration.Entry.MESSAGES_GENERAL_UPDATE_MESSAGE_PLAYER.getPath())
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            player.sendMessage(super.getConfiguration().getString(MultiWorldBukkitApiConfiguration.Entry.MESSAGES_GENERAL_UPDATE_MESSAGE_VERSION_PLAYER.getPath())
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%current_version%", MultiWorldPlugin.getInstance().getDescription().getVersion())
                    .replaceAll("%new_version%", MultiWorldPlugin.getInstance().getUpdateChecker().getNewVersion()));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final WorldUser user = super.getUserProvider().getUser(player.getUniqueId()).orElseThrow();

        super.getUserProvider().saveUser(user, WorldUserProperty.LAST_LOCATION);
        super.getUserProvider().unregisterUser(player.getUniqueId());
    }

}
