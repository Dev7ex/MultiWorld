package com.dev7ex.multiworld.command.world.whitelist;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.world.WorldProperty;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 15.02.2024
 */
@CommandProperties(name = "enable", permission = "multiworld.command.whitelist.enable")
public class EnableCommand extends BukkitCommand implements TabCompleter {

    public EnableCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        final BukkitWorldHolder worldHolder = MultiWorldPlugin.getInstance()
                .getWorldProvider()
                .getWorldHolder(arguments[1])
                .orElseThrow();

        if (worldHolder.isWhitelistEnabled()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.enable.already-enabled")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return true;
        }
        worldHolder.setWhitelistEnabled(true);
        MultiWorldPlugin.getInstance().getWorldConfiguration().write(worldHolder, WorldProperty.WHITELIST_ENABLED, true);
        commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.enable.successfully-enabled")
                .replaceAll("%prefix%", super.getPrefix())
                .replaceAll("%world_name%", arguments[1]));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final Command command,
                                      @NotNull final String commandLabel, @NotNull final String[] arguments) {
        return Collections.emptyList();
    }

}
