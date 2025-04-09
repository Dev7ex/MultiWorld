package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.world.whitelist.ListCommand;
import com.dev7ex.multiworld.command.world.whitelist.*;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Dev7ex
 * @since 29.06.2023
 */
@BukkitCommandProperties(name = "whitelist", permission = "multiworld.command.world.whitelist")
public class WhitelistCommand extends BukkitCommand implements BukkitTabCompleter {

    private final DefaultTranslationProvider translationProvider;
    private final DefaultWorldProvider worldProvider;

    public WhitelistCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.translationProvider = plugin.getTranslationProvider();
        this.worldProvider = plugin.getWorldProvider();

        super.registerSubCommand(new AddCommand(plugin));
        super.registerSubCommand(new DisableCommand(plugin));
        super.registerSubCommand(new EnableCommand(plugin));
        super.registerSubCommand(new HelpCommand(plugin));
        super.registerSubCommand(new ListCommand(plugin));
        super.registerSubCommand(new RemoveCommand(plugin));
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if ((arguments.length < 3) || (arguments.length > 4)) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.whitelist.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }

        if (this.worldProvider.getWorldHolder(arguments[1]).isEmpty()) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "general.world.not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }

        if (super.getSubCommand(arguments[2].toLowerCase()).isEmpty()) {
            Objects.requireNonNull(super.getSubCommand(HelpCommand.class)).execute(commandSender, arguments);
            return;
        }
        super.getSubCommand(arguments[2].toLowerCase())
                .get()
                .execute(commandSender, arguments);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if ((arguments.length < 2) || (arguments.length > 4)) {
            return Collections.emptyList();
        }

        if (arguments.length == 2) {
            return new ArrayList<>(this.worldProvider.getWorldHolders().keySet());
        }

        if (arguments.length == 3) {
            return Lists.newArrayList(super.getSubCommands().keySet());
        }

        if (super.getSubCommand(arguments[2].toLowerCase()).isEmpty()) {
            return Collections.emptyList();
        }
        final BukkitCommand subCommand = super.getSubCommand(arguments[2].toLowerCase())
                .get();

        if (!(subCommand instanceof BukkitTabCompleter)) {
            return Collections.emptyList();
        }
        return ((BukkitTabCompleter) subCommand).onTabComplete(commandSender, arguments);
    }

}
