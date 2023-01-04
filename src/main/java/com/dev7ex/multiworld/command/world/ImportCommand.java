package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.common.io.Files;
import com.dev7ex.common.util.NumberUtil;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;
import com.dev7ex.multiworld.world.WorldType;

import com.google.common.collect.Lists;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 25.05.2021
 */
@CommandProperties(name = "import", permission = "multiworld.command.world.import")
public final class ImportCommand extends WorldSubCommand implements TabCompleter {

    public ImportCommand(final MultiWorldPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!commandSender.hasPermission(this.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 3) {
            commandSender.sendMessage(super.getConfiguration().getCommandUsage(this));
            return true;
        }
        final File worldFolder = new File(Bukkit.getWorldContainer(), arguments[1]);

        if(!Files.containsFile(worldFolder, "level.dat")) {
            commandSender.sendMessage(super.getConfiguration().getMessage("general.folder-not-exists").replaceAll("%folder%", arguments[1]));
            return true;
        }

        if(super.getWorldManager().getWorldProperties().containsKey(arguments[1])) {
            commandSender.sendMessage(super.getConfiguration().getMessage("import.already-imported").replaceAll("%world%", arguments[1]));
            return true;
        }
        final Optional<WorldType> optional = WorldType.fromString(arguments[2].toUpperCase());

        if ((optional.isEmpty())) {
            commandSender.sendMessage(super.getConfiguration().getMessage("general.invalid-input"));
            return true;
        }
        super.getWorldManager().importWorld(commandSender, arguments[1], optional.get());
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String commandLabel, final String[] arguments) {
        if((arguments.length < 2) || (arguments.length > 3)) {
            return null;
        }

        if(arguments.length == 3) {
            return WorldType.toStringList();
        }
        final List<String> files = Files.toStringList(Bukkit.getWorldContainer());
        final List<String> completions = Lists.newArrayList(files);

        for(final File folder : Objects.requireNonNull(Bukkit.getWorldContainer().listFiles())) {
            if(Files.containsFile(folder, "level.dat")) {
                continue;
            }
            completions.remove(folder.getName());
        }
        return completions;
    }

}
