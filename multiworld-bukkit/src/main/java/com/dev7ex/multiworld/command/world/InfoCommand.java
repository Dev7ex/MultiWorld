package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.api.world.WorldEnvironment;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Dev7ex
 * @since 24.05.2021
 */
@BukkitCommandProperties(name = "info", permission = "multiworld.command.world.info")
public class InfoCommand extends BukkitCommand implements BukkitTabCompleter {

    public InfoCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        final DefaultTranslationProvider translationProvider = MultiWorldPlugin.getInstance().getTranslationProvider();

        if (arguments.length != 2) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "messages.commands.world.info.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }

        if (arguments[1].equalsIgnoreCase("%creator_name%")) {
            arguments[1] = arguments[1].replaceAll("%creator_name%", commandSender.getName());
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).isEmpty()) {
            commandSender.sendMessage(translationProvider.getMessage(commandSender, "messages.general.world-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        final BukkitWorldHolder worldHolder = MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).get();
        final MultiWorldConfiguration configuration = MultiWorldPlugin.getInstance().getConfiguration();

        translationProvider.getMessageList(commandSender, "commands.world.info.message").forEach(message -> {
            commandSender.sendMessage(message
                    .replaceAll("%world_name%", worldHolder.getName())
                    .replaceAll("%world_creator_name%", worldHolder.getCreatorName())
                    .replaceAll("%creation_timestamp%", configuration.getTimeFormat().format(new Date(worldHolder.getCreationTimeStamp())))
                    .replaceAll("%load_auto%", String.valueOf(worldHolder.isAutoLoadEnabled()))
                    .replaceAll("%loaded%", (worldHolder.isLoaded() ? "true" : "false"))
                    .replaceAll("%world_type%", worldHolder.getType().toString())
                    .replaceAll("%environment%", WorldEnvironment.fromType(worldHolder.getType()).name())
                    .replaceAll("%difficulty%", worldHolder.getDifficulty().toString())
                    .replaceAll("%gamemode%", worldHolder.getGameMode().toString())
                    .replaceAll("%pvp_enabled%", (worldHolder.isPvpEnabled() ? "true" : "false"))
                    .replaceAll("%spawn_animals%", (worldHolder.isSpawnAnimals() ? "true" : "false"))
                    .replaceAll("%spawn_monsters%", (worldHolder.isSpawnMonsters() ? "true" : "false"))
                    .replaceAll("%spawn_entities%", (worldHolder.isSpawnEntities() ? "true" : "false"))
                    .replaceAll("%end-portal-accessible%", (worldHolder.isEndPortalAccessible() ? "true" : "false"))
                    .replaceAll("%nether-portal-accessible%", (worldHolder.isNetherPortalAccessible() ? "true" : "false"))
                    .replaceAll("%whitelist_enabled%", (worldHolder.isWhitelistEnabled() ? "true" : "false"))
                    .replaceAll("%normal_world%", worldHolder.getNormalWorldName() == null ? "" : worldHolder.getNormalWorldName())
                    .replaceAll("%nether_world%", worldHolder.getNetherWorldName() == null ? "" : worldHolder.getNetherWorldName())
                    .replaceAll("%end_world%", worldHolder.getEndWorldName() == null ? "" : worldHolder.getEndWorldName())
                    .replaceAll("%receive_achievements%", (worldHolder.isReceiveAchievements() ? "true" : "false")));
        });
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        return new ArrayList<>(MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders().keySet());
    }

}
