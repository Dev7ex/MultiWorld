package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.java.reference.Reference;
import com.dev7ex.multiworld.MultiWorldPlugin;

import com.dev7ex.multiworld.command.WorldSubCommand;
import com.dev7ex.multiworld.user.WorldUser;
import com.dev7ex.multiworld.world.WorldType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

/**
 *
 * @author Dev7ex
 * @since 20.05.2021
 *
 */

public final class CreateCommand extends WorldSubCommand implements TabCompleter {

    public CreateCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage("§cSyntax: /world create <Name> <Type>");
    }

    @Override
    public final boolean execute(final CommandSender commandSender, final String[] arguments) {
        final Player player = (Player) commandSender;
        final WorldUser worldUser = super.getWorldUser(player.getUniqueId());

        if(arguments.length != 3) {
            player.sendMessage(super.getPrefix() + super.getUsage());
            return true;
        }

        if(super.worldManager.getWorldProperties().containsKey(arguments[1])) {
            player.sendMessage(super.getPrefix() + "§7This world already exists!");
            return true;
        }

        final Reference<WorldType> worldTypeReference = new Reference<>();

        try {
            worldTypeReference.setValue(WorldType.valueOf(arguments[2].toUpperCase()));

        } catch (final IllegalArgumentException exception) {
            player.sendMessage(super.getPrefix() + "§7This world type does not exist");
            return true;
        }

        if(super.worldManager.isServerCreatingWorld()) {
            player.sendMessage(super.getPrefix() + "§cPlease wait a moment...");
            return true;
        }

        super.worldManager.createWorld(worldUser, arguments[1], worldTypeReference.getValue());
        return true;
    }

    @Override
    public final List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        if(arguments.length == 3) {
            return WorldType.toStringList();
        }
        return null;
    }

}
