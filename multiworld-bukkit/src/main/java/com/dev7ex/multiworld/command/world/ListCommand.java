package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@CommandProperties(name = "list", permission = "multiworld.command.world.list")
public class ListCommand extends BukkitCommand {

    public ListCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 1) {
            return true;
        }

        final StringBuilder stringBuilder = new StringBuilder();
        final Set<String> worldEntries = MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders().keySet();

        for (final String worldEntry : worldEntries) {
            final BukkitWorldHolder worldHolder = MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(worldEntry).orElseThrow();

            if (stringBuilder.length() > 0) {
                stringBuilder.append(ChatColor.GRAY);
                stringBuilder.append(", ");
            }
            stringBuilder.append(worldHolder.isLoaded() ? ChatColor.GREEN : ChatColor.RED).append(worldEntry);
        }
        commandSender.sendMessage(super.getConfiguration().getString("messages.commands.list.message")
                .replaceAll("%prefix%", super.getPrefix())
                .replaceAll("%world_names%", stringBuilder.toString()));
        return true;

    }

}
