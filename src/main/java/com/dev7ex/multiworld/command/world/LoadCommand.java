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
 * @author Dev7ex
 * @since 20.05.2021
 */

public final class LoadCommand extends WorldSubCommand implements TabCompleter {

    public LoadCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        super.setUsage(plugin.getConfiguration().getUsage().replaceAll("%command%", "/world load <World>"));
        super.setPermission("multiworld.command.world.load");
    }

    @Override
    public final boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!commandSender.hasPermission(this.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 2) {
            commandSender.sendMessage(super.getUsage());
            return true;
        }

        if (Bukkit.getWorld(arguments[1]) != null) {
            commandSender.sendMessage(this.configuration.getWorldMessage("loading.already-loaded").replaceAll("%world%", arguments[1]));
            return true;
        }

        if (!super.worldManager.getWorldConfiguration().isWorldRegistered(arguments[1])) {
            commandSender.sendMessage(super.configuration.getWorldMessage("general.not-exists").replaceAll("%world%", arguments[1]));
            return true;
        }
        super.worldManager.loadWorld(commandSender, arguments[1]);

        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            player.spigot().sendMessage(this.getTeleportComponent(player, arguments[1]));
        }
        return true;
    }

    public final TextComponent getTeleportComponent(final Player player, final String world) {
        final TextComponent textComponent = new TextComponent(super.configuration.getWorldMessage("teleport.component-message"));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(super.configuration.getWorldMessage("teleport.component-hover-text"))));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/world teleport " + player.getName() + " " + world));
        return textComponent;
    }

    @Override
    public final List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        return Lists.newArrayList(super.worldManager.getWorldProperties().keySet());
    }

}
