
package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.command.world.language.HelpCommand;
import com.dev7ex.multiworld.command.world.language.InfoCommand;
import com.dev7ex.multiworld.command.world.language.ListCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

/**
 * @author Dev7ex
 * @since 06.06.2025
 */
@PaperCommandProperties(name = "language", permission = "multiworld.command.world.language")
public class LanguageCommand extends PaperCommand implements ArgumentTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;
    private final HelpCommand helpCommand;

    public LanguageCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.helpCommand = new HelpCommand(plugin);

        this.registerSubCommand(this.helpCommand);
        this.registerSubCommand(new InfoCommand(plugin));
        this.registerSubCommand(new ListCommand(plugin));
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length < 2) {
            this.helpCommand.execute(commandSender, arguments);
            return;
        }

        final String subCommandName = arguments[1].asString().toLowerCase(Locale.ROOT);

        super.findSubCommand(subCommandName).ifPresentOrElse(subCommand -> {
            if (!this.canExecute(commandSender, subCommand)) {
                commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_NO_PERMISSION));
                return;
            }

            subCommand.execute(commandSender, arguments);

        }, () -> this.helpCommand.execute(commandSender, arguments));
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length < 2) {
            return List.of();
        }

        final String subCommandName = arguments[1].asString().toLowerCase(Locale.ROOT);

        if (arguments.length == 2) {
            return super.getSubCommands().values().stream()
                    .filter(subCommand -> this.canExecute(commandSender, subCommand))
                    .map(subCommand -> subCommand.getName().toLowerCase(Locale.ROOT))
                    .filter(name -> name.startsWith(subCommandName))
                    .sorted()
                    .toList();
        }

        return super.findSubCommand(subCommandName)
                .filter(subCommand -> this.canExecute(commandSender, subCommand))
                .filter(ArgumentTabCompleter.class::isInstance)
                .map(ArgumentTabCompleter.class::cast)
                .map(completer -> completer.onTabComplete(commandSender, arguments))
                .orElseGet(List::of);
    }

    private boolean canExecute(@NotNull final CommandSender commandSender, @NotNull final PaperCommand command) {
        return !command.hasPermission() || commandSender.hasPermission(command.getPermission());
    }

}
