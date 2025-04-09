package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import com.dev7ex.multiworld.world.DefaultWorldManager;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import com.dev7ex.multiworld.world.generator.DefaultWorldGeneratorProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@BukkitCommandProperties(name = "load", permission = "multiworld.command.world.load")
public class LoadCommand extends BukkitCommand implements BukkitTabCompleter {

    private final DefaultTranslationProvider translationProvider;
    private final DefaultWorldManager worldManager;
    private final DefaultWorldProvider worldProvider;

    public LoadCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.translationProvider = plugin.getTranslationProvider();
        this.worldManager = plugin.getWorldManager();
        this.worldProvider = plugin.getWorldProvider();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 2) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "messages.commands.load.usage")
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

        if (Bukkit.getWorld(arguments[1]) != null) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "general.world.already-loaded")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        this.worldManager.loadWorld(commandSender.getName(), arguments[1]);
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        return this.worldProvider.getWorldHolders()
                .values()
                .stream()
                .filter(bukkitWorldHolder -> !bukkitWorldHolder.isLoaded())
                .map(BukkitWorldHolder::getName)
                .toList();
    }

}
