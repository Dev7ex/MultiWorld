package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 19.06.2023
 */
@CommandProperties(name = "backup", permission = "multiworld.command.world.backup")
public class BackupCommand extends WorldSubCommand implements TabCompleter {

    public BackupCommand(final MultiWorldPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (arguments.length != 2) {
            commandSender.sendMessage(super.getConfiguration().getCommandUsage(this));
            return true;
        }

        if (!super.getWorldManager().getWorldProperties().containsKey(arguments[1])) {
            commandSender.sendMessage(super.getConfiguration().getMessage("general.not-exists").replaceAll("%world%", arguments[1]));
            return true;
        }
        super.getWorldManager().createBackup(commandSender, arguments[1]);
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        if (arguments.length != 2) {
            return Collections.emptyList();
        }
        return new ArrayList<>(super.getWorldManager().getWorldProperties().keySet());
    }

}
