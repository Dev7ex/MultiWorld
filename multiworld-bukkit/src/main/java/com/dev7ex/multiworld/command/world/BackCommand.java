package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.event.user.WorldUserTeleportWorldEvent;
import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUser;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@BukkitCommandProperties(name = "back", permission = "multiworld.command.world.back")
public class BackCommand extends BukkitCommand {

    public BackCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        final DefaultTranslationProvider translationProvider = MultiWorldPlugin.getInstance().getTranslationProvider();

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "general.no-console-command")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%command_name%", super.getName()));
            return;
        }

        if (arguments.length != 1) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "commands.world.back.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }
        final Player player = (Player) commandSender;
        final BukkitWorldUser user = MultiWorldPlugin.getInstance().getUserProvider().getUser(player.getUniqueId()).orElseThrow();

        if (user.getLastLocation() == null) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "commands.world.back.world-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", player.getWorld().getName()));
            return;
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(user.getLastLocation().getWorldName()).isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("general.world.not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", player.getWorld().getName()));
            return;
        }
        final BukkitWorldHolder currentWorldHolder = MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(player.getWorld().getName()).orElseThrow();
        final BukkitWorldHolder nextWorldHolder = MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(user.getLastLocation().getWorldName()).orElseThrow();

        if (!nextWorldHolder.isLoaded()) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "general.world.not-loaded")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", player.getWorld().getName()));
            return;
        }

        if (user.getLastLocation().getWorldName().equalsIgnoreCase(player.getWorld().getName())) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "commands.world.back.already-there")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", player.getWorld().getName()));
            return;
        }
        final WorldUserTeleportWorldEvent event = new WorldUserTeleportWorldEvent(user, currentWorldHolder, nextWorldHolder);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        player.teleport(nextWorldHolder.getWorld().getSpawnLocation());
    }

}
