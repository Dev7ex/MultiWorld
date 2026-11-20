package com.dev7ex.multiworld.command.world.backup;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.WorldManager;
import com.dev7ex.multiworld.api.world.WorldRegistry;
import com.dev7ex.multiworld.api.world.backup.WorldBackupManager;
import com.dev7ex.multiworld.api.world.backup.WorldBackupRegistry;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Dev7ex
 * @since 05.01.2025
 */
@PaperCommandProperties(name = "restore", permission = "multiworld.command.world.backup.restore")
public class RestoreCommand extends PaperCommand implements ArgumentTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;
    private final WorldBackupManager backupManager;
    private final WorldManager worldManager;
    private final WorldBackupRegistry backupRegistry;
    private final WorldRegistry worldRegistry;

    public RestoreCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.worldManager = plugin.getWorldManager();
        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.backupManager = plugin.getBackupManager();
        this.backupRegistry = plugin.getBackupRegistry();
        this.worldRegistry = plugin.getWorldRegistry();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length != 4) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_RESTORE_USAGE));
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
            final String backupName = arguments[3].asString();

            this.backupRegistry.find(commandSender, worldContext, backupName).ifPresentOrElse(backup -> {
                this.backupManager.restore(commandSender, worldContext, backup);

            }, () -> {
                commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_BACKUP_NOT_EXISTS)
                        .replaceText(builder -> builder.match("%world_backup_name%").replacement(backupName)));
            });

        }, () -> {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_EXISTS)
                    .replaceText(builder -> builder.match("%world_name%").replacement(worldName)));
        });
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if ((arguments.length < 3) || (arguments.length > 4)) {
            return Collections.emptyList();
        }
        if (arguments.length == 3) {
            return this.backupRegistry.getWorldsWithBackups().stream()
                    .sorted(Comparator.comparing(WorldContext::getName))
                    .map(WorldContext::getName)
                    .toList();
        }
        final String worldName = arguments[2].asString();

        return this.worldRegistry.find(worldName)
                .filter(this.backupRegistry.getWorldsWithBackups()::contains)
                .map(holder -> this.backupRegistry.getBackups(holder).stream()
                        .map(backup -> backup.getFormattedName(
                                this.localizationRegistry.getDateTimeFormat(commandSender)))
                        .toList())
                .orElse(Collections.emptyList());
    }

}
