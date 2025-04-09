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
 * @since 26.06.2023
 */
@BukkitCommandProperties(name = "clone", permission = "multiworld.command.world.clone")
public class CloneCommand extends BukkitCommand implements BukkitTabCompleter {

    private final DefaultTranslationProvider translationProvider;
    private final DefaultWorldManager worldManager;
    private final DefaultWorldProvider worldProvider;

    public CloneCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.translationProvider = plugin.getTranslationProvider();
        this.worldManager = plugin.getWorldManager();
        this.worldProvider = plugin.getWorldProvider();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 3) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "messages.commands.clone.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }

        if (arguments[2].equalsIgnoreCase("%creator_name%")) {
            arguments[2] = arguments[2].replaceAll("%creator_name%", commandSender.getName());
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().isRegistered(arguments[2])) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender,"general.world.already-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[2]));
            return;
        }
        this.worldManager.cloneWorld(commandSender.getName(), arguments[1], arguments[2]);
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 2) {
            return Collections.emptyList();
        }
        return new ArrayList<>(this.worldProvider.getWorldHolders().keySet());
    }

}
