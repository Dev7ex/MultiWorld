package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.api.world.*;
import com.dev7ex.multiworld.api.world.generator.WorldGeneratorRegistry;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@PaperCommandProperties(name = "create", permission = "multiworld.command.world.create")
public class CreateCommand extends PaperCommand implements ArgumentTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;
    private final WorldGeneratorRegistry generatorRegistry;
    private final WorldFactory worldFactory;
    private final WorldRegistry registry;

    public CreateCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.generatorRegistry = plugin.getGeneratorRegistry();
        this.worldFactory = plugin.getWorldFactory();
        this.registry = plugin.getWorldRegistry();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length != 5) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_CREATE_USAGE));
            return;
        }
        final String firstArgument = arguments[1].asString();
        final String worldName = firstArgument.equalsIgnoreCase("%creator_name%")
                ? commandSender.getName()
                : firstArgument;

        this.registry.find(worldName).ifPresentOrElse(worldHolder -> {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_ALREADY_EXISTS)
                    .replaceText(builder -> builder.match("%world_name%").replacement(worldName))
                    .replaceText(builder -> builder.match("%world_display_name%").replacement(worldHolder.getDisplayName()))
                    .replaceText(builder -> builder.match("%world_colored_display_name%").replacement(worldHolder.getColoredDisplayName())));

        }, () -> {
            final String environmentName = arguments[2].asString().toUpperCase();
            final WorldEnvironment environment;

            try {
                environment = WorldEnvironment.valueOf(environmentName);

            } catch (final IllegalArgumentException exception) {
                commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_ENVIRONMENT_INVALID)
                        .replaceText(builder -> builder.match("%environment_name%").replacement(environmentName)));
                return;
            }
            final String parameter = arguments[3].asString();

            switch (parameter) {
                case "-g": {
                    return;
                }

                case "-s": {
                    final long seed;

                    try {
                        seed = Long.parseLong(arguments[4].asString());

                    } catch (final NumberFormatException exception) {
                        commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_INVALID_SEED)
                                .replaceText(builder -> builder.match("%seed%").replacement(arguments[4].asString())));
                        return;
                    }
                    this.worldFactory.create(commandSender, new WorldBuilder(worldName).environment(environment).seed(seed));
                    return;
                }

                case "-t": {
                    final String worldTypeName = arguments[4].asString();
                    final WorldType worldType;

                    try {
                        worldType = WorldType.valueOf(worldTypeName);

                    } catch (final IllegalArgumentException exception) {
                        commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_INVALID_TYPE)
                                .replaceText(builder -> builder.match("%world_type%").replacement(worldTypeName)));
                        return;
                    }
                    this.worldFactory.create(commandSender, new WorldBuilder(worldName).type(worldType).environment(environment));
                    return;
                }

                default: {
                    commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_CREATE_USAGE));
                }
            }
        });
    }

    @Override
    public List<String> onTabComplete(
            @NotNull final CommandSender commandSender,
            @NotNull final PaperCommandArgument[] arguments
    ) {
        if (arguments.length < 2 || arguments.length > 5) {
            return Collections.emptyList();
        }

        if (arguments.length == 2) {
            return this.registry.find(commandSender.getName()).isEmpty()
                    ? List.of("%creator_name%")
                    : Collections.emptyList();
        }

        if (arguments.length == 3) {
            return WorldEnvironment.names();
        }

        if (arguments.length == 4) {
            return List.of("-g", "-s", "-t");
        }

        return switch (arguments[3].asString()) {
            // case "-g" -> new ArrayList<>(this.worldGeneratorProvider.getAllGenerators());
            case "-s" -> List.of(String.valueOf(new Random().nextLong()));
            case "-t" -> WorldType.names();
            default -> Collections.emptyList();
        };
    }


}
