package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.command.world.backup.*;
import com.dev7ex.multiworld.command.world.backup.CreateCommand;
import com.dev7ex.multiworld.command.world.backup.DeleteCommand;
import com.dev7ex.multiworld.command.world.backup.HelpCommand;
import com.dev7ex.multiworld.command.world.backup.ListCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Dev7ex
 * @since 04.01.2025
 */
@PaperCommandProperties(name = "backup", permission = "multiworld.command.world.backup")
public class BackupCommand extends PaperCommand implements ArgumentTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;

    public BackupCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.localizationRegistry = plugin.getLocalizationRegistry();

        this.registerSubCommand(new ClearCommand(plugin));
        this.registerSubCommand(new CreateCommand(plugin));
        this.registerSubCommand(new DeleteCommand(plugin));
        this.registerSubCommand(new HelpCommand(plugin));
        this.registerSubCommand(new ListCommand(plugin));
        this.registerSubCommand(new RestoreCommand(plugin));
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if ((arguments.length == 1) || (arguments.length > 5)) {
            Objects.requireNonNull(super.findSubCommand(HelpCommand.class)).execute(commandSender, arguments);
            return;
        }
        final String subCommandName = arguments[1].asString().toLowerCase();

        super.findSubCommand(subCommandName).ifPresentOrElse(subCommand -> {
            if ((subCommand.hasPermission()) && (!commandSender.hasPermission(subCommand.getPermission()))) {
                commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_NO_PERMISSION));
                return;
            }
            subCommand.execute(commandSender, arguments);

        }, () -> Objects.requireNonNull(super.findSubCommand(HelpCommand.class)).execute(commandSender, arguments));
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if ((arguments.length < 2) || (arguments.length > 4)) {
            return Collections.emptyList();
        }

        if (arguments.length == 2) {
            return super.getSubCommands().values().stream()
                    .filter(subCommand -> !subCommand.hasPermission() || commandSender.hasPermission(subCommand.getPermission()))
                    .map(subCommand -> subCommand.getName().toLowerCase())
                    .toList();
        }
        final String subCommandName = arguments[1].asString().toLowerCase();

        return super.findSubCommand(subCommandName)
                .filter(subCommand -> !subCommand.hasPermission() || commandSender.hasPermission(subCommand.getPermission()))
                .filter(ArgumentTabCompleter.class::isInstance)
                .map(subCommand -> ((ArgumentTabCompleter) subCommand).onTabComplete(commandSender, arguments))
                .orElse(Collections.emptyList());
    }

}
