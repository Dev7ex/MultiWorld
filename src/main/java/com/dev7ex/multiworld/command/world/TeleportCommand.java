package com.dev7ex.multiworld.command.world;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;

import com.google.common.collect.Lists;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
public final class TeleportCommand extends WorldSubCommand implements TabCompleter {

    public TeleportCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage(plugin.getConfiguration().getUsage().replaceAll("%command%", "/world teleport <Player> <World>"));
        super.setAliases(Collections.singletonList("tp"));
        super.setPermission("multiworld.command.world.teleport");
    }

    @Override
    public final boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!commandSender.hasPermission(this.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 3) {
            commandSender.sendMessage(super.getUsage());
            return true;
        }

        if (!super.worldManager.getWorldProperties().containsKey(arguments[2])) {
            commandSender.sendMessage(super.configuration.getWorldMessage("general.not-exists").replaceAll("%world%", arguments[1]));
            return true;
        }
        final World world = Bukkit.getWorld(arguments[2]);

        if (world == null) {
            commandSender.sendMessage(super.configuration.getWorldMessage("loading.not-loaded").replaceAll("%world%", arguments[1]));
            return true;
        }
        final Player target = Bukkit.getPlayer(arguments[1]);

        if (target == null) {
            commandSender.sendMessage(super.getPlayerNotFoundMessage());
            return true;
        }

        if (target.getWorld().getName().equalsIgnoreCase(arguments[2])) {
            if (commandSender.getName().equalsIgnoreCase(target.getName())) {
                commandSender.sendMessage(super.configuration.getWorldMessage("teleport.sender-already-in-world").replaceAll("%world%", arguments[1]));
                return true;
            }
            commandSender.sendMessage(super.configuration.getWorldMessage("teleport.target-already-in-world").replaceAll("%target%", target.getName()).replaceAll("%world%", arguments[1]));
            return true;
        }
        super.worldManager.teleportWorld(commandSender, target, world.getSpawnLocation());
        return true;
    }

    @Override
    public final List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        if (arguments.length != 3) {
            return null;
        }
        final List<String> completions = Lists.newArrayList(super.worldManager.getWorldProperties().keySet());

        if (commandSender instanceof Player) {
            completions.remove(((Player) commandSender).getWorld().getName());
        }
        return completions;
    }

}
