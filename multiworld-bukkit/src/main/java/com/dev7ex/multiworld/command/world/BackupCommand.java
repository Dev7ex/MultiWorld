package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 19.06.2023
 */
@CommandProperties(name = "backup", permission = "multiworld.command.world.backup")
public class BackupCommand extends BukkitCommand implements TabCompleter {

    public BackupCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 2) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.backup.usage")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }

        if (!MultiWorldPlugin.getInstance().getWorldProvider().isRegistered(arguments[1])) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-not-exists")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }
        MultiWorldPlugin.getInstance().getWorldManager().createBackup(commandSender.getName(), arguments[1]);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final Command command,
                                      @NotNull final String commandLabel, @NotNull final String[] arguments) {
        if (arguments.length != 2) {
            return Collections.emptyList();
        }
        return new ArrayList<>(MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders().keySet());
    }

}
