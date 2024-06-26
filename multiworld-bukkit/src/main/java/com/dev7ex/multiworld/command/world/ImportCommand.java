package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.command.completer.BukkitTabCompleter;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.common.io.file.Files;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.world.WorldType;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 25.05.2021
 */
@BukkitCommandProperties(name = "import", permission = "multiworld.command.world.import")
public class ImportCommand extends BukkitCommand implements BukkitTabCompleter {

    public ImportCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 3) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.import.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }
        final File worldFile = new File(Bukkit.getWorldContainer(), arguments[1]);

        if (!worldFile.isDirectory()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-folder-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%folder%", arguments[1]));
            return;
        }

        if (!Files.containsFile(worldFile, "level.dat")) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-folder-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%folder%", arguments[1]));
            return;
        }

        if (arguments[1].equalsIgnoreCase("%creator_name%")) {
            arguments[1] = arguments[1].replaceAll("%creator_name%", commandSender.getName());
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).isPresent()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.import.world-already-imported")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return;
        }
        final Optional<WorldType> typeOptional = WorldType.fromString(arguments[2].toUpperCase());

        if (typeOptional.isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-type-not-exists")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }
        MultiWorldPlugin.getInstance().getWorldManager().importWorld(commandSender.getName(), arguments[1], typeOptional.get());
        return;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if ((arguments.length < 2) || (arguments.length > 3)) {
            return Collections.emptyList();
        }

        if (arguments.length == 3) {
            return WorldType.toStringList();
        }

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

        for (final String worldName : MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders().keySet()) {
            completions.remove(worldName);
        }
        return completions;
    }

}
