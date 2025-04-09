package com.dev7ex.multiworld.command.world.whitelist;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import com.dev7ex.multiworld.world.DefaultWorldConfiguration;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 15.02.2024
 */
@BukkitCommandProperties(name = "list", permission = "multiworld.command.whitelist.list")
public class ListCommand extends BukkitCommand implements BukkitTabCompleter {

    private final DefaultTranslationProvider translationProvider;
    private final DefaultWorldConfiguration worldConfiguration;
    private final DefaultWorldProvider worldProvider;

    public ListCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.translationProvider = plugin.getTranslationProvider();
        this.worldConfiguration = plugin.getWorldConfiguration();
        this.worldProvider = plugin.getWorldProvider();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        final BukkitWorldHolder worldHolder = this.worldProvider.getWorldHolder(arguments[1])
                .orElseThrow();

        if (worldHolder.getWhitelist().isEmpty()) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.whitelist.list.empty")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        final StringBuilder stringBuilder = new StringBuilder();

        for (final String name : worldHolder.getWhitelist()) {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append(ChatColor.GRAY);
                stringBuilder.append(", ");
            }
            stringBuilder.append(Bukkit.getPlayer(name) != null ? ChatColor.GREEN : ChatColor.RED).append(name);
        }
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender,"commands.world.whitelist.list.message")
                .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                .replaceAll("%world_name%", arguments[1])
                .replaceAll("%player_names%", stringBuilder.toString()));
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        return Collections.emptyList();
    }

}
