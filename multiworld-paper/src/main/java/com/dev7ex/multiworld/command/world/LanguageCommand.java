package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Dev7ex
 * @since 06.06.2025
 */
@PaperCommandProperties(name = "language", permission = "multiworld.command.world.language")
public class LanguageCommand extends PaperCommand implements ArgumentTabCompleter {

    public LanguageCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull CommandSender commandSender, @NotNull PaperCommandArgument[] arguments) {

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull PaperCommandArgument[] arguments) {
        return List.of();
    }

}
