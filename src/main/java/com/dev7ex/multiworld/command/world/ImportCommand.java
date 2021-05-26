package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.java.reference.Reference;
import com.dev7ex.common.java.util.FileUtil;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;

import com.dev7ex.multiworld.world.WorldType;

import com.google.common.collect.Lists;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.io.File;
import java.util.List;

/**
 *
 * @author Dev7ex
 * @since 25.05.2021
 *
 */

public final class ImportCommand extends WorldSubCommand implements TabCompleter {

    public ImportCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage(plugin.getConfiguration().getUsage().replaceAll("%command%", "/world import <Name> <WorldType>"));
        super.setPermission("multiworld.command.world.import");
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

        final File worldFolder = new File(Bukkit.getWorldContainer(), arguments[1]);

        if(!FileUtil.containsFile(worldFolder, "level.dat")) {
            commandSender.sendMessage(super.configuration.getWorldMessage("general.folder-not-exists").replaceAll("%folder%", arguments[1]));
            return true;
        }

        if(super.worldManager.getWorldProperties().containsKey(arguments[1])) {
            commandSender.sendMessage(this.configuration.getWorldMessage("import.already-imported").replaceAll("%world%", arguments[1]));
            return true;
        }

        final Reference<WorldType> worldTypeReference = new Reference<>();

        try {
            worldTypeReference.setValue(WorldType.valueOf(arguments[2].toUpperCase()));

        } catch (final IllegalArgumentException exception) {
            commandSender.sendMessage(super.configuration.getWorldMessage("general.type-not-available"));
            return true;
        }
        super.worldManager.importWorld(commandSender, arguments[1], worldTypeReference.getValue());
        return true;
    }

    @Override
    public final List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        if((arguments.length < 2) || (arguments.length > 3)) {
            return null;
        }

        if(arguments.length == 3) {
            return WorldType.toStringList();
        }
        final List<String> files = FileUtil.foldersToStringList(Bukkit.getWorldContainer());
        final List<String> completions = Lists.newArrayList(files);

        for(final File folder : Bukkit.getWorldContainer().listFiles()) {
            if(FileUtil.containsFile(folder, "level.dat")) {
                continue;
            }
            completions.remove(folder.getName());
        }
        return completions;
    }

}
