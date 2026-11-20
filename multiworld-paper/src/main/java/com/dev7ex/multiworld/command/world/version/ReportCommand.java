package com.dev7ex.multiworld.command.world.version;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.EmptyTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 19.06.2025
 */
@PaperCommandProperties(name = "report", permission = "multiworld.command.world.version.report")
public class ReportCommand extends PaperCommand implements EmptyTabCompleter {

    public ReportCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {

    }

}
