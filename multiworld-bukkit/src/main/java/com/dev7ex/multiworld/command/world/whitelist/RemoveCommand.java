package com.dev7ex.multiworld.command.world.whitelist;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.world.WorldProperty;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 15.02.2024
 */
@BukkitCommandProperties(name = "remove", permission = "multiworld.command.whitelist.remove")
public class RemoveCommand extends BukkitCommand implements BukkitTabCompleter {

    public RemoveCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        final BukkitWorldHolder worldHolder = MultiWorldPlugin.getInstance()
                .getWorldProvider()
                .getWorldHolder(arguments[1])
                .orElseThrow();

        if (!worldHolder.getWhitelist().contains(arguments[3])) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.remove.already-removed")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1])
                    .replaceAll("%player_name%", arguments[3]));
            return;
        }
        worldHolder.getWhitelist().remove(arguments[3]);
        MultiWorldPlugin.getInstance().getWorldConfiguration().write(worldHolder, WorldProperty.WHITELIST, worldHolder.getWhitelist());
        commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.remove.successfully-removed")
                .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                .replaceAll("%world_name%", arguments[1])
                .replaceAll("%player_name%", arguments[3]));
        return;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 4) {
            return Collections.emptyList();
        }

        final BukkitWorldHolder worldHolder = MultiWorldPlugin.getInstance()
                .getWorldProvider()
                .getWorldHolder(arguments[1])
                .orElseThrow();

        if (worldHolder.getWhitelist().isEmpty()) {
            return Collections.emptyList();
        }
        return worldHolder.getWhitelist();
    }

}
