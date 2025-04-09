package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.common.util.Booleans;
import com.dev7ex.common.util.Numbers;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.event.world.WorldGameRuleChangeEvent;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Dev7ex
 * @since 29.03.2024
 */
@BukkitCommandProperties(name = "gamerule", permission = "multiworld.command.world.gamerule")
public class GameRuleCommand extends BukkitCommand implements BukkitTabCompleter {

    private final DefaultTranslationProvider translationProvider;
    private final DefaultWorldProvider worldProvider;

    public GameRuleCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.translationProvider = plugin.getTranslationProvider();
        this.worldProvider = plugin.getWorldProvider();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 4) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.gamerule.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }

        if (arguments[1].equalsIgnoreCase("%creator_name%")) {
            arguments[1] = arguments[1].replaceAll("%creator_name%", commandSender.getName());
        }

        if (this.worldProvider.getWorldHolder(arguments[1]).isEmpty()) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "general.world.not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        final GameRule<?> gameRule = GameRule.getByName(arguments[2]);

        if (gameRule == null) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.gamerule.not-existing")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%gamerule_name%", arguments[2]));
            return;
        }
        final BukkitWorldHolder worldHolder = this.worldProvider.getWorldHolder(arguments[1])
                .orElseThrow();
        final WorldGameRuleChangeEvent event = new WorldGameRuleChangeEvent(worldHolder, commandSender, gameRule, arguments[3]);

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        if ((Numbers.isInteger(arguments[3])) && (gameRule.getType() == Integer.class)) {
            final GameRule<Integer> currentGameRule = (GameRule<Integer>) gameRule;
            worldHolder.getWorld().setGameRule(currentGameRule, Integer.parseInt(arguments[3]));
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.gamerule.successfully-set")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%gamerule_name%", gameRule.getName())
                    .replaceAll("%value%", arguments[3])
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }

        if ((Booleans.isBoolean(arguments[3])) && (gameRule.getType() == Boolean.class)) {
            final GameRule<Boolean> currentGameRule = (GameRule<Boolean>) gameRule;
            worldHolder.getWorld().setGameRule(currentGameRule, Boolean.parseBoolean(arguments[3]));
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.gamerule.successfully-set")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%gamerule_name%", gameRule.getName())
                    .replaceAll("%value%", arguments[3])
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.gamerule.invalid-value")
                .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                .replaceAll("%gamerule_name%", arguments[2])
                .replaceAll("%value%", arguments[3]));
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length == 2) {
            return Lists.newArrayList(this.worldProvider.getWorldHolders().keySet());
        }

        if (arguments.length == 3) {
            return Arrays.stream(GameRule.values())
                    .map(GameRule::getName)
                    .toList();
        }

        if (this.worldProvider.getWorldHolder(arguments[1]).isEmpty()) {
            return Collections.emptyList();
        }
        final BukkitWorldHolder worldHolder = this.worldProvider.getWorldHolder(arguments[1])
                .get();
        final GameRule<?> gameRule = GameRule.getByName(arguments[2]);

        if (gameRule == null) {
            return Collections.emptyList();
        }

        if (gameRule.getType() == Boolean.class) {
            return List.of("true", "false");
        }
        return List.of(String.valueOf(worldHolder.getWorld().getGameRuleValue(gameRule)));
    }


}
