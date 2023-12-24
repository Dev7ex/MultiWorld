package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.world.WorldProperty;
import com.dev7ex.multiworld.world.DefaultWorldConfiguration;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 29.06.2023
 */
@CommandProperties(name = "whitelist", permission = "multiworld.command.world.whitelist")
public class WhitelistCommand extends BukkitCommand implements TabCompleter {

    public WhitelistCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if ((arguments.length < 3) || (arguments.length > 4)) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.usage")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }

        if (arguments[1].equalsIgnoreCase("%creator_name%")) {
            arguments[1] = arguments[1].replaceAll("%creator_name%", commandSender.getName());
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-not-exists")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return true;
        }
        final DefaultWorldConfiguration worldConfiguration = MultiWorldPlugin.getInstance().getWorldConfiguration();
        final BukkitWorldHolder worldHolder = MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).orElseThrow();

        if (arguments.length == 3) {
            switch (arguments[2]) {
                case "enable":
                case "on":
                    if (worldHolder.isWhitelistEnabled()) {
                        commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.enable.already-enabled")
                                .replaceAll("%prefix%", super.getPrefix())
                                .replaceAll("%world_name%", arguments[1]));
                        return true;
                    }
                    worldHolder.setWhitelistEnabled(true);
                    worldConfiguration.write(worldHolder, WorldProperty.WHITELIST_ENABLED, true);
                    commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.enable.successfully-enabled")
                            .replaceAll("%prefix%", super.getPrefix())
                            .replaceAll("%world_name%", arguments[1]));
                    return true;

                case "disable":
                case "off":
                    if (!worldHolder.isWhitelistEnabled()) {
                        commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.disable.already-disabled")
                                .replaceAll("%prefix%", super.getPrefix())
                                .replaceAll("%world_name%", arguments[1]));
                        return true;
                    }
                    worldHolder.setWhitelistEnabled(false);
                    worldConfiguration.write(worldHolder, WorldProperty.WHITELIST_ENABLED, false);
                    commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.disable.successfully-disabled")
                            .replaceAll("%prefix%", super.getPrefix())
                            .replaceAll("%world_name%", arguments[1]));
                    return true;

                case "list":
                    if (worldHolder.getWhitelist().isEmpty()) {
                        commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.list.empty")
                                .replaceAll("%prefix%", super.getPrefix())
                                .replaceAll("%world_name%", arguments[1]));
                        return true;
                    }
                    final StringBuilder stringBuilder = new StringBuilder();

                    for (final String name : worldHolder.getWhitelist()) {
                        if (stringBuilder.length() > 0) {
                            stringBuilder.append(ChatColor.GRAY);
                            stringBuilder.append(", ");
                        }
                        stringBuilder.append(Bukkit.getPlayer(name) != null ? ChatColor.GREEN : ChatColor.RED).append(name);
                    }
                    commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.list.message")
                            .replaceAll("%prefix%", super.getPrefix())
                            .replaceAll("%world_name%", arguments[1])
                            .replaceAll("%player_names%", stringBuilder.toString()));
                    return true;

                default:
                    commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.usage")
                            .replaceAll("%prefix%", super.getPrefix()));
                    return true;
            }
        }

        switch (arguments[2]) {
            case "add":
                if (worldHolder.getWhitelist().contains(arguments[3])) {
                    commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.add.already-added")
                            .replaceAll("%prefix%", super.getPrefix())
                            .replaceAll("%world_name%", arguments[1])
                            .replaceAll("%player_name%", arguments[3]));
                    return true;
                }
                worldHolder.getWhitelist().add(arguments[3]);
                worldConfiguration.write(worldHolder, WorldProperty.WHITELIST, worldHolder.getWhitelist());
                commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.add.successfully-added")
                        .replaceAll("%prefix%", super.getPrefix())
                        .replaceAll("%world_name%", arguments[1])
                        .replaceAll("%player_name%", arguments[3]));
                return true;

            case "remove":
                if (!worldHolder.getWhitelist().contains(arguments[3])) {
                    commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.remove.already-removed")
                            .replaceAll("%prefix%", super.getPrefix())
                            .replaceAll("%world_name%", arguments[1])
                            .replaceAll("%player_name%", arguments[3]));
                    return true;
                }
                worldHolder.getWhitelist().remove(arguments[3]);
                worldConfiguration.write(worldHolder, WorldProperty.WHITELIST, worldHolder.getWhitelist());
                commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.remove.successfully-removed")
                        .replaceAll("%prefix%", super.getPrefix())
                        .replaceAll("%world_name%", arguments[1])
                        .replaceAll("%player_name%", arguments[3]));
                return true;

            default:
                commandSender.sendMessage(super.getConfiguration().getString("messages.commands.whitelist.usage")
                        .replaceAll("%prefix%", super.getPrefix()));
                return true;
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final Command command,
                                      @NotNull final String commandLabel, @NotNull final String[] arguments) {

        if ((arguments.length < 2) || (arguments.length > 4)) {
            return Collections.emptyList();
        }

        if (arguments.length == 2) {
            return new ArrayList<>(MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders().keySet());
        }

        if (arguments.length == 3) {
            return List.of("enable", "disable", "add", "list", "remove");
        }
        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).toList();
    }

}
