package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.common.util.Numbers;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.world.WorldEnvironment;
import com.dev7ex.multiworld.api.world.WorldType;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import com.dev7ex.multiworld.world.DefaultWorldManager;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import com.dev7ex.multiworld.world.generator.DefaultWorldGeneratorProvider;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@BukkitCommandProperties(name = "create", permission = "multiworld.command.world.create")
public class CreateCommand extends BukkitCommand implements BukkitTabCompleter {

    private final DefaultTranslationProvider translationProvider;
    private final DefaultWorldManager worldManager;
    private final DefaultWorldProvider worldProvider;
    private final DefaultWorldGeneratorProvider worldGeneratorProvider;

    public CreateCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.translationProvider = plugin.getTranslationProvider();
        this.worldManager = plugin.getWorldManager();
        this.worldProvider = plugin.getWorldProvider();
        this.worldGeneratorProvider = plugin.getWorldGeneratorProvider();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 5) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.create.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }

        if (arguments[1].equalsIgnoreCase("%creator_name%")) {
            arguments[1] = arguments[1].replaceAll("%creator_name%", commandSender.getName());
        }

        if (this.worldProvider.isRegistered(arguments[1])) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender,"general.world.already-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        final Optional<WorldEnvironment> environmentOptional = WorldEnvironment.fromString(arguments[2].toUpperCase());

        if (environmentOptional.isEmpty()) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender,"general.world.environment-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%environment_name%", arguments[2]));
            return;
        }
        final WorldEnvironment environment = environmentOptional.get();

        switch (arguments[3]) {
            case "-s":
                if (!Numbers.isLong(arguments[4])) {
                    commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "general.invalid-seed")
                            .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                            .replaceAll("%seed%", arguments[4]));
                    return;
                }

                this.worldManager.createWorld(commandSender.getName(), arguments[1], environment, Long.parseLong(arguments[4]));
                break;

            case "-g":
                final DefaultWorldGeneratorProvider generatorProvider = MultiWorldPlugin.getInstance().getWorldGeneratorProvider();

                if (!generatorProvider.isRegistered(arguments[4])) {
                    commandSender.sendMessage(this.translationProvider.getMessage(commandSender,"general.invalid-generator")
                            .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                            .replaceAll("%generator_name%", arguments[4]));
                    return;
                }
                this.worldManager.createWorld(commandSender.getName(), arguments[1], environment, arguments[4]);
                break;

            case "-t":
                final Optional<WorldType> typeOptional = WorldType.fromString(arguments[4].toUpperCase());

                if (typeOptional.isEmpty()) {
                    commandSender.sendMessage(super.getConfiguration().getString("general.world.type-not-exist")
                            .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
                    return;
                }
                this.worldManager.createWorld(commandSender.getName(), arguments[1], typeOptional.get());
                break;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if ((arguments.length < 3) || (arguments.length > 5)) {
            return Collections.emptyList();
        }

        if (arguments.length == 3) {
            return WorldType.toStringList();
        }

        if (arguments.length == 4) {
            return List.of("-g", "-s", "-t");
        }

        return switch (arguments[3]) {
            case "-g" -> new ArrayList<>(this.worldGeneratorProvider.getAllGenerators());
            case "-s" -> Collections.singletonList(String.valueOf(new Random().nextLong()));
            case "-t" -> WorldType.toStringList();
            default -> Collections.emptyList();
        };
    }

}
