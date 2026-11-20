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
import com.dev7ex.multiworld.api.world.backup.WorldBackupManager;
import com.dev7ex.multiworld.api.world.backup.WorldBackupRegistry;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@PaperCommandProperties(name = "clear", permission = "multiworld.command.world.backup.clear")
public class ClearCommand extends PaperCommand implements ArgumentTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;
    private final WorldBackupManager backupManager;
    private final WorldBackupRegistry backupRegistry;
    private final WorldBackupFactory backupFactory;
    private final WorldRegistry worldRegistry;

    public ClearCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.backupManager = plugin.getBackupManager();
        this.backupRegistry = plugin.getBackupRegistry();
        this.backupFactory = plugin.getBackupFactory();
        this.worldRegistry = plugin.getWorldRegistry();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length != 3) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_BACKUP_CLEAR_USAGE));
            return;
        }
        final String worldName = arguments[2].asString();

        this.worldRegistry.find(worldName).ifPresentOrElse(worldContext -> {
            if (this.backupRegistry.getBackups(worldContext).isEmpty()) {
                commandSender.sendMessage(
                        this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_BACKUP_NO_ENTRIES)
                                .replaceText(builder -> builder.match("%world_name%").replacement(worldName))
                );
                return;
            }
            this.backupFactory.clear(commandSender, worldContext);

        }, () -> {
            commandSender.sendMessage(
                    this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_EXISTS)
                            .replaceText(builder -> builder.match("%world_name%").replacement(worldName))
            );
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
