package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.command.world.whitelist.*;
import com.dev7ex.multiworld.command.world.whitelist.HelpCommand;
import com.dev7ex.multiworld.command.world.whitelist.ListCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author Dev7ex
 * @since 29.06.2023
 */
@PaperCommandProperties(name = "whitelist", permission = "multiworld.command.world.whitelist")
public class WhitelistCommand extends PaperCommand implements ArgumentTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;
    private final HelpCommand helpCommand;

    public WhitelistCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.helpCommand = new HelpCommand(plugin);

        this.registerSubCommand(new AddCommand(plugin));
        this.registerSubCommand(new DisableCommand(plugin));
        this.registerSubCommand(new EnableCommand(plugin));
        this.registerSubCommand(this.helpCommand);
        this.registerSubCommand(new ListCommand(plugin));
        this.registerSubCommand(new RemoveCommand(plugin));
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if ((arguments.length < 2) || (arguments.length > 5)) {
            this.helpCommand.execute(commandSender, arguments);
            return;
        }
        final String subCommandName = arguments[1].asString().toLowerCase(Locale.ROOT);

        super.findSubCommand(subCommandName).ifPresentOrElse(subCommand -> {
            if ((subCommand.hasPermission()) && (!commandSender.hasPermission(subCommand.getPermission()))) {
                commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_NO_PERMISSION));
                return;
            }
            subCommand.execute(commandSender, arguments);

        }, () -> this.helpCommand.execute(commandSender, arguments));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if ((arguments.length < 2) || (arguments.length > 4)) {
            return Collections.emptyList();
        }

        if (arguments.length == 2) {
            final String input = arguments[1].asString().toLowerCase(Locale.ROOT);

            return super.getSubCommands().values().stream()
                    .filter(subCommand -> !subCommand.hasPermission() || commandSender.hasPermission(subCommand.getPermission()))
                    .map(subCommand -> subCommand.getName().toLowerCase(Locale.ROOT))
                    .filter(name -> name.startsWith(input))
                    .toList();
        }
        final String subCommandName = arguments[1].asString().toLowerCase(Locale.ROOT);

        return super.findSubCommand(subCommandName)
                .filter(subCommand -> !subCommand.hasPermission() || commandSender.hasPermission(subCommand.getPermission()))
                .filter(ArgumentTabCompleter.class::isInstance)
                .map(subCommand -> ((ArgumentTabCompleter) subCommand).onTabComplete(commandSender, arguments))
                .orElse(Collections.emptyList());
    }

}
