package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import com.dev7ex.multiworld.util.Colored;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Dev7ex
 * @since 24.05.2021
 */
@BukkitCommandProperties(name = "info", permission = "multiworld.command.world.info")
public class InfoCommand extends BukkitCommand implements BukkitTabCompleter {

    private final DefaultTranslationProvider translationProvider;
    private final DefaultWorldProvider worldProvider;

    public InfoCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.translationProvider = plugin.getTranslationProvider();
        this.worldProvider = plugin.getWorldProvider();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 2) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.info.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }

        if (arguments[1].equalsIgnoreCase("%creator_name%")) {
            arguments[1] = arguments[1].replaceAll("%creator_name%", commandSender.getName());
        }

        if (this.worldProvider.getWorldHolder(arguments[1]).isEmpty()) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "messages.general.world-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        final BukkitWorldHolder worldHolder = this.worldProvider.getWorldHolder(arguments[1])
                .get();

        this.translationProvider.getMessageList(commandSender, "commands.world.info.message")
                .forEach(message -> commandSender.sendMessage(this.getReplacedInfoMessage(worldHolder, message)));
    }

    public String getReplacedInfoMessage(@NotNull final BukkitWorldHolder worldHolder, @NotNull final String message) {
        final Map<String, String> replacements = new HashMap<>();

        // Populate the replacements map with keys as placeholders and values from worldHolder
        replacements.put("%world_name%", worldHolder.getName());
        replacements.put("%world_creator_name%", worldHolder.getCreatorName());
        replacements.put("%creation_timestamp%",
                MultiWorldPlugin.getInstance().getConfiguration().getTimeFormat().format(new Date(worldHolder.getCreationTimeStamp())));
        replacements.put("%auto_load_enabled%", Colored.getColoredBoolean(worldHolder.isAutoLoadEnabled()));
        replacements.put("%auto_unload_enabled%", Colored.getColoredBoolean(worldHolder.isAutoUnloadEnabled()));
        replacements.put("%difficulty%", Colored.getColoredDifficulty(worldHolder.getDifficulty()));
        replacements.put("%end-portal-accessible%", Colored.getColoredBoolean(worldHolder.isEndPortalAccessible()));
        replacements.put("%end_world%", worldHolder.getEndWorldName());
        replacements.put("%game-mode%", Colored.getColoredGameMode(worldHolder.getGameMode()));
        replacements.put("%force-game-mode%", Colored.getColoredBooleanWithOption(worldHolder.getForceGameMode()));
        replacements.put("%hunger_enabled%", Colored.getColoredBoolean(worldHolder.isHungerEnabled()));
        replacements.put("%keep_spawn_in_memory%", Colored.getColoredBoolean(worldHolder.isKeepSpawnInMemory()));
        replacements.put("%nether-portal-accessible%", Colored.getColoredBoolean(worldHolder.isNetherPortalAccessible()));
        replacements.put("%nether_world%", worldHolder.getNetherWorldName());
        replacements.put("%normal_world%", worldHolder.getNormalWorldName());
        replacements.put("%pvp_enabled%", Colored.getColoredBoolean(worldHolder.isPvpEnabled()));
        replacements.put("%receive_achievements%", Colored.getColoredBoolean(worldHolder.isReceiveAchievements()));
        replacements.put("%redstone_enabled%", Colored.getColoredBoolean(worldHolder.isRedstoneEnabled()));
        replacements.put("%spawn_animals%", Colored.getColoredBoolean(worldHolder.isSpawnAnimals()));
        replacements.put("%spawn_entities%", Colored.getColoredBoolean(worldHolder.isSpawnEntities()));
        replacements.put("%spawn_monsters%", Colored.getColoredBoolean(worldHolder.isSpawnMonsters()));
        replacements.put("%weather_enabled%", Colored.getColoredBoolean(worldHolder.isWeatherEnabled()));
        replacements.put("%whitelist_enabled%", Colored.getColoredBoolean(worldHolder.isWhitelistEnabled()));
        replacements.put("%environment%", worldHolder.getEnvironment().name());
        replacements.put("%generator_name%", worldHolder.getGenerator());
        replacements.put("%world_type%", worldHolder.getType().toString());

        final StringBuilder replacedMessage = new StringBuilder(message);

        // Replace each placeholder with its corresponding value
        replacements.forEach((key, value) -> {
            int startIndex = 0;
            // Continue replacing until no more instances are found
            while ((startIndex = replacedMessage.indexOf(key, startIndex)) != -1) {
                replacedMessage.replace(startIndex, startIndex + key.length(), value);
                startIndex += value.length(); // Move past the last replacement
            }
        });
        return replacedMessage.toString();
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        return new ArrayList<>(this.worldProvider.getWorldHolders().keySet());
    }

}
