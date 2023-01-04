package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.util.NumberUtil;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;
import com.dev7ex.multiworld.world.WorldType;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@CommandProperties(name = "create", permission = "multiworld.command.world.create")
public final class CreateCommand extends WorldSubCommand implements TabCompleter {

    public CreateCommand(final MultiWorldPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!commandSender.hasPermission(this.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 3) {
            commandSender.sendMessage(super.getConfiguration().getCommandUsage(this));
            return true;
        }

        if (super.getWorldManager().getWorldProperties().containsKey(arguments[1])) {
            commandSender.sendMessage(super.getConfiguration().getMessage("general.already-exists").replaceAll("%world%", arguments[1]));
            return true;
        }

        if (NumberUtil.isNumber(arguments[2])) {
            super.getWorldManager().createWorld(commandSender, arguments[1], Long.parseLong(arguments[2]));
            return true;
        }

        final Optional<WorldType> optional = WorldType.fromString(arguments[2].toUpperCase());

        if ((optional.isEmpty())) {
            commandSender.sendMessage(super.getConfiguration().getMessage("general.invalid-input"));
            return true;
        }
        super.getWorldManager().createWorld(commandSender, arguments[1], optional.get());
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        return (arguments.length == 3 ? WorldType.toStringList() : Collections.emptyList());
    }

}
