package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.java.reference.Reference;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;
import com.dev7ex.multiworld.event.world.WorldOptionUpdateEvent;
import com.dev7ex.multiworld.world.WorldOption;
import com.dev7ex.multiworld.world.WorldProperties;

import com.google.common.collect.Lists;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

/**
 * @author Dev7ex
 * @since 24.05.2021
 */
public final class OptionsCommand extends WorldSubCommand implements TabCompleter {

    public OptionsCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage(plugin.getConfiguration().getUsage().replaceAll("%command%", "/world options <World> <Option> <Value>"));
        super.setPermission("multiworld.command.world.options");
    }

    @Override
    public final boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!commandSender.hasPermission(super.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 4) {
            commandSender.sendMessage(super.getUsage());
            return true;
        }

        if (!super.worldManager.getWorldProperties().containsKey(arguments[1])) {
            commandSender.sendMessage(super.configuration.getWorldMessage("general.not-exists").replaceAll("%world%", arguments[1]));
            return true;
        }
        final Reference<WorldOption> optionReference = new Reference<>();

        try {
            optionReference.setValue(WorldOption.valueOf(arguments[2].toUpperCase()));

        } catch (final IllegalArgumentException exception) {
            commandSender.sendMessage(super.configuration.getWorldMessage("options.not-available"));
            return true;
        }

        final WorldProperties worldProperties = super.worldManager.getWorldProperties().get(arguments[1]);

        if (!optionReference.getValue().getValues().contains(arguments[3])) {
            commandSender.sendMessage(super.configuration.getWorldMessage("options.value-wrong").replaceAll("%value%", arguments[3]));
            return true;
        }
        commandSender.sendMessage(super.configuration.getWorldMessage("options.updating").replaceAll("%option%", optionReference.getValue().toString())
                .replaceAll("%value%", arguments[3]).replaceAll("%world%", arguments[1]));
        Bukkit.getPluginManager().callEvent(new WorldOptionUpdateEvent(worldProperties, optionReference.getValue(), arguments[3]));
        worldProperties.updateWorldOption(optionReference.getValue(), arguments[3]);
        super.worldManager.getWorldConfiguration().updateWorldOption(worldProperties.getWorldName(), optionReference.getValue(), arguments[3]);
        return true;
    }

    @Override
    public final List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        if (arguments.length == 2) {
            return Lists.newArrayList(super.worldManager.getWorldProperties().keySet());
        }
        if (arguments.length == 3) {
            return WorldOption.toStringList();
        }
        final Reference<WorldOption> optionReference = new Reference<>();

        try {
            optionReference.setValue(WorldOption.valueOf(arguments[2].toUpperCase()));
        } catch (final IllegalArgumentException ignored) {}

        if (optionReference.getValue() == null) {
            return null;
        }
        return Lists.newArrayList(optionReference.getValue().getValues());
    }

}
