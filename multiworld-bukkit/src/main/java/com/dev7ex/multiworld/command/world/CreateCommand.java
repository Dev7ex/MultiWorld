package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.common.util.Numbers;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.world.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@CommandProperties(name = "create", permission = "multiworld.command.world.create")
public class CreateCommand extends BukkitCommand implements TabCompleter {

    public CreateCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 3) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.create.usage")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().isRegistered(arguments[1])) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-already-exists")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }

        if (Numbers.isLong(arguments[2])) {
            MultiWorldPlugin.getInstance().getWorldManager().createWorld(commandSender.getName(), arguments[1], Long.parseLong(arguments[2]));
            return true;
        }

        if (MultiWorldPlugin.getInstance().getWorldGeneratorProvider().exists(arguments[1])) {
            MultiWorldPlugin.getInstance().getWorldManager().createWorld(commandSender.getName(), arguments[1], arguments[2]);
            return true;
        }

        final Optional<WorldType> typeOptional = WorldType.fromString(arguments[2].toUpperCase());

        if (typeOptional.isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-type-not-exists")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }
        MultiWorldPlugin.getInstance().getWorldManager().createWorld(commandSender.getName(), arguments[1], typeOptional.get());
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final Command command,
                                      @NotNull final String commandLabel, @NotNull final String[] arguments) {
        if (arguments.length != 3) {
            return Collections.emptyList();
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
