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
 * @since 26.06.2023
 */
@CommandProperties(name = "clone", permission = "multiworld.command.world.clone")
public class CloneCommand extends BukkitCommand implements TabCompleter {

    public CloneCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 3) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.clone.usage")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().isRegistered(arguments[2])) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-already-exists")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }
        MultiWorldPlugin.getInstance().getWorldManager().cloneWorld(commandSender.getName(), arguments[1], arguments[2]);
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
