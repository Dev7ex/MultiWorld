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
 * @author Dev7ex
 * @since 24.05.2021
 */
public final class InfoCommand extends WorldSubCommand implements TabCompleter {

    public InfoCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage(plugin.getConfiguration().getUsage().replaceAll("%command%", "/world info <World>"));
        super.setPermission("multiworld.command.world.info");
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String[] arguments) {
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

        super.configuration.getStringList("messages.world.info").forEach(message ->
                commandSender.sendMessage(message.replaceAll("%world%", arguments[1]).replaceAll("%worldCreator%", worldProperties.getWorldCreator())
                        .replaceAll("%creationDate%", worldProperties.formatCreationDate(worldProperties.getCreationTime()))
                        .replaceAll("%loaded%", worldProperties.isLoaded() ? "true" : "false")
                        .replaceAll("%worldType%", worldProperties.getWorldType().toString())
                        .replaceAll("%environment%", worldProperties.getWorldType().getEnvironment().toString())
                        .replaceAll("%difficulty%", worldProperties.getDifficulty().toString())
                        .replaceAll("%gameMode%", worldProperties.getGameMode().toString())
                        .replaceAll("%pvpEnabled%", worldProperties.isPvpEnabled() ? "true" : "false")));
        return true;
    }

    @Override
    public final List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        return Lists.newArrayList(super.worldManager.getWorldProperties().keySet());
    }

}
