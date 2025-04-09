package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.common.io.file.Files;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.world.WorldEnvironment;
import com.dev7ex.multiworld.api.world.WorldType;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import com.dev7ex.multiworld.world.DefaultWorldManager;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import com.dev7ex.multiworld.world.generator.DefaultWorldGeneratorProvider;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

/**
 * @author Dev7ex
 * @since 25.05.2021
 */
@BukkitCommandProperties(name = "import", permission = "multiworld.command.world.import")
public class ImportCommand extends BukkitCommand implements BukkitTabCompleter {

    private final DefaultTranslationProvider translationProvider;
    private final DefaultWorldManager worldManager;
    private final DefaultWorldProvider worldProvider;
    private final DefaultWorldGeneratorProvider worldGeneratorProvider;

    public ImportCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.translationProvider = plugin.getTranslationProvider();
        this.worldManager = plugin.getWorldManager();
        this.worldProvider = plugin.getWorldProvider();
        this.worldGeneratorProvider = plugin.getWorldGeneratorProvider();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 5) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.import.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }
        final File worldFile = new File(Bukkit.getWorldContainer(), arguments[1]);

        if (!worldFile.isDirectory()) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender,"general.world.folder-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%folder%", arguments[1]));
            return;
        }

        if (!Files.containsFile(worldFile, "level.dat")) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender,"general.world.folder-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%folder%", arguments[1]));
            return;
        }

        if (arguments[1].equalsIgnoreCase("%creator_name%")) {
            arguments[1] = arguments[1].replaceAll("%creator_name%", commandSender.getName());
        }

        if (this.worldProvider.getWorldHolder(arguments[1]).isPresent()) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender,"commands.world.import.world-already-imported")
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
            case "-g":
                if (!this.worldGeneratorProvider.isRegistered(arguments[4])) {
                    commandSender.sendMessage(this.translationProvider.getMessage(commandSender,"general.invalid-generator")
                            .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                            .replaceAll("%generator_name%", arguments[3]));
                    return;
                }
                this.worldManager.importWorld(commandSender.getName(), arguments[1], environment, arguments[4]);
                break;

            case "-t":
                final Optional<WorldType> typeOptional = WorldType.fromString(arguments[4].toUpperCase());

                if (typeOptional.isEmpty()) {
                    commandSender.sendMessage(this.translationProvider.getMessage(commandSender,"general.world.type-not-exist")
                            .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                            .replaceAll("%world_type%", arguments[4]));
                    return;
                }
                this.worldManager.importWorld(commandSender.getName(), arguments[1], environment, typeOptional.get());
                break;

            default:
                commandSender.sendMessage(this.translationProvider.getMessage(commandSender,"commands.world.import.usage")
                        .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                        .replaceAll("%world_type%", arguments[4]));
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if ((arguments.length < 2) || (arguments.length > 5)) {
            return Collections.emptyList();
        }

        if (arguments.length == 2) {
            final List<String> files = Files.toStringList(Bukkit.getWorldContainer());
            final List<String> completions = Lists.newArrayList(files);

            for (final File file : Objects.requireNonNull(Bukkit.getWorldContainer().listFiles())) {
                if (!file.isDirectory()) {
                    continue;
                }

                if (Files.containsFile(file, "level.dat")) {
                    continue;
                }
                completions.remove(file.getName());
            }

            for (final String worldName : this.worldProvider.getWorldHolders().keySet()) {
                completions.remove(worldName);
            }
            return completions;
        }

        if (arguments.length == 3) {
            return WorldType.toStringList();
        }

        if (arguments.length == 4) {
            return List.of("-g", "-t");
        }

        return switch (arguments[3]) {
            case "-g" -> new ArrayList<>(this.worldGeneratorProvider.getAllGenerators());
            case "-t" -> WorldType.toStringList();
            default -> Collections.emptyList();
        };
    }

}
