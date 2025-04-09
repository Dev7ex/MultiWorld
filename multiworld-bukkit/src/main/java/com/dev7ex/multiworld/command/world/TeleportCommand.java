package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.event.user.WorldUserTeleportWorldEvent;
import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUser;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@BukkitCommandProperties(name = "teleport", permission = "multiworld.command.world.teleport", aliases = "tp")
public class TeleportCommand extends BukkitCommand implements BukkitTabCompleter {

    public TeleportCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        final DefaultTranslationProvider translationProvider = MultiWorldPlugin.getInstance().getTranslationProvider();

        //   /world teleport <World>   /world teleport <Player> <World>
        if ((arguments.length < 2) || (arguments.length > 3)) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "commands.world.teleport.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }
        final DefaultWorldProvider worldProvider = MultiWorldPlugin.getInstance().getWorldProvider();

        if (arguments.length == 2) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(translationProvider.getMessage(commandSender, "general.no-console-command")
                        .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                        .replaceAll("%command_name%", super.getName()));
                return;
            }
            final Player player = (Player) commandSender;
            final BukkitWorldUser user = MultiWorldPlugin.getInstance()
                    .getUserProvider()
                    .getUser(player.getUniqueId())
                    .orElseThrow();

            if (MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).isEmpty()) {
                commandSender.sendMessage(translationProvider.getMessage(commandSender, "general.world.not-exists")
                        .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                        .replaceAll("%world_name%", arguments[1]));
                return;
            }

            if (Bukkit.getWorld(arguments[1]) == null) {
                commandSender.sendMessage(translationProvider.getMessage(commandSender, "general.world.not-loaded")
                        .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                        .replaceAll("%world_name%", arguments[1]));
                return;
            }
            final World world = Bukkit.getWorld(arguments[1]);
            final BukkitWorldHolder lastWorldHolder = worldProvider.getWorldHolder(player.getLocation().getWorld().getName())
                    .orElseThrow();
            final BukkitWorldHolder nextWorldHolder = worldProvider.getWorldHolder(arguments[1])
                    .orElseThrow();

            if (player.getLocation().getWorld().getName().equalsIgnoreCase(arguments[1])) {
                commandSender.sendMessage(translationProvider.getMessage(commandSender, "commands.world.teleport.sender-already-there")
                        .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                        .replaceAll("%world_name%", arguments[1]));
                return;
            }

            if ((world.getEnvironment() == World.Environment.NETHER) && (!super.getConfiguration().getBoolean("settings.access-nether-world-via-command"))) {
                commandSender.sendMessage(translationProvider.getMessage(commandSender, "general.world.nether-not-accessible")
                        .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                        .replaceAll("%reason%", "Locked"));
                return;
            }

            if ((world.getEnvironment() == World.Environment.THE_END) && (!super.getConfiguration().getBoolean("settings.access-end-world-via-command"))) {
                commandSender.sendMessage(translationProvider.getMessage(commandSender, "general.world.end-not-accessible")
                        .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                        .replaceAll("%reason%", "Locked"));
                return;
            }

            final WorldUserTeleportWorldEvent event = new WorldUserTeleportWorldEvent(user, lastWorldHolder, nextWorldHolder);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
            user.teleport(event.getNextWorldHolder());
            return;
        }
        final Player target = Bukkit.getPlayer(arguments[1]);

        if (target == null) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "general.no-player-found")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%player_name%", arguments[1]));
            return;
        }
        final BukkitWorldUser user = MultiWorldPlugin.getInstance()
                .getUserProvider()
                .getUser(target.getUniqueId())
                .orElseThrow();

        if (!MultiWorldPlugin.getInstance().getWorldProvider().isRegistered(arguments[2])) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "general.world.not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[2]));
            return;
        }

        if (Bukkit.getWorld(arguments[2]) == null) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "general.world.not-loaded")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[2]));
            return;
        }
        final World world = Bukkit.getWorld(arguments[2]);

        if (target.getLocation().getWorld().getName().equalsIgnoreCase(arguments[2])) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "commands.world.teleport.target-already-there")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%player_name%", arguments[1])
                    .replaceAll("%world_name%", arguments[2]));
            return;
        }

        if ((world.getEnvironment() == World.Environment.NETHER) && (!super.getConfiguration().getBoolean("settings.access-nether-world-via-command"))) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "general.world.nether-not-accessible")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%reason%", "Locked"));
            return;
        }

        if ((world.getEnvironment() == World.Environment.THE_END) && (!super.getConfiguration().getBoolean("settings.access-end-world-via-command"))) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "general.world.end-not-accessible")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%reason%", "Locked"));
            return;
        }

        final BukkitWorldHolder lastWorldHolder = worldProvider.getWorldHolder(target.getLocation().getWorld().getName())
                .orElseThrow();
        final BukkitWorldHolder nextWorldHolder = worldProvider.getWorldHolder(arguments[2])
                .orElseThrow();

        final WorldUserTeleportWorldEvent event = new WorldUserTeleportWorldEvent(user, lastWorldHolder, nextWorldHolder);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        user.teleport(nextWorldHolder);
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if ((arguments.length < 2) || (arguments.length > 3)) {
            return Collections.emptyList();
        }
        final DefaultWorldProvider worldProvider = MultiWorldPlugin.getInstance().getWorldProvider();
        final List<String> completions = Lists.newArrayList(worldProvider.getWorldHolders().keySet());

        if (arguments.length == 2) {
            if (commandSender instanceof Player) {
                return completions;

            } else {
                return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).toList();
            }
        }

        if (commandSender instanceof Player) {
            if (Bukkit.getPlayer(arguments[1]) == null) {
                return Collections.emptyList();
            }
            return completions;

        } else {
            return completions;
        }
    }
}
