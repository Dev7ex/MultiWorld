package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.EmptyTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.WorldRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@PaperCommandProperties(name = "list", permission = "multiworld.command.world.list")
public class ListCommand extends PaperCommand implements EmptyTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;
    private final WorldRegistry worldRegistry;

    public ListCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.worldRegistry = plugin.getWorldRegistry();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length != 1) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_LIST_USAGE));
            return;
        }
        final Collection<WorldContext> worldContexts = this.worldRegistry.getWorlds();
        final TextComponent.Builder componentBuilder = Component.text();

        for (final WorldContext worldContext : worldContexts) {
            if (!componentBuilder.children().isEmpty()) {
                componentBuilder.append(Component.text(", ", NamedTextColor.GRAY));
            }
            final NamedTextColor nameColor = (worldContext.isLoaded() ? NamedTextColor.GREEN : NamedTextColor.RED);
            final TextComponent.Builder hoverBuilder = Component.text()
                    .append(Component.text("Test")).append(Component.newline())
                    .append(Component.text("")).append(Component.newline());
            final boolean underline = commandSender instanceof final Player player && player.getWorld().getName().equals(worldContext.getName());

            final TextComponent.Builder worldComponent = Component.text()
                    .content(worldContext.getName())
                    .color(nameColor)
                    .hoverEvent(hoverBuilder.build())
                    .clickEvent(ClickEvent.runCommand("/world teleport " + worldContext.getName()));

            if (underline) {
                worldComponent.decorate(TextDecoration.UNDERLINED);
            }
            componentBuilder.append(worldComponent);
        }
        commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_LIST_MESSAGE)
                .replaceText(builder -> builder.match("%world_names%").replacement(componentBuilder.build())));

    }

}
