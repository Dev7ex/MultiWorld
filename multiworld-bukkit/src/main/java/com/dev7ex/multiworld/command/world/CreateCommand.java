package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.common.util.Numbers;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.world.WorldType;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@BukkitCommandProperties(name = "create", permission = "multiworld.command.world.create")
public class CreateCommand extends BukkitCommand implements BukkitTabCompleter {

    public CreateCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 3) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.create.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }

        if (arguments[1].equalsIgnoreCase("%creator_name%")) {
            arguments[1] = arguments[1].replaceAll("%creator_name%", commandSender.getName());
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().isRegistered(arguments[1])) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-already-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }

        if (Numbers.isLong(arguments[2])) {
            MultiWorldPlugin.getInstance().getWorldManager().createWorld(commandSender.getName(), arguments[1], Long.parseLong(arguments[2]));
            return;
        }

        if (MultiWorldPlugin.getInstance().getWorldGeneratorProvider().exists(arguments[2])) {
            MultiWorldPlugin.getInstance().getWorldManager().createWorld(commandSender.getName(), arguments[1], arguments[2]);
            return;
        }

        final Optional<WorldType> typeOptional = WorldType.fromString(arguments[2].toUpperCase());

        if (typeOptional.isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-type-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }
        MultiWorldPlugin.getInstance().getWorldManager().createWorld(commandSender.getName(), arguments[1], typeOptional.get());
        return;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {

        if ((arguments.length < 2) || (arguments.length > 3)) {
            return Collections.emptyList();
        }

        if (arguments.length == 2) {
            return List.of("%creator_name%");
        }
        if (MultiWorldPlugin.getInstance().getWorldGeneratorProvider().getCustomGenerators().isEmpty()) {
            return WorldType.toStringList();
        }
        final List<String> completions = new ArrayList<>();
        completions.addAll(WorldType.toStringList());
        completions.addAll(MultiWorldPlugin.getInstance().getWorldGeneratorProvider().getCustomGenerators().values());

        return completions;
    }

}
