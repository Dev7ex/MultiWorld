package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.common.io.file.Files;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import com.dev7ex.multiworld.world.DefaultWorldManager;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@BukkitCommandProperties(name = "delete", permission = "multiworld.command.world.delete")
public class DeleteCommand extends BukkitCommand implements BukkitTabCompleter {

    private final DefaultTranslationProvider translationProvider;
    private final DefaultWorldManager worldManager;
    private final DefaultWorldProvider worldProvider;

    public DeleteCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.translationProvider = plugin.getTranslationProvider();
        this.worldManager = plugin.getWorldManager();
        this.worldProvider = plugin.getWorldProvider();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 2) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "messages.commands.delete.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }

        if (arguments[1].equalsIgnoreCase("%creator_name%")) {
            arguments[1] = arguments[1].replaceAll("%creator_name%", commandSender.getName());
        }

        if (this.worldProvider.getWorldHolder(arguments[1]).isEmpty()) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "general.world.not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        final File worldFolder = new File(Bukkit.getWorldContainer(), arguments[1]);

        if (!worldFolder.exists()) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "general.world.not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }

        if ((!Files.containsFile(worldFolder, "level.dat")) && (!Files.containsFile(worldFolder, "session.lock"))) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "general.world.not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }

        if (arguments[1].equalsIgnoreCase(MultiWorldPlugin.getInstance().getConfiguration().getString("settings.defaults.normal-world"))) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.delete.world.delete-locked")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        this.worldManager.deleteWorld(commandSender.getName(), arguments[1]);
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        final List<String> worlds = new ArrayList<>(this.worldProvider.getWorldHolders().keySet());
        worlds.remove(super.getConfiguration().getString("settings.defaults.normal-world"));
        return worlds;
    }

}
