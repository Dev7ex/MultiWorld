package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.event.world.WorldFlagChangeEvent;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.world.WorldFlag;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 26.06.2023
 */
@CommandProperties(name = "flag", permission = "multiworld.command.world.flag")
public class FlagCommand extends BukkitCommand implements TabCompleter {

    public FlagCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 4) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.flag.usage")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-not-exists")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return true;
        }
        final Optional<WorldFlag> flagOptional = WorldFlag.fromString(arguments[2].toUpperCase());

        if (flagOptional.isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.flag.not-existing")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }
        final BukkitWorldHolder worldHolder = MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).orElseThrow();
        final WorldFlag flag = flagOptional.get();

        if (!flag.getValues().contains(arguments[3])) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.flag.value-not-existing")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%flag%", flag.toString()));
            return true;
        }
        final WorldFlagChangeEvent event = new WorldFlagChangeEvent(worldHolder, commandSender, flag, arguments[3]);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return true;
        }

        worldHolder.updateFlag(flag, arguments[3]);
        MultiWorldPlugin.getInstance().getWorldConfiguration().updateFlag(worldHolder, flag, arguments[3]);
        commandSender.sendMessage(super.getConfiguration().getString("messages.commands.flag.successfully-set")
                .replaceAll("%prefix%", super.getPrefix())
                .replaceAll("%flag%", flag.toString())
                .replaceAll("%value%", arguments[3])
                .replaceAll("%world_name%", arguments[1]));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final Command command,
                                      @NotNull final String commandLabel, @NotNull final String[] arguments) {
        if (arguments.length == 2) {
            return Lists.newArrayList(MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders().keySet());
        }

        if (arguments.length == 3) {
            return WorldFlag.toStringList();
        }
        final Optional<WorldFlag> flagOptional = WorldFlag.fromString(arguments[2].toUpperCase());

        if (flagOptional.isEmpty()) {
            return Collections.emptyList();
        }
        return Lists.newArrayList(flagOptional.get().getValues());
    }

}
