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
import com.dev7ex.multiworld.api.world.backup.WorldBackupFactory;
import com.dev7ex.multiworld.api.world.backup.WorldBackupRegistry;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Dev7ex
 * @since 04.01.2025
 */
@PaperCommandProperties(name = "delete", permission = "multiworld.command.world.backup.delete")
public class DeleteCommand extends PaperCommand implements ArgumentTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;
    private final WorldBackupRegistry backupRegistry;
    private final WorldBackupFactory backupFactory;
    private final WorldRegistry worldRegistry;

    public DeleteCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.backupRegistry = plugin.getBackupRegistry();
        this.backupFactory = plugin.getBackupFactory();
        this.worldRegistry = plugin.getWorldRegistry();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length != 4) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_DELETE_USAGE));
            return;
        }
        final String originalWorldName = arguments[2].asString();
        final String worldName;

        if (originalWorldName.equalsIgnoreCase("%creator_name%")) {
            worldName = originalWorldName.replaceAll("%creator_name%", commandSender.getName());

        } else {
            worldName = originalWorldName;
        }

        if (this.worldRegistry.find(worldName).isEmpty()) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_EXISTS)
                    .replaceText(builder -> builder.match("%world_name%").replacement(worldName)));
            return;
        }
        final WorldContext context = this.worldRegistry.find(worldName).get();
        final String backupName = arguments[3].asString();
        if (this.backupRegistry.find(commandSender, context, backupName).isEmpty()) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_BACKUP_NOT_EXISTS)
                    .replaceText(builder -> builder.match("%world_backup_name%").replacement(backupName)));
            return;
        }
        final WorldBackup worldBackup = this.backupRegistry.find(commandSender, context, backupName).get();
        this.backupFactory.delete(commandSender, worldBackup);
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
                .map(worldHolder -> this.backupRegistry.getBackups(worldHolder).stream()
                        .map(backup -> backup.getFormattedName(this.localizationRegistry.getDateTimeFormat(commandSender)))
                        .toList()
                ).orElse(Collections.emptyList());
    }

}
