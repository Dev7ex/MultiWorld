package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;
import com.dev7ex.multiworld.world.WorldOption;
import com.dev7ex.multiworld.world.WorldProperties;

import com.dev7ex.multiworld.world.WorldType;
import com.google.common.collect.Lists;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 24.05.2021
 */
@CommandProperties(name = "options", permission = "multiworld.command.world.options")
public final class OptionsCommand extends WorldSubCommand implements TabCompleter {

    public OptionsCommand(final MultiWorldPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!commandSender.hasPermission(super.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 4) {
            commandSender.sendMessage(super.getConfiguration().getCommandUsage(this));
            return true;
        }

        if (!super.getWorldManager().getWorldProperties().containsKey(arguments[1])) {
            commandSender.sendMessage(super.getConfiguration().getMessage("general.not-exists").replaceAll("%world%", arguments[1]));
            return true;
        }
        final Optional<WorldOption> optional = WorldOption.fromString(arguments[2].toUpperCase());

        if (optional.isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getMessage("options.not-available"));
            return true;
        }
        final WorldProperties worldProperties = super.getWorldManager().getWorldProperties().get(arguments[1]);
        final WorldOption currentOption = optional.get();

        if (!optional.get().getValues().contains(arguments[3])) {
            commandSender.sendMessage(super.getConfiguration().getMessage("options.value-wrong").replaceAll("%value%", arguments[3]));
            return true;
        }
        commandSender.sendMessage(super.getConfiguration().getMessage("options.updating").replaceAll("%option%", currentOption.toString())
                .replaceAll("%value%", arguments[3]).replaceAll("%world%", arguments[1]));
        worldProperties.updateWorldOption(currentOption, arguments[3]);
        super.getWorldManager().getWorldConfiguration().updateWorldOption(worldProperties.getWorldName(), currentOption, arguments[3]);
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        if (arguments.length == 2) {
            return Lists.newArrayList(super.getWorldManager().getWorldProperties().keySet());
        }
        if (arguments.length == 3) {
            return WorldOption.toStringList();
        }
        final Optional<WorldOption> optional = WorldOption.fromString(arguments[2].toUpperCase());

        if ((optional.isEmpty())) {
            return null;
        }
        return Lists.newArrayList(optional.get().getValues());
    }

}
