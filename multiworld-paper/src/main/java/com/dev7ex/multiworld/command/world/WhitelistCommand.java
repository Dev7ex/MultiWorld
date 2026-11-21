package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.world.whitelist.*;
import com.dev7ex.multiworld.command.world.whitelist.HelpCommand;
import com.dev7ex.multiworld.command.world.whitelist.ListCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Dev7ex
 * @since 29.06.2023
 */
@PaperCommandProperties(name = "whitelist", permission = "multiworld.command.world.whitelist")
public class WhitelistCommand extends PaperCommand implements ArgumentTabCompleter {

    public WhitelistCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.registerSubCommand(new AddCommand(plugin));
        this.registerSubCommand(new DisableCommand(plugin));
        this.registerSubCommand(new EnableCommand(plugin));
        this.registerSubCommand(new HelpCommand(plugin));
        this.registerSubCommand(new ListCommand(plugin));
        this.registerSubCommand(new RemoveCommand(plugin));
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        return List.of();
    }

}
