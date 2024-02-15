package com.dev7ex.multiworld.command.world.whitelist;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 15.02.2024
 */
@CommandProperties(name = "list", permission = "multiworld.command.whitelist.list")
public class ListCommand extends BukkitCommand implements TabCompleter {

    public ListCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        final BukkitWorldHolder worldHolder = MultiWorldPlugin.getInstance()
                .getWorldProvider()
                .getWorldHolder(arguments[1])
                .orElseThrow();

        if (worldHolder.getWhitelist().isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.list.empty")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return true;
        }
        final StringBuilder stringBuilder = new StringBuilder();

        for (final String name : worldHolder.getWhitelist()) {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append(ChatColor.GRAY);
                stringBuilder.append(", ");
            }
            stringBuilder.append(Bukkit.getPlayer(name) != null ? ChatColor.GREEN : ChatColor.RED).append(name);
        }
        commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.list.message")
                .replaceAll("%prefix%", super.getPrefix())
                .replaceAll("%world_name%", arguments[1])
                .replaceAll("%player_names%", stringBuilder.toString()));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final Command command,
                                      @NotNull final String commandLabel, @NotNull final String[] arguments) {
        return Collections.emptyList();
    }

}
