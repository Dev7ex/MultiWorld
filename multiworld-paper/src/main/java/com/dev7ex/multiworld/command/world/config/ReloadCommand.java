package com.dev7ex.multiworld.command.world.config;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.EmptyTabCompleter;
import com.dev7ex.multiworld.MultiWorldConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import io.papermc.paper.registry.keys.SoundEventKeys;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Dev7ex
 * @since 05.01.2025
 */
@PaperCommandProperties(name = "reload", permission = "multiworld.command.world.config.reload")
public class ReloadCommand extends PaperCommand implements EmptyTabCompleter {

    private final MultiWorldConfiguration configuration;
    private final WorldLocalizationRegistry localizationRegistry;

    public ReloadCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.configuration = plugin.getConfiguration();
        this.localizationRegistry = plugin.getLocalizationRegistry();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length != 2) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_CONFIG_RELOAD_USAGE));
            return;
        }
        commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_CONFIG_RELOAD_STARTING));
        commandSender.playSound(Sound.sound().type(SoundEventKeys.BLOCK_NOTE_BLOCK_HARP).build());

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            try {
                this.configuration.loadFile();

                commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_CONFIG_RELOAD_FINISHING));
                commandSender.playSound(Sound.sound().type(SoundEventKeys.BLOCK_NOTE_BLOCK_PLING).build());

            } catch (final Exception exception) {
                this.plugin.getLogger().severe("Failed to reload the configuration: " + exception.getMessage());
            }
        }, 10L);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        return List.of();
    }

}
