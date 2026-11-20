package com.dev7ex.multiworld.command.world;

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
import com.dev7ex.multiworld.api.world.backup.WorldBackupRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author MGG
 * @since 20.11.2026
 */
@PaperCommandProperties(name = "unload", permission = "multiworld.command.world.unload")
public class UnloadCommand extends PaperCommand implements ArgumentTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;
    private final WorldRegistry worldRegistry;
    private final WorldBackupRegistry backupRegistry;
    private final WorldManager worldManager;

    public UnloadCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.worldRegistry = plugin.getWorldRegistry();
        this.backupRegistry = plugin.getBackupRegistry();
        this.worldManager = plugin.getWorldManager();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length != 2) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_UNLOAD_USAGE));
            return;
        }
        final String worldName = arguments[1].asString();

        this.worldRegistry.find(worldName).ifPresentOrElse(worldContext -> {
            if (Bukkit.getWorld(worldContext.getKey()) == null) {
                this.sendMessage(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_LOADED, worldContext);
                return;
            }

            if (this.backupRegistry.isLocked(worldContext)) {
                this.sendMessage(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_BACKUP_IN_PROGRESS, worldContext);
                return;
            }
            this.sendMessage(commandSender, WorldLocalizationEntry.COMMAND_WORLD_UNLOAD_STARTING, worldContext);

            switch (this.worldManager.unload(commandSender, worldContext.getKey(), true)) {
                case SUCCESS -> this.sendMessage(commandSender, WorldLocalizationEntry.COMMAND_WORLD_UNLOAD_FINISHING, worldContext);
                case NOT_LOADED -> this.sendMessage(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_LOADED, worldContext);
                case UNSUPPORTED -> this.sendMessage(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_UNLOAD_PROTECTED, worldContext);
                default -> this.sendMessage(commandSender, WorldLocalizationEntry.COMMAND_WORLD_UNLOAD_FAILED, worldContext);
            }

        }, () -> {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_EXISTS)
                    .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(worldName)));
        });
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length != 2) {
            return Collections.emptyList();
        }
        return this.worldRegistry.getWorlds()
                .stream()
                .filter(worldContext -> Bukkit.getWorld(worldContext.getKey()) != null)
                .map(WorldContext::getName)
                .sorted()
                .toList();
    }

    private void sendMessage(@NotNull final CommandSender commandSender, @NotNull final WorldLocalizationEntry entry, @NotNull final WorldContext worldContext) {
        commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, entry)
                .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(worldContext.getName())));
    }

}
