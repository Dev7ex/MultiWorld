package com.dev7ex.multiworld.command.world;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;

import com.dev7ex.multiworld.world.WorldProperties;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Set;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
public final class ListCommand extends WorldSubCommand {

    public ListCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage(plugin.getConfiguration().getUsage().replaceAll("%command%", "/world list"));
        super.setPermission("multiworld.command.world.list");
    }

    @Override
    public final boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!commandSender.hasPermission(this.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 1) {
            commandSender.sendMessage(super.getUsage());
            return true;
        }

        final StringBuilder stringBuilder = new StringBuilder();
        final Set<String> worldSet = super.worldManager.getWorldProperties().keySet();

        for (final String world : worldSet) {
            final WorldProperties worldProperties = super.worldManager.getWorldProperties().get(world);

            if (stringBuilder.length() > 0) {
                stringBuilder.append(ChatColor.WHITE);
                stringBuilder.append(", ");
            }
            stringBuilder.append(worldProperties.isLoaded() ? ChatColor.GREEN : ChatColor.RED).append(world);
        }
        commandSender.sendMessage(super.configuration.getWorldMessage("list.message").replaceAll("%worlds%", stringBuilder.toString()));
        return true;
    }

}
