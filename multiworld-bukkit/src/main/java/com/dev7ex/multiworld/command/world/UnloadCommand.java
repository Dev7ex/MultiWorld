package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@BukkitCommandProperties(name = "unload", permission = "multiworld.command.world.unload")
public class UnloadCommand extends BukkitCommand implements BukkitTabCompleter {

    public UnloadCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        final DefaultTranslationProvider translationProvider = MultiWorldPlugin.getInstance().getTranslationProvider();

        if (arguments.length != 2) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "messages.commands.load.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }

        if (arguments[1].equalsIgnoreCase("%creator_name%")) {
            arguments[1] = arguments[1].replaceAll("%creator_name%", commandSender.getName());
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).isEmpty()) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "general.world.not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }

        if (arguments[1].equalsIgnoreCase(super.getConfiguration().getString("settings.defaults.normal-world"))) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "general.world.unload-locked")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }

        if (Bukkit.getWorld(arguments[1]) == null) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "general.world.not-loaded")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
        }
        MultiWorldPlugin.getInstance().getWorldManager().unloadWorld(commandSender.getName(), arguments[1]);
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        return MultiWorldPlugin.getInstance()
                .getWorldProvider()
                .getWorldHolders()
                .values()
                .stream()
                .filter(BukkitWorldHolder::isLoaded)
                .map(BukkitWorldHolder::getName)
                .toList();
    }

}
