package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multiworld.MultiWorldConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@PaperCommandProperties(name = "help", permission = "multiworld.command.world")
public class HelpCommand extends PaperCommand implements ArgumentTabCompleter {

    private final MultiWorldConfiguration configuration;
    private final WorldLocalizationRegistry localizationRegistry;

    public HelpCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.configuration = plugin.getConfiguration();
        this.localizationRegistry = plugin.getLocalizationRegistry();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        final int page;
        final String command = "/mw help ";

        if (arguments.length > 2) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_HELP_USAGE));
            return;
        }

        if ((arguments.length == 0) || (arguments.length == 1)) {
            page = 1;

        } else {
            final String pageArgument = arguments[1].asString();

            try {
                page = Integer.parseInt(pageArgument);

            } catch (final NumberFormatException ignored) {
                commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_PAGINATION_NOT_FOUND)
                        .replaceText(builder -> builder.match("%page%").replacement(pageArgument)));
                return;
            }
        }
        final int maxAvailablePages = this.localizationRegistry.getMaxAvailablePages(commandSender, WorldLocalizationEntry.COMMAND_WORLD_HELP);

        if ((page < 1) || (page > maxAvailablePages)) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_PAGINATION_OUT_OF_RANGE)
                    .replaceText(builder -> builder.match("%maximum_pages%").replacement(String.valueOf(maxAvailablePages))));
            return;
        }
        commandSender.sendMessage(this.localizationRegistry.getHeader(commandSender, this.configuration.getPrefix()));
        this.localizationRegistry.getComponentList(commandSender, WorldLocalizationEntry.COMMAND_WORLD_HELP.getKeyForPage(page)).forEach(commandSender::sendMessage);
        commandSender.sendMessage(Component.empty());
        commandSender.sendMessage(this.localizationRegistry.getNavigablePaginationComponent(commandSender, page, command, WorldLocalizationEntry.COMMAND_WORLD_HELP));
        commandSender.sendMessage(this.localizationRegistry.getFooter(commandSender, this.configuration.getPrefix()));
    }


    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length != 2) {
            return Collections.emptyList();
        }
        final int maxPages = this.localizationRegistry.getMaxAvailablePages(commandSender, WorldLocalizationEntry.COMMAND_WORLD_HELP);

        return IntStream.rangeClosed(1, maxPages)
                .mapToObj(String::valueOf)
                .toList();
    }

}
