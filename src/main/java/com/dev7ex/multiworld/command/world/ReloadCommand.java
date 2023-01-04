package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.CommandProperties;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.command.WorldSubCommand;
import org.bukkit.command.CommandSender;

/**
 * @author Dev7ex
 * @since 28.12.2022
 */
@CommandProperties(name = "reload", permission = "multiworld.command.world.reload")
public class ReloadCommand extends WorldSubCommand {

    public ReloadCommand(final MultiWorldPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String[] arguments) {
        if (!commandSender.hasPermission(this.getPermission())) {
            commandSender.sendMessage(super.getNoPermissionMessage());
            return true;
        }

        if (arguments.length != 1) {
            commandSender.sendMessage(super.getConfiguration().getCommandUsage(this));
            return true;
        }
        super.getPlugin().getConfiguration().load();
        commandSender.sendMessage(super.getConfiguration().getMessage("reload.successfully-reloaded"));
        return false;
    }

}
