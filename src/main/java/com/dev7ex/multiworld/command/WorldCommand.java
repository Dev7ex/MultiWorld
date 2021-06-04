package com.dev7ex.multiworld.command;

import com.dev7ex.common.bukkit.command.SimpleCommand;
import com.dev7ex.common.bukkit.command.SubCommand;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.world.*;

import com.google.common.collect.Lists;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

/**
 * @author Dev7ex
 * @since 19.05.2021
 */
public final class WorldCommand extends SimpleCommand implements TabCompleter {

    public WorldCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setPermission("multiworld.command.world");

        super.registerSubCommand(new BackCommand(plugin));
        super.registerSubCommand(new CreateCommand(plugin));
        super.registerSubCommand(new DeleteCommand(plugin));
        super.registerSubCommand(new InfoCommand(plugin));
        super.registerSubCommand(new HelpCommand(plugin));
        super.registerSubCommand(new ImportCommand(plugin));
        super.registerSubCommand(new ListCommand(plugin));
        super.registerSubCommand(new LoadCommand(plugin));
        super.registerSubCommand(new OptionsCommand(plugin));
        super.registerSubCommand(new TeleportCommand(plugin));
        super.registerSubCommand(new UnloadCommand(plugin));
    }

    @Override
    public final boolean onCommand(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        if (!commandSender.hasPermission(super.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if ((arguments.length == 0) || (arguments.length > 4)) {
            return super.commandMap.get("help").execute(commandSender, arguments);
        }
        final SubCommand subCommand = super.commandMap.get(arguments[0].toLowerCase());

        if (subCommand == null) {
            super.commandMap.get("help").execute(commandSender, arguments);
            return true;
        }
        return subCommand.execute(commandSender, arguments);
    }

    @Override
    public final List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        if (arguments.length == 1) {
            return Lists.newArrayList(super.commandMap.keySet());
        }
        final SubCommand subCommand = super.commandMap.get(arguments[0].toLowerCase());

        if ((subCommand == null) || (!(subCommand instanceof TabCompleter))) {
            return null;
        }
        return ((TabCompleter) subCommand).onTabComplete(commandSender, command, commandLabel, arguments);
    }

}
