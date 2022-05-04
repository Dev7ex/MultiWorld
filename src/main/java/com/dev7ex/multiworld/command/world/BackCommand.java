package com.dev7ex.multiworld.command.world;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;
import com.dev7ex.multiworld.user.WorldUser;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
public final class BackCommand extends WorldSubCommand {

    public BackCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage(plugin.getConfiguration().getUsage().replaceAll("%command%", "back"));
        super.setPermission("multiworld.command.world.back");
    }

    @Override
    public final boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(super.configuration.getOnlyPlayerCommandMessage());
            return true;
        }
        final Player player = (Player) commandSender;
        final WorldUser worldUser = super.getWorldUser(player.getUniqueId());

        if (!player.hasPermission(super.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 1) {
            commandSender.sendMessage(super.getUsage());
            return true;
        }

        if (worldUser.getProperties().getLastWorld() == null) {
            player.sendMessage(this.configuration.getWorldMessage("back.world-not-found"));
            return true;
        }
        final World world = Bukkit.getWorld(worldUser.getProperties().getLastWorld());

        if (world == null) {
            worldUser.getProperties().setLastWorld(null);
            player.sendMessage(this.configuration.getWorldMessage(this.configuration.getMessage("back.world-not-found")));
            return true;
        }

        if (worldUser.getProperties().getLastWorld().equalsIgnoreCase(player.getWorld().getName())) {
            player.sendMessage(this.configuration.getWorldMessage("back.world-already-there"));
            return true;
        }
        final Location teleportLocation = (worldUser.getProperties().getLastWorldLocation() == null ? world.getSpawnLocation() : worldUser.getProperties().getLastWorldLocation());
        super.worldManager.teleportWorld(player, teleportLocation);
        return true;
    }

}
