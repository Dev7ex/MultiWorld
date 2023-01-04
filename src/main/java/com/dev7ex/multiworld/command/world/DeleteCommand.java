package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.io.Files;
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
@CommandProperties(name = "delete", permission = "multiworld.command.world.delete")
public final class DeleteCommand extends WorldSubCommand implements TabCompleter {

    public DeleteCommand(final MultiWorldPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (arguments.length != 2) {
            commandSender.sendMessage(super.getConfiguration().getCommandUsage(this));
            return true;
        }
        final File worldFolder = new File(Bukkit.getWorldContainer(), arguments[1]);

        if(!worldFolder.exists()) {
            commandSender.sendMessage(super.getConfiguration().getMessage("general.folder-not-exists").replaceAll("%folder%", arguments[1]));
            return true;
        }

        if(!Files.containsFile(worldFolder, "session.lock")) {
            commandSender.sendMessage(super.getConfiguration().getMessage("general.folder-not-exists").replaceAll("%folder%", arguments[1]));
            return true;
        }

        if (arguments[1].equalsIgnoreCase(MultiWorldPlugin.getInstance().getConfiguration().getString("defaults.world"))) {
            commandSender.sendMessage(super.getConfiguration().getMessage("general.cannot-deleted").replaceAll("%world%", arguments[1]));
            return true;
        }

        if (!super.getWorldManager().getWorldConfiguration().isWorldRegistered(arguments[1])) {
            commandSender.sendMessage(super.getConfiguration().getMessage("general.not-exists").replaceAll("%world%", arguments[1]));
            return true;
        }
        super.getWorldManager().deleteWorld(commandSender, arguments[1]);
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        final List<String> worlds = Lists.newArrayList(super.getWorldManager().getWorldProperties().keySet());
        worlds.remove(super.getPlugin().getConfiguration().getString("defaults.world"));
        return worlds;
    }

}
