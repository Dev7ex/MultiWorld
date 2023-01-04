package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;
import com.dev7ex.multiworld.user.WorldUser;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@CommandProperties(name = "back", permission = "multiworld.command.world.back")
public final class BackCommand extends WorldSubCommand {

    public BackCommand(final MultiWorldPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(super.getConfiguration().getMessage("only-player-command"));
            return true;
        }
        final Player player = (Player) commandSender;
        final WorldUser worldUser = super.getWorldUser(player.getUniqueId());

        if (arguments.length != 1) {
            commandSender.sendMessage(super.getConfiguration().getCommandUsage(this));
            return true;
        }

        if (worldUser.getProperties().getLastWorld() == null) {
            player.sendMessage(super.getConfiguration().getMessage("back.world-not-found"));
            return true;
        }
        final World world = Bukkit.getWorld(worldUser.getProperties().getLastWorld());

        if (world == null) {
            worldUser.getProperties().setLastWorld(null);
            player.sendMessage(super.getConfiguration().getMessage(super.getConfiguration().getMessage("back.world-not-found")));
            return true;
        }

        if (worldUser.getProperties().getLastWorld().equalsIgnoreCase(player.getWorld().getName())) {
            player.sendMessage(super.getConfiguration().getMessage("back.world-already-there"));
            return true;
        }
        final Location teleportLocation = (worldUser.getProperties().getLastWorldLocation() == null ? world.getSpawnLocation() : worldUser.getProperties().getLastWorldLocation());
        super.getWorldManager().teleportWorld(player, teleportLocation);
        return true;
    }

}
