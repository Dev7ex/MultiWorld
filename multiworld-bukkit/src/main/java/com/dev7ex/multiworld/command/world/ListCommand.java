package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@BukkitCommandProperties(name = "list", permission = "multiworld.command.world.list")
public class ListCommand extends BukkitCommand {

    public ListCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        final DefaultTranslationProvider translationProvider = MultiWorldPlugin.getInstance().getTranslationProvider();

        if (arguments.length != 1) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "commands.world.list.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }

        final StringBuilder stringBuilder = new StringBuilder();
        final Set<String> worldEntries = MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders().keySet();

        for (final String worldEntry : worldEntries) {
            final BukkitWorldHolder worldHolder = MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(worldEntry).orElseThrow();

            if (!stringBuilder.isEmpty()) {
                stringBuilder.append(ChatColor.GRAY);
                stringBuilder.append(", ");
            }
            stringBuilder.append(worldHolder.isLoaded() ? ChatColor.GREEN : ChatColor.RED).append(worldEntry);
        }
        commandSender.sendMessage(translationProvider.getMessage(commandSender, "commands.world.list.message")
                .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                .replaceAll("%world_names%", stringBuilder.toString()));
    }

}
