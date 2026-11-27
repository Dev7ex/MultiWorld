package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multicommon.api.util.Booleans;
import com.dev7ex.multicommon.api.util.Numbers;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.WorldRegistry;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a command for modifying game rules of registered worlds.
 *
 * <p>Provides game rule validation, type-safe value conversion
 * and context-sensitive argument completion.</p>
 *
 * @author Dev7ex
 * @since 29.03.2024
 */
@PaperCommandProperties(name = "gamerule", permission = "multiworld.command.world.gamerule")
public class GameRuleCommand extends PaperCommand implements ArgumentTabCompleter {


    private final WorldLocalizationRegistry localizationRegistry;
    private final WorldRegistry worldRegistry;

    public GameRuleCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.worldRegistry = plugin.getWorldRegistry();
    }

    /**
     * Executes the command and modifies the specified game rule
     * of the selected world.
     *
     * @param commandSender the sender executing the command
     * @param arguments     the provided command arguments
     */
    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length != 4) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_GAMERULE_USAGE));
            return;
        }

        final String worldName = this.resolveWorldName(commandSender, arguments[1].asString());
        final String gameRuleName = arguments[2].asString();
        final String value = arguments[3].asString();

        final WorldContext worldContext = this.worldRegistry.find(worldName).orElse(null);

        if (worldContext == null) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_EXISTS)
                    .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(worldName)));
            return;
        }

        final World world = worldContext.getWorld();

        if (!worldContext.isLoaded() || world == null) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_LOADED)
                    .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(worldName)));
            return;
        }

        final GameRule<?> gameRule = GameRule.getByName(gameRuleName);

        if (gameRule == null) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_GAMERULE_NOT_EXISTS)
                    .replaceText(builder -> builder.matchLiteral("%gamerule%").replacement(gameRuleName)));
            return;
        }

        if (!this.setGameRule(world, gameRule, value)) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_GAMERULE_INVALID_VALUE)
                    .replaceText(builder -> builder.matchLiteral("%gamerule%").replacement(gameRule.getName()))
                    .replaceText(builder -> builder.matchLiteral("%value%").replacement(value)));
            return;
        }

        commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_GAMERULE_SUCCESSFULLY_SET)
                .replaceText(builder -> builder.matchLiteral("%gamerule%").replacement(gameRule.getName()))
                .replaceText(builder -> builder.matchLiteral("%value%").replacement(value)));
    }

    /**
     * Returns context-sensitive completions for world names,
     * game rule names and game rule values.
     *
     * @param commandSender the sender requesting completions
     * @param arguments     the current command arguments
     * @return the available argument completions
     */
    @Override
    public @NotNull List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        return switch (arguments.length) {
            case 2 -> this.worldRegistry.getLoadedWorlds()
                    .stream()
                    .map(WorldContext::getName)
                    .toList();

            case 3 -> Arrays.stream(GameRule.values())
                    .map(GameRule::getName)
                    .toList();

            case 4 -> this.getGameRuleValues(
                    this.resolveWorldName(commandSender, arguments[1].asString()),
                    arguments[2].asString());

            default -> List.of();
        };
    }

    /**
     * Resolves the specified world name by replacing the creator
     * placeholder with the name of the command sender.
     *
     * @param commandSender the sender executing the command
     * @param worldName     the original world name
     * @return the resolved world name
     */
    private @NotNull String resolveWorldName(@NotNull final CommandSender commandSender, @NotNull final String worldName) {
        return worldName.equalsIgnoreCase("%creator_name%")
                ? commandSender.getName()
                : worldName;
    }

    /**
     * Converts and applies the specified value according to the
     * type of the given game rule.
     *
     * @param world    the world to modify
     * @param gameRule the game rule to modify
     * @param value    the value to convert and apply
     * @param <T>      the game rule value type
     * @return whether the game rule was successfully modified
     */
    private <T> boolean setGameRule(@NotNull final World world, @NotNull final GameRule<T> gameRule, @NotNull final String value) {
        final Class<T> type = gameRule.getType();
        final Object parsedValue;

        if (type == Integer.class && Numbers.isInteger(value)) {
            parsedValue = Integer.parseInt(value);

        } else if (type == Boolean.class && Booleans.isBoolean(value)) {
            parsedValue = Boolean.parseBoolean(value);

        } else {
            return false;
        }

        return world.setGameRule(gameRule, type.cast(parsedValue));
    }

    /**
     * Returns the available value completions for the specified
     * game rule within the selected world.
     *
     * <p>Boolean game rules provide both possible values, whereas
     * integer game rules provide their current world-specific value.</p>
     *
     * @param worldName    the name of the selected world
     * @param gameRuleName the name of the selected game rule
     * @return the available game rule value completions
     */
    private @NotNull List<String> getGameRuleValues(@NotNull final String worldName, @NotNull final String gameRuleName) {
        final WorldContext worldContext = this.worldRegistry.find(worldName).orElse(null);

        if (worldContext == null || !worldContext.isLoaded()) {
            return List.of();
        }

        final World world = worldContext.getWorld();
        final GameRule<?> gameRule = GameRule.getByName(gameRuleName);

        if (world == null || gameRule == null) {
            return List.of();
        }

        if (gameRule.getType() == Boolean.class) {
            return List.of("true", "false");
        }

        final Object value = world.getGameRuleValue(gameRule);

        return value != null ? List.of(String.valueOf(value)) : List.of();
    }

}
