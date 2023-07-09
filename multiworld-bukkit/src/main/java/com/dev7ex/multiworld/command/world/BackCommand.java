package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.event.user.WorldUserTeleportWorldEvent;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.user.WorldUser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@CommandProperties(name = "back", permission = "multiworld.command.world.back")
public class BackCommand extends BukkitCommand {

    public BackCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(super.getConfiguration().getString("only-player-command")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }

        if (arguments.length != 1) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.back.usage")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }
        final Player player = (Player) commandSender;
        final WorldUser user = MultiWorldPlugin.getInstance().getUserProvider().getUser(player.getUniqueId()).orElseThrow();

        if (user.getLastLocation() == null) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.back.world-not-exists")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%world_name%", player.getWorld().getName()));
            return true;
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(user.getLastLocation().getWorldName()).isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-not-exists")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%world_name%", player.getWorld().getName()));
            return true;
        }
        final BukkitWorldHolder currentWorldHolder = MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(player.getWorld().getName()).orElseThrow();
        final BukkitWorldHolder nextWorldHolder = MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(user.getLastLocation().getWorldName()).orElseThrow();

        if (!nextWorldHolder.isLoaded()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-not-loaded")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%world_name%", user.getLastLocation().getWorldName()));
            return true;
        }

        if (user.getLastLocation().getWorldName().equalsIgnoreCase(player.getWorld().getName())) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.teleport.sender-already-there")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%world_name%", player.getWorld().getName()));
            return true;
        }
        final WorldUserTeleportWorldEvent event = new WorldUserTeleportWorldEvent(user, currentWorldHolder, nextWorldHolder);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return true;
        }

        final Location teleportLocation = nextWorldHolder.getWorld().getSpawnLocation();
        player.teleport(teleportLocation);
        return true;
    }

}
