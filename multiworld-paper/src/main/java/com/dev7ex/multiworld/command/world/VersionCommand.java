package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.command.world.version.CheckCommand;
import com.dev7ex.multiworld.command.world.version.HelpCommand;
import com.dev7ex.multiworld.command.world.version.InfoCommand;
import com.dev7ex.multiworld.command.world.version.ReportCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author Dev7ex
 * @since 22.07.2024
 */
@PaperCommandProperties(name = "version", permission = "multiworld.command.world.version")
public class VersionCommand extends PaperCommand implements ArgumentTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;
    private final HelpCommand helpCommand;

    public VersionCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.helpCommand = new HelpCommand(plugin);

        this.registerSubCommand(new CheckCommand(plugin));
        this.registerSubCommand(this.helpCommand);
        this.registerSubCommand(new InfoCommand(plugin));
        this.registerSubCommand(new ReportCommand(plugin));
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length < 2) {
            this.helpCommand.execute(commandSender, arguments);
            return;
        }
        final String subCommandName = arguments[1].asString().toLowerCase(Locale.ROOT);

        super.findSubCommand(subCommandName).ifPresentOrElse(subCommand -> {
            if (subCommand.hasPermission() && !commandSender.hasPermission(subCommand.getPermission())) {
                commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_NO_PERMISSION));
                return;
            }
            subCommand.execute(commandSender, arguments);

        }, () -> this.helpCommand.execute(commandSender, arguments));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length < 2) {
            return Collections.emptyList();
        }
        final String subCommandName = arguments[1].asString().toLowerCase(Locale.ROOT);

        if (arguments.length == 2) {
            return super.getSubCommands().values().stream()
                    .filter(subCommand -> !subCommand.hasPermission() || commandSender.hasPermission(subCommand.getPermission()))
                    .map(subCommand -> subCommand.getName().toLowerCase(Locale.ROOT))
                    .filter(name -> name.startsWith(subCommandName))
                    .toList();
        }
        return super.findSubCommand(subCommandName)
                .filter(subCommand -> !subCommand.hasPermission() || commandSender.hasPermission(subCommand.getPermission()))
                .filter(ArgumentTabCompleter.class::isInstance)
                .map(subCommand -> ((ArgumentTabCompleter) subCommand).onTabComplete(commandSender, arguments))
                .orElseGet(Collections::emptyList);
    }

}