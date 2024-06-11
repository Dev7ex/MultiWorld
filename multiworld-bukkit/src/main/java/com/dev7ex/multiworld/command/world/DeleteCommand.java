package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.common.io.file.Files;
import com.dev7ex.multiworld.MultiWorldPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@BukkitCommandProperties(name = "delete", permission = "multiworld.command.world.delete")
public class DeleteCommand extends BukkitCommand implements BukkitTabCompleter {

    public DeleteCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 2) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.delete.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }

        if (arguments[1].equalsIgnoreCase("%creator_name%")) {
            arguments[1] = arguments[1].replaceAll("%creator_name%", commandSender.getName());
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        final File worldFolder = new File(Bukkit.getWorldContainer(), arguments[1]);

        if (!worldFolder.exists()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }

        if (!Files.containsFile(worldFolder, "session.lock")) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }

        if (arguments[1].equalsIgnoreCase(MultiWorldPlugin.getInstance().getConfiguration().getString("settings.defaults.normal-world"))) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.delete.world-cannot-deleted")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        MultiWorldPlugin.getInstance().getWorldManager().deleteWorld(commandSender.getName(), arguments[1]);
        return;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        final List<String> worlds = new ArrayList<>(MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders().keySet());
        worlds.remove(super.getConfiguration().getString("settings.defaults.world"));
        return worlds;
    }

}
