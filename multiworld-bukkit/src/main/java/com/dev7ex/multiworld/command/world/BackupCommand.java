package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import com.dev7ex.multiworld.world.DefaultWorldManager;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 19.06.2023
 */
@BukkitCommandProperties(name = "backup", permission = "multiworld.command.world.backup")
public class BackupCommand extends BukkitCommand implements BukkitTabCompleter {

    private final DefaultTranslationProvider translationProvider;
    private final DefaultWorldManager worldManager;
    private final DefaultWorldProvider worldProvider;

    public BackupCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.translationProvider = plugin.getTranslationProvider();
        this.worldManager = plugin.getWorldManager();
        this.worldProvider = plugin.getWorldProvider();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 2) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.backup.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }

        if (arguments[1].equalsIgnoreCase("%creator_name%")) {
            arguments[1] = arguments[1].replaceAll("%creator_name%", commandSender.getName());
        }

        if (!this.worldProvider.isRegistered(arguments[1])) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender,"general.world.not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        this.worldManager.createBackup(commandSender.getName(), arguments[1]);
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 2) {
            return Collections.emptyList();
        }
        return new ArrayList<>(this.worldProvider.getWorldHolders().keySet());
    }

}
