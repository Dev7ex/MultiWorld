package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.CommandProperties;
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
@CommandProperties(name = "list", permission = "multiworld.command.world.list")
public final class ListCommand extends WorldSubCommand {

    public ListCommand(final MultiWorldPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!commandSender.hasPermission(this.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 1) {
            commandSender.sendMessage(super.getConfiguration().getCommandUsage(this));
            return true;
        }

        final StringBuilder stringBuilder = new StringBuilder();
        final Set<String> worldSet = super.getWorldManager().getWorldProperties().keySet();

        for (final String world : worldSet) {
            final WorldProperties worldProperties = super.getWorldManager().getWorldProperties().get(world);

            if (stringBuilder.length() > 0) {
                stringBuilder.append(ChatColor.WHITE);
                stringBuilder.append(", ");
            }
            stringBuilder.append(worldProperties.isLoaded() ? ChatColor.GREEN : ChatColor.RED).append(world);
        }
        commandSender.sendMessage(super.getConfiguration().getMessage("list.message").replaceAll("%worlds%", stringBuilder.toString()));
        return true;
    }

}
