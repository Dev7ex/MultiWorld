package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.world.WorldProperty;
import com.dev7ex.multiworld.command.world.whitelist.*;
import com.dev7ex.multiworld.command.world.whitelist.ListCommand;
import com.dev7ex.multiworld.world.DefaultWorldConfiguration;
import com.google.common.collect.Lists;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 29.06.2023
 */
@CommandProperties(name = "whitelist", permission = "multiworld.command.world.whitelist")
public class WhitelistCommand extends BukkitCommand implements TabCompleter {

    public WhitelistCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);

        super.registerSubCommand(new AddCommand(plugin));
        super.registerSubCommand(new DisableCommand(plugin));
        super.registerSubCommand(new EnableCommand(plugin));
        super.registerSubCommand(new HelpCommand(plugin));
        super.registerSubCommand(new ListCommand(plugin));
        super.registerSubCommand(new RemoveCommand(plugin));
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if ((arguments.length < 3) || (arguments.length > 4)) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.usage")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-not-exists")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return true;
        }
        final Optional<BukkitCommand> commandOptional = super.getSubCommand(arguments[2].toLowerCase());

        if (commandOptional.isEmpty()) {
            return super.getSubCommand("help").orElseThrow().execute(commandSender, arguments);
        }
        return commandOptional.get().execute(commandSender, arguments);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final Command command,
                                      @NotNull final String commandLabel, @NotNull final String[] arguments) {
        if ((arguments.length < 2) || (arguments.length > 4)) {
            return Collections.emptyList();
        }

        if (arguments.length == 2) {
            return new ArrayList<>(MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders().keySet());
        }

        if (arguments.length == 3) {
            return Lists.newArrayList(super.getSubCommands().keySet());
        }

        final Optional<BukkitCommand> commandOptional = super.getSubCommand(arguments[2].toLowerCase());

        if ((commandOptional.isEmpty()) || (!(commandOptional.get() instanceof TabCompleter))) {
            return null;
        }
        return ((TabCompleter) commandOptional.get()).onTabComplete(commandSender, command, commandLabel, arguments);
    }

}
