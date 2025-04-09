package com.dev7ex.multiworld.command.world.whitelist;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.world.WorldProperty;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import com.dev7ex.multiworld.world.DefaultWorldConfiguration;
import com.dev7ex.multiworld.world.DefaultWorldManager;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 15.02.2024
 */
@BukkitCommandProperties(name = "add", permission = "multiworld.command.whitelist.add")
public class AddCommand extends BukkitCommand implements BukkitTabCompleter {

    private final DefaultTranslationProvider translationProvider;
    private final DefaultWorldConfiguration worldConfiguration;
    private final DefaultWorldProvider worldProvider;

    public AddCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.translationProvider = plugin.getTranslationProvider();
        this.worldConfiguration = plugin.getWorldConfiguration();
        this.worldProvider = plugin.getWorldProvider();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        final BukkitWorldHolder worldHolder = this.worldProvider
                .getWorldHolder(arguments[1])
                .orElseThrow();

        if (worldHolder.getWhitelist().contains(arguments[3])) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.whitelist.add.already-added")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1])
                    .replaceAll("%player_name%", arguments[3]));
            return;
        }
        worldHolder.getWhitelist().add(arguments[3]);
        this.worldConfiguration.write(worldHolder, WorldProperty.WHITELIST, worldHolder.getWhitelist());
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.whitelist.add.successfully-added")
                .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                .replaceAll("%world_name%", arguments[1])
                .replaceAll("%player_name%", arguments[3]));
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 4) {
            return Collections.emptyList();
        }
        final BukkitWorldHolder worldHolder = this.worldProvider
                .getWorldHolder(arguments[1])
                .orElseThrow();

        final List<String> completions = Lists.newArrayList(Bukkit.getOnlinePlayers()
                .stream()
                .map(HumanEntity::getName)
                .toList());
        completions.removeAll(worldHolder.getWhitelist());

        return completions;
    }

}