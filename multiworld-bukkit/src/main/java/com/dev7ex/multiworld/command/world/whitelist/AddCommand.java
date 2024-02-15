package com.dev7ex.multiworld.command.world.whitelist;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.world.WorldProperty;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 15.02.2024
 */
@CommandProperties(name = "add", permission = "multiworld.command.whitelist.add")
public class AddCommand extends BukkitCommand implements TabCompleter {

    public AddCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        final BukkitWorldHolder worldHolder = MultiWorldPlugin.getInstance()
                .getWorldProvider()
                .getWorldHolder(arguments[1])
                .orElseThrow();

        if (worldHolder.getWhitelist().contains(arguments[3])) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.add.already-added")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%world_name%", arguments[1])
                    .replaceAll("%player_name%", arguments[3]));
            return true;
        }
        worldHolder.getWhitelist().add(arguments[3]);
        MultiWorldPlugin.getInstance().getWorldConfiguration().write(worldHolder, WorldProperty.WHITELIST, worldHolder.getWhitelist());
        commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.add.successfully-added")
                .replaceAll("%prefix%", super.getPrefix())
                .replaceAll("%world_name%", arguments[1])
                .replaceAll("%player_name%", arguments[3]));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final Command command,
                                      @NotNull final String commandLabel, @NotNull final String[] arguments) {
        if (arguments.length != 4) {
            return Collections.emptyList();
        }

        final BukkitWorldHolder worldHolder = MultiWorldPlugin.getInstance()
                .getWorldProvider()
                .getWorldHolder(arguments[1])
                .orElseThrow();

        final List<String> completions = Lists.newArrayList(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).toList());
        completions.removeAll(worldHolder.getWhitelist());

        return completions;
    }

}