package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multicommon.api.plugin.AdvancedPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Dev7ex
 * @since 24.05.2021
 */
@PaperCommandProperties(name = "info", permission = "multiworld.command.world.info")
public class InfoCommand extends PaperCommand implements ArgumentTabCompleter {

    public InfoCommand(@NotNull final AdvancedPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        return List.of();
    }

}
