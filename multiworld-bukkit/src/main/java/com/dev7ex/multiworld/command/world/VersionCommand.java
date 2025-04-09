package com.dev7ex.multiworld.command.world;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.command.BukkitCommandProperties;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.util.Colored;
import com.dev7ex.multiworld.util.PluginUpdater;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 22.07.2024
 */
@BukkitCommandProperties(name = "version", permission = "multiworld.command.world.version")
public class VersionCommand extends BukkitCommand {

    private final PluginUpdater updater;
    private final PluginDescriptionFile descriptionFile;

    public VersionCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.updater = plugin.getUpdater();
        this.descriptionFile = plugin.getDescription();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final String[] arguments) {
        final MultiWorldPlugin plugin = MultiWorldPlugin.getInstance();

        commandSender.sendMessage(" ");
        commandSender.sendMessage("§f§m                    §r§r " + super.getConfiguration().getPrefix() + " §f§m                    ");
        commandSender.sendMessage(" ");
        commandSender.sendMessage("§8» §bVersion: §a" + this.descriptionFile.getVersion());
        commandSender.sendMessage("§8» §bAuthors: " + this.getAuthors());
        commandSender.sendMessage("§8» §bSupport: §adiscord.dev7ex.com");
        commandSender.sendMessage("§8» §bWiki: §ahttps://github.com/Dev7ex/MultiWorld/wiki");
        commandSender.sendMessage("§8» §bReport Bug: §ahttps://github.com/Dev7ex/MultiWorld/issues");
        commandSender.sendMessage("§8» §bUpdate Available: " + Colored.getColoredBoolean(this.updater.isUpdateAvailable()));
        commandSender.sendMessage(" ");
        commandSender.sendMessage("§f§m                    §r§r " + super.getConfiguration().getPrefix() + " §f§m                    ");
        commandSender.sendMessage(" ");
    }

    private String getAuthors() {
        final StringBuilder stringBuilder = new StringBuilder();

        for (final String author : this.descriptionFile.getAuthors()) {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append(ChatColor.GRAY);
                stringBuilder.append(", ");
            }
            stringBuilder.append(ChatColor.GREEN).append(author);
        }
        return stringBuilder.toString();
    }

}