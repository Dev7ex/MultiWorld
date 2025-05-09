package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.translation.DefaultTranslationProvider;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@BukkitCommandProperties(name = "help", permission = "multiworld.command.world")
public class HelpCommand extends BukkitCommand {

    private final DefaultTranslationProvider translationProvider;

    public HelpCommand(final MultiWorldPlugin plugin) {
        super(plugin);

        this.translationProvider = plugin.getTranslationProvider();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        this.translationProvider.getMessageList(commandSender, "commands.world.help.message")
                .forEach(message -> commandSender.sendMessage(message.replaceAll("%prefix%", super.getConfiguration().getPrefix())));
    }

}
