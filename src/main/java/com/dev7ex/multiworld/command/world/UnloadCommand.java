package com.dev7ex.multiworld.command.world;

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
public final class UnloadCommand extends WorldSubCommand implements TabCompleter {

    public UnloadCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage(plugin.getConfiguration().getUsage().replaceAll("%command%", "/world unload <World>"));
        super.setPermission("multiworld.command.world.unload");
    }

    @Override
    public final boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!commandSender.hasPermission(this.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 2) {
            commandSender.sendMessage(super.getUsage());
            return true;
        }

        if (arguments[1].equalsIgnoreCase(MultiWorldPlugin.getInstance().getConfiguration().getDefaultWorldName())) {
            commandSender.sendMessage(super.configuration.getWorldMessage("general.cannot-unloaded").replaceAll("%world%", arguments[1]));
            return true;
        }

        if (!super.worldManager.getWorldConfiguration().isWorldRegistered(arguments[1])) {
            commandSender.sendMessage(super.configuration.getWorldMessage("general.not-exists").replaceAll("%world%", arguments[1]));
            return true;
        }

        if (Bukkit.getWorld(arguments[1]) == null) {
            commandSender.sendMessage(super.configuration.getWorldMessage("loading.not-loaded").replaceAll("%world%", arguments[1]));
            return true;
        }
        super.worldManager.unloadWorld(commandSender, arguments[1]);
        return true;
    }

    @Override
    public final List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        final List<String> loadedWorlds = Lists.newArrayList(super.worldManager.getWorldProperties().keySet());
        loadedWorlds.remove(super.plugin.getConfiguration().getMessageSafe("defaults.world"));
        return loadedWorlds;
    }

}
