package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.event.world.WorldFlagChangeEvent;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.world.WorldFlag;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import com.dev7ex.multiworld.world.DefaultWorldConfiguration;
import com.dev7ex.multiworld.world.DefaultWorldManager;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 26.06.2023
 */
@BukkitCommandProperties(name = "flag", permission = "multiworld.command.world.flag")
public class FlagCommand extends BukkitCommand implements BukkitTabCompleter {

    private final DefaultTranslationProvider translationProvider;
    private final DefaultWorldConfiguration worldConfiguration;
    private final DefaultWorldManager worldManager;
    private final DefaultWorldProvider worldProvider;

    public FlagCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.translationProvider = plugin.getTranslationProvider();
        this.worldConfiguration = plugin.getWorldConfiguration();
        this.worldManager = plugin.getWorldManager();
        this.worldProvider = plugin.getWorldProvider();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 4) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.flag.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }

        if (arguments[1].equalsIgnoreCase("%creator_name%")) {
            arguments[1] = arguments[1].replaceAll("%creator_name%", commandSender.getName());
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).isEmpty()) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "general.world.not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        final Optional<WorldFlag> flagOptional = WorldFlag.fromString(arguments[2].toUpperCase());

        if (flagOptional.isEmpty()) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.flag.not-existing")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%flag_name%", arguments[2]));
            return;
        }
        final BukkitWorldHolder worldHolder = this.worldProvider.getWorldHolder(arguments[1]).orElseThrow();
        final WorldFlag flag = flagOptional.get();

        if (!flag.getValues().contains(arguments[3])) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.flag.invalid-value")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%value%", arguments[3])
                    .replaceAll("%flag_name%", flag.toString()));
            return;
        }
        final WorldFlagChangeEvent event = new WorldFlagChangeEvent(worldHolder, commandSender, flag, arguments[3]);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        worldHolder.updateFlag(flag, arguments[3]);
        this.worldConfiguration.updateFlag(worldHolder, flag, arguments[3]);
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.flag.successfully-set")
                .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                .replaceAll("%flag_name%", flag.toString())
                .replaceAll("%value%", arguments[3])
                .replaceAll("%world_name%", arguments[1]));
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length == 2) {
            return Lists.newArrayList(this.worldProvider.getWorldHolders().keySet());
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
