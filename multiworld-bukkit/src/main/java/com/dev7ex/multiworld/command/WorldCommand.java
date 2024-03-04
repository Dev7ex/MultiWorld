package com.dev7ex.multiworld.command;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.world.*;
import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Dev7ex
 * @since 19.05.2021
 */
@BukkitCommandProperties(name = "world", permission = "multiworld.command.world")
public final class WorldCommand extends BukkitCommand implements BukkitTabCompleter {

    public WorldCommand(final MultiWorldPlugin plugin) {
        super(plugin);

        super.registerSubCommand(new BackCommand(plugin));
        super.registerSubCommand(new BackupCommand(plugin));
        super.registerSubCommand(new CloneCommand(plugin));
        super.registerSubCommand(new CreateCommand(plugin));
        super.registerSubCommand(new DeleteCommand(plugin));
        super.registerSubCommand(new FlagCommand(plugin));
        super.registerSubCommand(new HelpCommand(plugin));
        super.registerSubCommand(new ImportCommand(plugin));
        super.registerSubCommand(new InfoCommand(plugin));
        super.registerSubCommand(new ListCommand(plugin));
        super.registerSubCommand(new LoadCommand(plugin));
        super.registerSubCommand(new ReloadCommand(plugin));
        super.registerSubCommand(new TeleportCommand(plugin));
        super.registerSubCommand(new UnloadCommand(plugin));
        super.registerSubCommand(new WhitelistCommand(plugin));
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if ((arguments.length == 0) || (arguments.length > 4)) {
            Objects.requireNonNull(super.getSubCommand(HelpCommand.class)).execute(commandSender, arguments);
            return;
        }
        if (super.getSubCommand(arguments[0].toLowerCase()).isEmpty()) {
            Objects.requireNonNull(super.getSubCommand(HelpCommand.class)).execute(commandSender, arguments);
            return;
        }
        super.getSubCommand(arguments[0].toLowerCase()).get().execute(commandSender, arguments);
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length == 1) {
            return Lists.newArrayList(super.getSubCommands().keySet());
        }
        if (super.getSubCommand(arguments[0].toLowerCase()).isEmpty()) {
            return Collections.emptyList();
        }
        final BukkitCommand subCommand = super.getSubCommand(arguments[0].toLowerCase()).get();

        if (!(subCommand instanceof BukkitTabCompleter)) {
            return Collections.emptyList();
        }
        return ((BukkitTabCompleter) subCommand).onTabComplete(commandSender, arguments);
    }

}
