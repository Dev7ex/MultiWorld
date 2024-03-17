package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldConfiguration;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldProvider;
import com.dev7ex.multiworld.api.world.WorldEnvironment;
import com.dev7ex.multiworld.api.world.WorldProperty;
import com.dev7ex.multiworld.api.world.WorldType;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 17.03.2024
 */
@BukkitCommandProperties(name = "link", permission = "multiworld.command.world.link")
public class LinkCommand extends BukkitCommand implements BukkitTabCompleter {

    public LinkCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 4) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.link.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }
        final BukkitWorldProvider worldProvider = MultiWorldPlugin.getInstance().getWorldProvider();
        final BukkitWorldConfiguration worldConfiguration = MultiWorldPlugin.getInstance().getWorldConfiguration();

        if (worldProvider.getWorldHolder(arguments[1]).isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }

        final Optional<WorldEnvironment> environmentOptional = WorldEnvironment.fromString(arguments[2].toUpperCase());

        if (environmentOptional.isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.link.environment-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%environment_name%", arguments[2]));
            return;
        }
        final WorldEnvironment environment = environmentOptional.get();

        if (worldProvider.getWorldHolder(arguments[3]).isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[3]));
            return;
        }
        final BukkitWorldHolder worldHolder = worldProvider.getWorldHolder(arguments[1]).get();

        switch (environment) {
            case THE_END:
                worldHolder.setEndWorldName(arguments[3]);
                worldConfiguration.write(worldHolder, WorldProperty.END_WORLD, arguments[3]);

                commandSender.sendMessage(super.getConfiguration().getString("messages.commands.link.successfully-set")
                        .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                        .replaceAll("%world_name%", arguments[1])
                        .replaceAll("%environment_name%", arguments[2])
                        .replaceAll("%target_world_name%", arguments[3]));
                return;

            case NETHER:
                worldHolder.setNetherWorldName(arguments[3]);
                worldConfiguration.write(worldHolder, WorldProperty.NETHER_WORLD, arguments[3]);

                commandSender.sendMessage(super.getConfiguration().getString("messages.commands.link.successfully-set")
                        .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                        .replaceAll("%world_name%", arguments[1])
                        .replaceAll("%environment_name%", arguments[2])
                        .replaceAll("%target_world_name%", arguments[3]));
                return;

            case NORMAL:
                worldHolder.setNormalWorldName(arguments[3]);
                worldConfiguration.write(worldHolder, WorldProperty.NORMAL_WORLD, arguments[3]);

                commandSender.sendMessage(super.getConfiguration().getString("messages.commands.link.successfully-set")
                        .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                        .replaceAll("%world_name%", arguments[1])
                        .replaceAll("%environment_name%", arguments[2])
                        .replaceAll("%target_world_name%", arguments[3]));
                return;

            default:
                commandSender.sendMessage(super.getConfiguration().getString("messages.commands.link.usage")
                        .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
                break;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {

        if (arguments.length == 2) {
            return new ArrayList<>(MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders().keySet());
        }

        if (arguments.length == 3) {
            return WorldEnvironment.toStringList();
        }

        if (arguments.length == 4) {
            final Optional<WorldEnvironment> environmentOptional = WorldEnvironment.fromString(arguments[2]);

            if (environmentOptional.isEmpty()) {
                return Collections.emptyList();
            }
            final WorldEnvironment environment = environmentOptional.get();

            switch (environment) {
                case NETHER:
                    return MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders()
                            .values()
                            .stream()
                            .filter(worldHolder -> worldHolder.getType() == WorldType.NETHER)
                            .map(BukkitWorldHolder::getName)
                            .toList();

                case NORMAL:
                    return MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders()
                            .values()
                            .stream()
                            .filter(worldHolder -> worldHolder.getType().isOverWorld())
                            .map(BukkitWorldHolder::getName)
                            .toList();

                case THE_END:
                    return MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders()
                            .values()
                            .stream()
                            .filter(worldHolder -> worldHolder.getType() == WorldType.END)
                            .map(BukkitWorldHolder::getName)
                            .toList();
            }

        }
        return Collections.emptyList();
    }

}
