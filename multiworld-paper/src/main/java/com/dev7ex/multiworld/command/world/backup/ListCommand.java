package com.dev7ex.multiworld.command.world.backup;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.WorldRegistry;
import com.dev7ex.multiworld.api.world.backup.WorldBackup;
import com.dev7ex.multiworld.api.world.backup.WorldBackupRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;

/**
 * @author Dev7ex
 * @since 05.01.2025
 */
@PaperCommandProperties(name = "list", permission = "multiworld.command.world.backup.list")
public class ListCommand extends PaperCommand implements ArgumentTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;
    private final WorldBackupRegistry backupRegistry;
    private final WorldRegistry worldRegistry;

    public ListCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.backupRegistry = plugin.getBackupRegistry();
        this.worldRegistry = plugin.getWorldRegistry();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length != 3) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_LIST_USAGE));
            return;
        }
        final String originalWorldName = arguments[2].asString();
        final String worldName;

        if (originalWorldName.equalsIgnoreCase("%creator_name%")) {
            worldName = originalWorldName.replaceAll("%creator_name%", commandSender.getName());

        } else {
            worldName = originalWorldName;
        }

        this.worldRegistry.find(worldName).ifPresentOrElse(context -> {
            final NavigableSet<WorldBackup> worldBackups = this.backupRegistry.getBackups(context);

            if (worldBackups.isEmpty()) {
                commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_BACKUP_NO_ENTRIES)
                        .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(worldName)));
                return;
            }

            final TextComponent.Builder componentBuilder = Component.text();
            final WorldBackup lastBackup = this.backupRegistry.findLatest(context).orElseThrow();

            for (final WorldBackup worldBackup : worldBackups) {
                if (!componentBuilder.children().isEmpty()) {
                    componentBuilder.append(Component.text(", ", NamedTextColor.GRAY));
                }

                final boolean underline = lastBackup.equals(worldBackup);

                final TextComponent.Builder backupComponent = Component.text()
                        .content(worldBackup.getFormattedName(this.localizationRegistry.getDateTimeFormat(commandSender)))
                        .color(NamedTextColor.GREEN);

                if (underline) {
                    backupComponent.decorate(TextDecoration.UNDERLINED);
                }

                componentBuilder.append(backupComponent);
            }

            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_LIST_MESSAGE)
                    .replaceText(builder -> builder.match("%world_backup_names%").replacement(componentBuilder.build())));

        }, () -> {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_EXISTS)
                    .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(worldName)));
        });
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length != 3) {
            return Collections.emptyList();
        }
        return this.backupRegistry.getWorldsWithBackups().stream()
                .sorted(Comparator.comparing(WorldContext::getName))
                .map(WorldContext::getName)
                .toList();
    }

}
