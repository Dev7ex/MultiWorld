package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.CommandProperties;
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
 * @author Dev7ex
 * @since 20.05.2021
 */
@CommandProperties(name = "load", permission = "multiworld.command.world.load")
public final class LoadCommand extends WorldSubCommand implements TabCompleter {

    public LoadCommand(final MultiWorldPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!commandSender.hasPermission(this.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 2) {
            commandSender.sendMessage(super.getConfiguration().getCommandUsage(this));
            return true;
        }

        if (Bukkit.getWorld(arguments[1]) != null) {
            commandSender.sendMessage(super.getConfiguration().getMessage("load.already-loaded").replaceAll("%world%", arguments[1]));
            return true;
        }

        if (!super.getWorldManager().getWorldConfiguration().isWorldRegistered(arguments[1])) {
            commandSender.sendMessage(super.getConfiguration().getMessage("general.not-exists").replaceAll("%world%", arguments[1]));
            return true;
        }
        super.getWorldManager().loadWorld(commandSender, arguments[1]);

        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            player.spigot().sendMessage(this.getTeleportComponent(player, arguments[1]));
        }
        return true;
    }

    public TextComponent getTeleportComponent(final Player player, final String world) {
        final TextComponent textComponent = new TextComponent(super.getConfiguration().getMessage("teleport.component-message"));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(super.getConfiguration().getMessage("teleport.component-hover-text"))));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/world teleport " + player.getName() + " " + world));
        return textComponent;
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        return Lists.newArrayList(super.getWorldManager().getWorldProperties().keySet());
    }

}
