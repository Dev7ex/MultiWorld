package com.dev7ex.multiworld.command;

import com.dev7ex.common.bukkit.command.SimpleCommand;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;

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

    public BackCommand(final BukkitPlugin plugin) {
        super(plugin);
        super.setUsage("§cSyntax: /back");
        super.setPermission("multiworld.command.back");
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        if(!(commandSender instanceof Player)) {
            return true;
        }

        if(!commandSender.hasPermission(super.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if(arguments.length != 0) {
            commandSender.sendMessage(super.getPrefix() + super.getUsage());
            return true;
        }
        final Player player = (Player) commandSender;
        final WorldUser worldUser = MultiWorldPlugin.getInstance().getWorldUserService().getUsers().get(player.getUniqueId());

        if(worldUser.getProperties().getLastWorld() == null) {
            player.sendMessage(super.getPrefix() + "§7There is no world you can be teleported");
            return true;
        }
        final World world = Bukkit.getWorld(worldUser.getProperties().getLastWorld());

        if(world == null) {
            worldUser.getProperties().setLastWorld(null);
            player.sendMessage(super.getPrefix() + "§7This world is no longer active");
            return true;
        }

        if(worldUser.getProperties().getLastWorld().equalsIgnoreCase(player.getWorld().getName())) {
            player.sendMessage(super.getPrefix() + "§7You are already in this world");
            return true;
        }
        final Location teleportLocation = worldUser.getProperties().getLastWorldLocation() == null ? world.getSpawnLocation() : worldUser.getProperties().getLastWorldLocation();
        MultiWorldPlugin.getInstance().getWorldManager().teleportWorld(worldUser, world, teleportLocation);
        return true;
    }

}
