package com.dev7ex.multiworld.command.world;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

/**
 *
 * @author Dev7ex
 * @since 20.05.2021
 *
 */

public final class DeleteCommand extends WorldSubCommand implements TabCompleter {

    public DeleteCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage("§cSyntax: /world delete <Name>");
    }

    @Override
    public final boolean execute(final CommandSender commandSender, final String[] arguments) {
        final Player player = (Player) commandSender;

        if(arguments.length != 2) {
            commandSender.sendMessage(super.getPrefix() + super.getUsage());
            return true;
        }

        if(arguments[1].equalsIgnoreCase(MultiWorldPlugin.getInstance().getConfiguration().getDefaultWorldName())) {
            commandSender.sendMessage(super.getPrefix() + "§cThis world cannot be deleted");
            return true;
        }

        if(super.worldManager.isServerDeletingWorld()) {
            commandSender.sendMessage(super.getPrefix() + "§cPlease wait a moment...");
            return true;
        }

        if(!super.worldManager.getWorldConfiguration().isWorldRegistered(arguments[1])) {
            commandSender.sendMessage(super.getPrefix() + "§7This world doesn't exist!");
            return true;
        }
        super.worldManager.deleteWorld(super.getWorldUser(player.getUniqueId()), arguments[1]);
        return true;
    }

    @Override
    public final List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, String[] arguments) {
        final List<String> loadedWorlds = Lists.newArrayList(super.worldManager.getWorldProperties().keySet());
        loadedWorlds.remove(super.plugin.getConfiguration().getMessageSafe("default-world"));
        return loadedWorlds;
    }

}
