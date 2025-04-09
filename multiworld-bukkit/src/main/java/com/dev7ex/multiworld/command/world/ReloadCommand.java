package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 28.12.2022
 */
@BukkitCommandProperties(name = "reload", permission = "multiworld.command.world.reload")
public class ReloadCommand extends BukkitCommand {

    private final DefaultTranslationProvider translationProvider;

    public ReloadCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.translationProvider = plugin.getTranslationProvider();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        if (arguments.length != 1) {
            commandSender.sendMessage(this.translationProvider.getMessage(commandSender, "commands.world.reload.usage")
                    .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
            return;
        }
        super.getConfiguration().load();

        commandSender.sendMessage(this.translationProvider.getMessage(commandSender,"commands.world.reload.message")
                .replaceAll("%prefix%", super.getConfiguration().getPrefix()));
    }

}
