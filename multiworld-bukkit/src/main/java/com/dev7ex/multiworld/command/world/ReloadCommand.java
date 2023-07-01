package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 28.12.2022
 */
@CommandProperties(name = "reload", permission = "multiworld.command.world.reload")
public class ReloadCommand extends BukkitCommand {

    public ReloadCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 1) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.reload.usage")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }
        super.getConfiguration().load();
        commandSender.sendMessage(super.getConfiguration().getString("messages.commands.reload.message")
                .replaceAll("%prefix%", super.getPrefix()));
        return true;
    }

}
