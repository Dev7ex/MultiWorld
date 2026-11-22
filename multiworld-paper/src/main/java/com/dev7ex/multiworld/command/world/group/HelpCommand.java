
package com.dev7ex.multiworld.command.world.group;

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
 * @since 09.07.2025
 */
@PaperCommandProperties(name = "help", permission = "multiworld.command.world.group")
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
        if (arguments.length > 3) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_GROUP_HELP_USAGE));
            return;
        }
        int page = 1;

        if (arguments.length == 3) {
            final String pageArgument = arguments[2].asString();

            try {
                page = Integer.parseInt(pageArgument);

            } catch (final NumberFormatException ignored) {
                commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_PAGINATION_NOT_FOUND)
                        .replaceText(builder -> builder.matchLiteral("%page%").replacement(pageArgument)));
                return;
            }
        }
        final int maxAvailablePages = this.localizationRegistry.getMaxAvailablePages(commandSender, WorldLocalizationEntry.COMMAND_WORLD_GROUP_HELP);

        if ((page < 1) || (page > maxAvailablePages)) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_PAGINATION_OUT_OF_RANGE)
                    .replaceText(builder -> builder.matchLiteral("%maximum_pages%").replacement(String.valueOf(maxAvailablePages))));
            return;
        }
        commandSender.sendMessage(this.localizationRegistry.getHeader(commandSender, this.configuration.getPrefix()));
        this.localizationRegistry.getComponentList(commandSender, WorldLocalizationEntry.COMMAND_WORLD_GROUP_HELP.getKeyForPage(page)).forEach(commandSender::sendMessage);
        commandSender.sendMessage(Component.empty());
        commandSender.sendMessage(this.localizationRegistry.getNavigablePaginationComponent(commandSender, page, "/world group help ", WorldLocalizationEntry.COMMAND_WORLD_GROUP_HELP));
        commandSender.sendMessage(this.localizationRegistry.getFooter(commandSender, this.configuration.getPrefix()));
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length != 3) {
            return Collections.emptyList();
        }
        final String input = arguments[2].asString();
        final int maxAvailablePages = this.localizationRegistry.getMaxAvailablePages(commandSender, WorldLocalizationEntry.COMMAND_WORLD_GROUP_HELP);

        return IntStream.rangeClosed(1, maxAvailablePages)
                .mapToObj(String::valueOf)
                .filter(page -> page.startsWith(input))
                .toList();
    }

}
