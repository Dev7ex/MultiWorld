package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.EmptyTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 05.01.2025
 */
@PaperCommandProperties(name = "setspawn", permission = "multiworld.command.world.setspawn")
public class SetSpawnCommand extends PaperCommand implements EmptyTabCompleter {

    private static final Sound SUCCESS_SOUND = Sound.sound(Key.key("entity.player.levelup"), Sound.Source.MASTER, 1.0F, 1.0F);

    private final WorldLocalizationRegistry localizationRegistry;

    public SetSpawnCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.localizationRegistry = plugin.getLocalizationRegistry();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (!(commandSender instanceof final Player player)) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_NO_CONSOLE_COMMAND)
                    .replaceText(builder -> builder.matchLiteral("%command_name%").replacement("/world " + super.getName())));
            return;
        }

        if (arguments.length != 1) {
            player.sendMessage(this.localizationRegistry.getComponent(player, WorldLocalizationEntry.COMMAND_WORLD_SETSPAWN_USAGE));
            return;
        }
        final World world = player.getWorld();

        world.setSpawnLocation(player.getLocation());

        player.sendMessage(this.localizationRegistry.getComponent(player, WorldLocalizationEntry.COMMAND_WORLD_SETSPAWN_SUCCESSFULLY_SET)
                .replaceText(builder -> builder.matchLiteral("%world_name%").replacement(world.getName())));
        player.playSound(SetSpawnCommand.SUCCESS_SOUND);
    }

}