package com.dev7ex.multiworld.command;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.command.world.*;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Dev7ex
 * @since 14.04.2025
 */
@PaperCommandProperties(name = "world", permission = "multiworld.command.world")
public class WorldCommand extends PaperCommand implements ArgumentTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;

    public WorldCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.localizationRegistry = plugin.getLocalizationRegistry();

        this.registerSubCommand(new BackupCommand(plugin));
        this.registerSubCommand(new CreateCommand(plugin));
        super.registerSubCommand(new HelpCommand(plugin));
        this.registerSubCommand(new ListCommand(plugin));
        this.registerSubCommand(new TeleportCommand(plugin));
        this.registerSubCommand(new SetSpawnCommand(plugin));
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if ((arguments.length == 0) || (arguments.length > 5)) {
            Objects.requireNonNull(super.findSubCommand(HelpCommand.class)).execute(commandSender, arguments);
            return;
        }
        final String subCommandName = arguments[0].asString().toLowerCase();

        super.findSubCommand(subCommandName).ifPresentOrElse(subCommand -> {
            if (!commandSender.hasPermission(subCommand.getPermission())) {
                commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_NO_PERMISSION));
                return;
            }
            subCommand.execute(commandSender, arguments);

        }, () -> Objects.requireNonNull(super.findSubCommand(HelpCommand.class)).execute(commandSender, arguments));
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length == 1) {
            return new ArrayList<>(super.getSubCommands().keySet());
        }
        final String subCommandName = arguments[0].asString().toLowerCase();

        return super.findSubCommand(subCommandName)
                .filter(sub -> commandSender.hasPermission(sub.getPermission()))
                .filter(ArgumentTabCompleter.class::isInstance)
                .map(sub -> ((ArgumentTabCompleter) sub).onTabComplete(commandSender, arguments))
                .orElse(Collections.emptyList());
    }

}
