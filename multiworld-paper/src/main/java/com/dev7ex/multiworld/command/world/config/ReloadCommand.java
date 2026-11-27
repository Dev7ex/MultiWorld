package com.dev7ex.multiworld.command.world.config;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.EmptyTabCompleter;
import com.dev7ex.multiworld.MultiWorldConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.api.scheduler.WorldScheduler;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

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
        commandSender.playSound(Sound.sound(Key.key("block.note_block.harp"), Sound.Source.MASTER, 1.0F, 1.0F));

        final MultiWorldPlugin multiWorldPlugin = (MultiWorldPlugin) this.plugin;
        final WorldScheduler scheduler = multiWorldPlugin.getScheduler();

        scheduler.runGlobalLater(10L, () -> scheduler.runTaskAsynchronously(() -> {
            try {
                this.configuration.loadFile();

            } catch (final Exception exception) {
                this.plugin.getLogger().severe("Failed to reload the configuration: " + exception.getMessage());
                return;
            }
            final Runnable finishing = () -> {
                commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_CONFIG_RELOAD_FINISHING));
                commandSender.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.MASTER, 1.0F, 1.0F));
            };
            if (commandSender instanceof final Entity entity) {
                scheduler.runOnEntity(entity, finishing);
                return;
            }
            scheduler.runTask(finishing);
        }));
    }

}
