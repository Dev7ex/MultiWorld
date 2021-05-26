package com.dev7ex.multiworld.command.world;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;
import com.dev7ex.multiworld.world.WorldProperties;

import com.google.common.collect.Lists;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

/**
 *
 * @author Dev7ex
 * @since 24.05.2021
 *
 */

public final class InfoCommand extends WorldSubCommand implements TabCompleter {

    public InfoCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage(plugin.getConfiguration().getUsage().replaceAll("%command%", "/world info <World>"));
        super.setPermission("multiworld.command.world.info");
    }

    @Override
    public final boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!commandSender.hasPermission(super.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 2) {
            commandSender.sendMessage(super.getUsage());
            return true;
        }

        if (!super.worldManager.getWorldProperties().containsKey(arguments[1])) {
            commandSender.sendMessage(super.configuration.getMessage("world-doesnt-exist").replaceAll("%world%", arguments[1]));
            return true;
        }
        final WorldProperties worldProperties = super.worldManager.getWorldProperties().get(arguments[1]);

        commandSender.sendMessage(" ");
        commandSender.sendMessage("§f§m------------------§r§r §b" + worldProperties.getWorldName() + " §f§m------------------");
        commandSender.sendMessage(" ");
        commandSender.sendMessage("§7- Creator: §b" + worldProperties.getWorldCreator());
        commandSender.sendMessage("§7- CreationTime: §b" + worldProperties.formatCreationDate());
        commandSender.sendMessage("§7- Loaded: §b" + (worldProperties.isLoaded() ? "true" : "false"));
        commandSender.sendMessage("§7- WorldType: §b" + worldProperties.getWorldType().toString());
        commandSender.sendMessage("§7- Environment: §b" + worldProperties.getWorldType().getEnvironment().toString());
        commandSender.sendMessage("§7- Difficulty: §b" + worldProperties.getDifficulty().toString());
        commandSender.sendMessage("§7- GameMode: §b" + worldProperties.getGameMode().toString());
        commandSender.sendMessage("§7- PvP: §b" + (worldProperties.isPvpEnabled() ? "true" : "false"));
        commandSender.sendMessage(" ");
        commandSender.sendMessage("§f§m------------------§r§r §b" + worldProperties.getWorldName() + " §f§m------------------");
        commandSender.sendMessage(" ");
        return true;
    }

    @Override
    public final List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        return Lists.newArrayList(super.worldManager.getWorldProperties().keySet());
    }

}
