package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.java.util.FileUtil;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;

import com.google.common.collect.Lists;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.io.File;
import java.util.List;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */

public final class DeleteCommand extends WorldSubCommand implements TabCompleter {

    public DeleteCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage(plugin.getConfiguration().getUsage().replaceAll("%command%", "/world delete <Name>"));
        super.setPermission("multiworld.command.world.delete");
    }

    @Override
    public final boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!commandSender.hasPermission(this.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 2) {
            commandSender.sendMessage(super.getPrefix() + super.getUsage());
            return true;
        }

        final File worldFolder = new File(Bukkit.getWorldContainer(), arguments[1]);

        if(!worldFolder.exists()) {
            commandSender.sendMessage(super.configuration.getWorldMessage("general.folder-not-exists").replaceAll("%folder%", arguments[1]));
            return true;
        }

        if(!FileUtil.containsFile(worldFolder, "level.dat")) {
            commandSender.sendMessage(super.configuration.getWorldMessage("general.folder-not-exists").replaceAll("%folder%", arguments[1]));
            return true;
        }

        if (arguments[1].equalsIgnoreCase(MultiWorldPlugin.getInstance().getConfiguration().getDefaultWorldName())) {
            commandSender.sendMessage(super.configuration.getWorldMessage("general.cannot-deleted").replaceAll("%world%", arguments[1]));
            return true;
        }

        if (super.worldManager.isServerDeletingWorld()) {
            commandSender.sendMessage(super.configuration.getWorldMessage("general.waiting"));
            return true;
        }

        if (!super.worldManager.getWorldConfiguration().isWorldRegistered(arguments[1])) {
            commandSender.sendMessage(super.configuration.getWorldMessage("general.not-exists").replaceAll("%world%", arguments[1]));
            return true;
        }
        super.worldManager.deleteWorld(commandSender, arguments[1]);
        return true;
    }

    @Override
    public final List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        final List<String> worlds = Lists.newArrayList(super.worldManager.getWorldProperties().keySet());
        worlds.remove(super.plugin.getConfiguration().getMessageSafe("defaults.world"));
        return worlds;
    }

}
