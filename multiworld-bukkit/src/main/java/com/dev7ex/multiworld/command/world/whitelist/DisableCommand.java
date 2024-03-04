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
@BukkitCommandProperties(name = "disable", permission = "multiworld.command.whitelist.disable")
public class DisableCommand extends BukkitCommand implements BukkitTabCompleter {

    public DisableCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        final BukkitWorldHolder worldHolder = MultiWorldPlugin.getInstance()
                .getWorldProvider()
                .getWorldHolder(arguments[1])
                .orElseThrow();

        if (!worldHolder.isWhitelistEnabled()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.disable.already-disabled")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        worldHolder.setWhitelistEnabled(false);
        MultiWorldPlugin.getInstance().getWorldConfiguration().write(worldHolder, WorldProperty.WHITELIST_ENABLED, false);
        commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.disable.successfully-disabled")
                .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                .replaceAll("%world_name%", arguments[1]));
        return;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        return Collections.emptyList();
    }

}
