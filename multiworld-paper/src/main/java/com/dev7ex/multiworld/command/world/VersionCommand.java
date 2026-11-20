package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.world.version.CheckCommand;
import com.dev7ex.multiworld.command.world.version.HelpCommand;
import com.dev7ex.multiworld.command.world.version.InfoCommand;
import com.dev7ex.multiworld.command.world.version.ReportCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Dev7ex
 * @since 04.01.2025
 */
@PaperCommandProperties(name = "version", permission = "multiworld.command.world.version")
public class VersionCommand extends PaperCommand implements ArgumentTabCompleter {

    public VersionCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.registerSubCommand(new CheckCommand(plugin));
        this.registerSubCommand(new HelpCommand(plugin));
        this.registerSubCommand(new InfoCommand(plugin));
        this.registerSubCommand(new ReportCommand(plugin));
    }

    @Override
    public void execute(@NotNull CommandSender commandSender, @NotNull PaperCommandArgument[] paperCommandArguments) {

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull PaperCommandArgument[] paperCommandArguments) {
        return List.of();
    }

}
