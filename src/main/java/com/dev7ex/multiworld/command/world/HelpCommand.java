package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.SubCommand;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;

import org.bukkit.command.CommandSender;

/**
 * @author Dev7ex
 * @since 18.05.2021
 */

public final class HelpCommand extends SubCommand {

    public HelpCommand(final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String[] arguments) {
        commandSender.sendMessage(" ");
        commandSender.sendMessage("§f§m------------------§r§r " + super.getPrefix() + "§f§m------------------");
        commandSender.sendMessage(" ");
        commandSender.sendMessage("§7- §7/multiworld §bcreate §7<Name> <WorldType>");
        commandSender.sendMessage("§7- §7/multiworld §bdelete §7<Name>");
        commandSender.sendMessage("§7- §7/multiworld §bhelp");
        commandSender.sendMessage("§7- §7/multiworld §binfo §7<World>");
        commandSender.sendMessage("§7- §7/multiworld §blist");
        commandSender.sendMessage("§7- §7/multiworld §bload §7<World>");
        commandSender.sendMessage("§7- §7/multiworld §boption §7<World> <Option> <Value>");
        commandSender.sendMessage("§7- §7/multiworld §bteleport §7<World>");
        commandSender.sendMessage("§7- §7/multiworld §bunload §7<World>");
        commandSender.sendMessage(" ");
        commandSender.sendMessage("§f§m------------------§r§r " + super.getPrefix() + "§f§m------------------");
        commandSender.sendMessage(" ");
        return true;
    }

}
