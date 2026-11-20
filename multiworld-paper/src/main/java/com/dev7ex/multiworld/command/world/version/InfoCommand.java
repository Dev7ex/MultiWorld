package com.dev7ex.multiworld.command.world.version;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.EmptyTabCompleter;
import com.dev7ex.multiworld.MultiWorldConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 22.06.2025
 */
@PaperCommandProperties(name = "info", permission = "multiworld.command.world.version.info")
public class InfoCommand extends PaperCommand implements EmptyTabCompleter {

    private final MultiWorldConfiguration configuration;
    private final WorldLocalizationRegistry localizationRegistry;

    public InfoCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.configuration = plugin.getConfiguration();
        this.localizationRegistry = plugin.getLocalizationRegistry();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {

    }

}
