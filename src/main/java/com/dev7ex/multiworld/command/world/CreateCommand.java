package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.java.reference.Reference;

import com.dev7ex.multiworld.MultiWorldPlugin;

import com.dev7ex.multiworld.command.WorldSubCommand;
import com.dev7ex.multiworld.world.WorldType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */

public final class CreateCommand extends WorldSubCommand implements TabCompleter {

    public CreateCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage(plugin.getConfiguration().getUsage().replaceAll("%command%", "/world create <Name> <Type>"));
        super.setPermission("multiworld.command.world.create");
    }

    @Override
    public final boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!commandSender.hasPermission(this.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 3) {
            commandSender.sendMessage(super.getUsage());
            return true;
        }

        if (super.worldManager.getWorldProperties().containsKey(arguments[1])) {
            commandSender.sendMessage(super.configuration.getWorldMessage("general.already-exists").replaceAll("%world%", arguments[1]));
            return true;
        }

        final Reference<WorldType> worldTypeReference = new Reference<>();

        try {
            worldTypeReference.setValue(WorldType.valueOf(arguments[2].toUpperCase()));

        } catch (final IllegalArgumentException exception) {
            commandSender.sendMessage(super.configuration.getWorldMessage("general.type-not-available"));
            return true;
        }

        if (super.worldManager.isServerCreatingWorld()) {
            commandSender.sendMessage(super.configuration.getWorldMessage("general.waiting"));
            return true;
        }
        super.worldManager.createWorld(commandSender, arguments[1], worldTypeReference.getValue());
        return true;
    }

    @Override
    public final List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        return (arguments.length == 3 ? WorldType.toStringList() : null);
    }

}
