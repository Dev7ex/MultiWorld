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
import com.dev7ex.multiworld.api.world.backup.WorldBackupFactory;
import com.dev7ex.multiworld.api.world.backup.WorldBackupRegistry;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 04.01.2025
 */
@PaperCommandProperties(name = "create", permission = "multiworld.command.world.backup.create")
public class CreateCommand extends PaperCommand implements ArgumentTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;
    private final WorldBackupFactory backupFactory;
    private final WorldBackupRegistry backupRegistry;
    private final WorldRegistry worldRegistry;

    public CreateCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.backupFactory = plugin.getBackupFactory();
        this.backupRegistry = plugin.getBackupRegistry();
        this.worldRegistry = plugin.getWorldRegistry();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length != 3) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_CREATE_USAGE));
            return;
        }
        final String originalWorldName = arguments[2].asString();
        final String worldName;

        if (originalWorldName.equalsIgnoreCase("%creator_name%")) {
            worldName = originalWorldName.replaceAll("%creator_name%", commandSender.getName());

        } else {
            worldName = originalWorldName;
        }

        this.worldRegistry.find(worldName).ifPresentOrElse(worldContext -> {
            this.backupRegistry.findLatest(worldContext).ifPresentOrElse(backup -> {
                if ((System.currentTimeMillis() - backup.getTimestamp()) < 60_000) {
                    commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_BACKUP_CREATION_DELAY)
                            .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(worldName))
                            .replaceText(builder -> builder.match("%seconds%").replacement("60")));

                } else {
                    this.backupFactory.create(commandSender, worldContext);
                }

            }, () -> {
                this.backupFactory.create(commandSender, worldContext);
            });


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
        return this.worldRegistry.getWorlds()
                .stream()
                .map(WorldContext::getName)
                .toList();
    }

}
