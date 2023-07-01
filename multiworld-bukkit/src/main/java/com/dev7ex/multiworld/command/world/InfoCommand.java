package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Dev7ex
 * @since 24.05.2021
 */
@CommandProperties(name = "info", permission = "multiworld.command.world.info")
public class InfoCommand extends BukkitCommand implements TabCompleter {

    public InfoCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 2) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.info.usage")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-not-exists")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return true;
        }
        final BukkitWorldHolder worldHolder = MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).get();

        // TODO: 29.06.2023 NullPointerException if world not loaded [55]

        super.getConfiguration().getStringList("messages.commands.info.message").forEach(message -> {
            commandSender.sendMessage(message
                    .replaceAll("%world_name%", worldHolder.getName())
                    .replaceAll("%world_creator_name%", worldHolder.getCreatorName())
                    .replaceAll("%creation_timestamp%", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(worldHolder.getCreationTimeStamp())))
                    .replaceAll("%loaded%", (worldHolder.isLoaded() ? "true" : "false"))
                    .replaceAll("%world_type%", worldHolder.getType().toString())
                    .replaceAll("%environment%", worldHolder.getWorld().getEnvironment().toString())
                    .replaceAll("%difficulty%", worldHolder.getDifficulty().toString())
                    .replaceAll("%gamemode%", worldHolder.getGameMode().toString())
                    .replaceAll("%pvp_enabled%", (worldHolder.isPvpEnabled() ? "true" : "false"))
                    .replaceAll("%whitelist_enabled%", (worldHolder.isWhitelistEnabled() ? "true" : "false")));
        });
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final Command command,
                                      @NotNull final String commandLabel, @NotNull final String[] arguments) {
        return new ArrayList<>(MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders().keySet());
    }

}
