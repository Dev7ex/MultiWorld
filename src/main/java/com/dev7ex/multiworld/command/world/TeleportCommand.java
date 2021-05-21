package com.dev7ex.multiworld.command.world;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Dev7ex
 * @since 20.05.2021
 *
 */

public final class TeleportCommand extends WorldSubCommand implements TabCompleter {

    public TeleportCommand(MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage("§cSyntax: /world teleport <World>");
        super.setAliases(Collections.singletonList("tp"));
    }

    @Override
    public final boolean execute(final CommandSender commandSender, final String[] arguments) {
        if(arguments.length != 2) {
            commandSender.sendMessage(super.getPrefix() + super.getUsage());
            return true;
        }
        if(!super.worldManager.getWorldProperties().containsKey(arguments[1])) {
            commandSender.sendMessage(super.getPrefix() + "§7This world doesn't exist!");
            return true;
        }
        final World world = Bukkit.getWorld(arguments[1]);

        if(world == null) {
            commandSender.sendMessage(super.getPrefix() + "§7This world is not loaded!");
            return true;
        }
        final Player player = (Player) commandSender;

        if(player.getWorld().getName().equalsIgnoreCase(arguments[1])) {
            player.sendMessage(super.getPrefix() + "§7You already in this world!");
            return true;
        }

        super.worldManager.teleportWorld(super.getWorldUser(player.getUniqueId()), world, world.getSpawnLocation());
        return true;
    }

    @Override
    public final List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, String[] arguments) {
        final Player player = (Player) commandSender;
        final List<String> completions = Lists.newArrayList(super.worldManager.getWorldProperties().keySet());
        completions.remove(player.getWorld().getName());
        return completions;
    }

}
