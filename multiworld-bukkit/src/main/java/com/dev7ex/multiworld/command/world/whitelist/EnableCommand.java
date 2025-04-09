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
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 15.02.2024
 */
@BukkitCommandProperties(name = "enable", permission = "multiworld.command.whitelist.enable")
public class EnableCommand extends BukkitCommand implements BukkitTabCompleter {

    private final DefaultTranslationProvider translationProvider;
    private final DefaultWorldConfiguration worldConfiguration;
    private final DefaultWorldProvider worldProvider;

    public EnableCommand(@NotNull final MultiWorldPlugin plugin) {
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

        if (worldHolder.isWhitelistEnabled()) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.whitelist.enable.already-enabled")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        worldHolder.setWhitelistEnabled(true);
        this.worldConfiguration.write(worldHolder, WorldProperty.WHITELIST_ENABLED, true);
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.whitelist.enable.successfully-enabled")
                .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                .replaceAll("%world_name%", arguments[1]));
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        return Collections.emptyList();
    }

}
