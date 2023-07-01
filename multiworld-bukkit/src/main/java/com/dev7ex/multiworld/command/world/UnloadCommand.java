package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@CommandProperties(name = "unload", permission = "multiworld.command.world.unload")
public class UnloadCommand extends BukkitCommand implements TabCompleter {

    public UnloadCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 2) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.load.usage")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }


        if (MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-not-exists")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return true;
        }

        if (arguments[1].equalsIgnoreCase(MultiWorldPlugin.getInstance().getConfiguration().getString("settings.defaults.normal-world"))) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.unload.world-cannot-unloaded")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return true;
        }

        if (Bukkit.getWorld(arguments[1]) == null) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-not-loaded")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
        }
        MultiWorldPlugin.getInstance().getWorldManager().unloadWorld(commandSender.getName(), arguments[1]);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final Command command,
                                      @NotNull final String commandLabel, @NotNull final String[] arguments) {
        return MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders().values().stream().filter(BukkitWorldHolder::isLoaded).map(BukkitWorldHolder::getName).toList();
    }

}
