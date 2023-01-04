package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;
import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CommandProperties(name = "clone", permission = "multiworld.command.world.clone")
public class CloneCommand extends WorldSubCommand implements TabCompleter {

    public CloneCommand(final MultiWorldPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (arguments.length != 3) {
            commandSender.sendMessage(super.getConfiguration().getCommandUsage(this));
            return true;
        }

        if (super.getWorldManager().getWorldProperties().containsKey(arguments[2])) {
            commandSender.sendMessage(super.getConfiguration().getMessage("general.already-exists").replaceAll("%world%", arguments[1]));
            return true;
        }
        super.getWorldManager().cloneWorld(commandSender, arguments[1], arguments[2]);
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        if (arguments.length == 3) {
            return Collections.emptyList();
        }
        return new ArrayList<>(super.getWorldManager().getWorldProperties().keySet());
    }

}
