package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.common.util.Numbers;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.world.WorldType;
import com.dev7ex.multiworld.world.generator.DefaultWorldGeneratorProvider;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
        if (arguments.length != 4) {
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

        switch (arguments[2]) {
            case "-s":
                if (!Numbers.isLong(arguments[3])) {
                    commandSender.sendMessage(super.getConfiguration().getString("messages.general.invalid-seed")
                            .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
                    return;
                }
                MultiWorldPlugin.getInstance().getWorldManager().createWorld(commandSender.getName(), arguments[1], Long.parseLong(arguments[3]));
                break;

            case "-g":
                final DefaultWorldGeneratorProvider generatorProvider = MultiWorldPlugin.getInstance().getWorldGeneratorProvider();

                if (!generatorProvider.isRegistered(arguments[3])) {
                    commandSender.sendMessage(super.getConfiguration().getString("messages.general.invalid-generator")
                            .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
                    return;
                }
                MultiWorldPlugin.getInstance().getWorldManager().createWorld(commandSender.getName(), arguments[1], arguments[3]);
                break;

            case "-t":
                final Optional<WorldType> typeOptional = WorldType.fromString(arguments[3].toUpperCase());

                if (typeOptional.isEmpty()) {
                    commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-type-not-exists")
                            .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
                    return;
                }
                MultiWorldPlugin.getInstance().getWorldManager().createWorld(commandSender.getName(), arguments[1], typeOptional.get());
                break;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {

        if ((arguments.length < 2) || (arguments.length > 4)) {
            return Collections.emptyList();
        }

        if (arguments.length == 2) {
            return List.of("%creator_name%");
        }

        if (arguments.length == 3) {
            return List.of("-g", "-s", "-t");
        }

        switch (arguments[2]) {
            case "-g":
                return new ArrayList<>(MultiWorldPlugin.getInstance().getWorldGeneratorProvider().getCustomGenerators().values());

            case "-s":
                return Collections.singletonList(String.valueOf(new Random().nextLong()));

            case "-t":
                return WorldType.toStringList();

            default:
                return Collections.emptyList();
        }
    }

}
