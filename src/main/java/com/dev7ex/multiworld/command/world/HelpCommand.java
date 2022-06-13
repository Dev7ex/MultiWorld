package com.dev7ex.multiworld.command.world;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;

import org.bukkit.command.CommandSender;

/**
 * @author Dev7ex
 * @since 18.05.2021
 */
public final class HelpCommand extends WorldSubCommand {

    public HelpCommand(final MultiWorldPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String[] arguments) {
        super.configuration.getStringList("messages.world.help").forEach(message ->
                commandSender.sendMessage(message.replaceAll("%prefix%", super.configuration.getPrefix())));
        return true;
    }

}
