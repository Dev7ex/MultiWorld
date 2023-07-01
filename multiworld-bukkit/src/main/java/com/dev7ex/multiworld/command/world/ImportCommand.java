package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.common.io.Files;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.world.WorldType;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
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
@CommandProperties(name = "import", permission = "multiworld.command.world.import")
public class ImportCommand extends BukkitCommand implements TabCompleter {

    public ImportCommand(@NotNull final BukkitPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 3) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.import.usage")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }
        final File worldFolder = new File(Bukkit.getWorldContainer(), arguments[1]);

        if (!Files.containsFile(worldFolder, "level.dat")) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-folder-not-exists")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%folder%", arguments[1]));
            return true;
        }

        if (MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolder(arguments[1]).isPresent()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.commands.import.world-already-imported")
                    .replaceAll("%prefix%", super.getPrefix())
                    .replaceAll("%world_name%", arguments[1]));
            return true;
        }
        final Optional<WorldType> typeOptional = WorldType.fromString(arguments[2].toUpperCase());

        if (typeOptional.isEmpty()) {
            commandSender.sendMessage(super.getConfiguration().getString("messages.general.world-type-not-exists")
                    .replaceAll("%prefix%", super.getPrefix()));
            return true;
        }
        MultiWorldPlugin.getInstance().getWorldManager().importWorld(commandSender.getName(), arguments[1], typeOptional.get());
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final Command command,
                                      @NotNull final String commandLabel, @NotNull final String[] arguments) {
        if ((arguments.length < 2) || (arguments.length > 3)) {
            return Collections.emptyList();
        }

        if (arguments.length == 3) {
            return WorldType.toStringList();
        }

        final List<String> files = Files.toStringList(Bukkit.getWorldContainer());
        final List<String> completions = Lists.newArrayList(files);

        for (final File folder : Objects.requireNonNull(Bukkit.getWorldContainer().listFiles())) {
            if (Files.containsFile(folder, "level.dat")) {
                continue;
            }
            completions.remove(folder.getName());
        }

        for (final String worldName : MultiWorldPlugin.getInstance().getWorldProvider().getWorldHolders().keySet()) {
            completions.remove(worldName);
        }
        return completions;
    }

}
