package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;

import com.google.common.collect.Lists;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@CommandProperties(name = "unload", permission = "multiworld.command.world.unload")
public final class UnloadCommand extends WorldSubCommand implements TabCompleter {

    public UnloadCommand(final MultiWorldPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!commandSender.hasPermission(this.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 2) {
            commandSender.sendMessage(super.getConfiguration().getCommandUsage(this));
            return true;
        }

        if (arguments[1].equalsIgnoreCase(MultiWorldPlugin.getInstance().getConfiguration().getString("defaults.world"))) {
            commandSender.sendMessage(super.getConfiguration().getMessage("general.cannot-unloaded").replaceAll("%world%", arguments[1]));
            return true;
        }

        if (!super.getWorldManager().getWorldConfiguration().isWorldRegistered(arguments[1])) {
            commandSender.sendMessage(super.getConfiguration().getMessage("general.not-exists").replaceAll("%world%", arguments[1]));
            return true;
        }

        if (Bukkit.getWorld(arguments[1]) == null) {
            commandSender.sendMessage(super.getConfiguration().getMessage("load.not-loaded").replaceAll("%world%", arguments[1]));
            return true;
        }
        super.getWorldManager().unloadWorld(commandSender, arguments[1]);
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        final List<String> loadedWorlds = Lists.newArrayList(super.getWorldManager().getWorldProperties().keySet());
        loadedWorlds.remove(super.getPlugin().getConfiguration().getString("defaults.world"));
        return loadedWorlds;
    }

}
