package com.dev7ex.multiworld.command.world;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

/**
 *
 * @author Dev7ex
 * @since 20.05.2021
 *
 */

public final class LoadCommand extends WorldSubCommand implements TabCompleter {

    public LoadCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage("§cSyntax: /world load <World>");
    }

    @Override
    public final boolean execute(final CommandSender commandSender, final String[] arguments) {
        if(arguments.length != 2) {
            commandSender.sendMessage(super.getPrefix() + super.getUsage());
            return true;
        }
        final Player player = (Player) commandSender;

        if(Bukkit.getWorld(arguments[1]) != null) {
            commandSender.sendMessage(super.getPrefix() + "§7This world is loaded");
            player.spigot().sendMessage(this.getTeleportComponent(arguments[1]));
            return true;
        }

        if(!super.worldManager.getWorldConfiguration().isWorldRegistered(arguments[1])) {
            commandSender.sendMessage(super.getPrefix() + "§7This world doesn't exist!");
            return true;
        }
        super.worldManager.loadWorld(super.getWorldUser(player.getUniqueId()), arguments[1]);
        player.spigot().sendMessage(this.getTeleportComponent(arguments[1]));
        return true;
    }

    public final TextComponent getTeleportComponent(final String world) {
        final TextComponent textComponent = new TextComponent(super.getPrefix() + "§8[§aTeleport§8]");
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7Click to teleport")));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/world teleport " + world));
        return textComponent;
    }


    @Override
    public final List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        return  Lists.newArrayList(super.worldManager.getWorldProperties().keySet());
    }

}
