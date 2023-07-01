package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.BukkitCommon;
import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.user.WorldUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@CommandProperties(name = "help", permission = "multiworld.command.world")
public final class HelpCommand extends BukkitCommand {

    public HelpCommand(final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String[] arguments) {
        super.getConfiguration().getStringList("messages.commands.help.message").forEach(message -> {
            commandSender.sendMessage(message.replaceAll("%prefix%", super.getPrefix()));
        });
        return true;
    }

}
