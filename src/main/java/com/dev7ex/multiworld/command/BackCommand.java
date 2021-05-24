package com.dev7ex.multiworld.command;

import com.dev7ex.common.bukkit.command.SimpleCommand;

import com.dev7ex.multiworld.MultiWorldConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.user.WorldUser;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Dev7ex
 * @since 20.05.2021
 *
 */

public final class BackCommand extends SimpleCommand {

    private final MultiWorldConfiguration configuration;

    public BackCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage(plugin.getConfiguration().getMessage("usage").replaceAll("%command%", "/back"));
        super.setPermission("multiworld.command.back");
        this.configuration = plugin.getConfiguration();
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(super.getConfiguration().getMessageSafe("only-player-command"));
            return true;
        }
        final Player player = (Player) commandSender;
        final WorldUser worldUser = MultiWorldPlugin.getInstance().getWorldUserService().getUsers().get(player.getUniqueId());

        if (!player.hasPermission(super.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 0) {
            commandSender.sendMessage(super.getUsage());
            return true;
        }

        if (worldUser.getProperties().getLastWorld() == null) {
            player.sendMessage(this.configuration.getMessage("back.world-not-found"));
            return true;
        }
        final World world = Bukkit.getWorld(worldUser.getProperties().getLastWorld());

        if (world == null) {
            worldUser.getProperties().setLastWorld(null);
            player.sendMessage(this.configuration.getMessage(this.configuration.getMessage("back.world-not-found")));
            return true;
        }

        if (worldUser.getProperties().getLastWorld().equalsIgnoreCase(player.getWorld().getName())) {
            player.sendMessage(this.configuration.getMessage("back.world-already-there"));
            return true;
        }
        final Location teleportLocation = (worldUser.getProperties().getLastWorldLocation() == null ? world.getSpawnLocation() : worldUser.getProperties().getLastWorldLocation());
        MultiWorldPlugin.getInstance().getWorldManager().teleportWorld(player, teleportLocation);
        return true;
    }

}
