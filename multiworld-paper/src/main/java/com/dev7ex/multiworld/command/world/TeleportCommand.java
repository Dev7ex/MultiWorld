package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.WorldRegistry;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@PaperCommandProperties(name = "teleport", permission = "multiworld.command.world.teleport", aliases = "tp")
public class TeleportCommand extends PaperCommand implements ArgumentTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;
    private final WorldRegistry worldRegistry;

    public TeleportCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.worldRegistry = plugin.getWorldRegistry();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        final boolean hasAdminPermission = commandSender.hasPermission("multiworld.command.world.teleport.admin");

        if ((arguments.length < 2) || (arguments.length > 3)) {
            final WorldLocalizationEntry usageProperty = hasAdminPermission
                    ? WorldLocalizationEntry.COMMAND_WORLD_TELEPORT_USAGE_ADMIN
                    : WorldLocalizationEntry.COMMAND_WORLD_TELEPORT_USAGE_USER;

            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, usageProperty));
            return;
        }
        if ((arguments.length == 2) && (!(commandSender instanceof Player))) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_NO_CONSOLE_COMMAND)
                    .replaceText(builder -> builder.match("%command_name%").replacement(this.getName())));
            return;
        }
        if ((arguments.length == 3) && (!hasAdminPermission)) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_NO_PERMISSION));
            return;
        }
        final String originalWorldName = arguments[arguments.length - 1].asString();
        final String worldName = originalWorldName.equalsIgnoreCase("%creator_name%") ? commandSender.getName() : originalWorldName;

        if (!commandSender.hasPermission("multiworld.command.world.teleport." + worldName)) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_NO_PERMISSION));
            return;
        }
        final World world = this.findWorld(commandSender, worldName);

        if (world == null) {
            return;
        }
        if (arguments.length == 2) {
            final Player player = (Player) commandSender;

            if (!player.getWorld().equals(world)) {
                player.teleportAsync(world.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
            return;
        }
        final String entityArgument = arguments[1].asString();
        final Player target = Bukkit.getPlayer(entityArgument);
        final List<Entity> entities;

        if (target != null) {
            entities = List.of(target);

        } else {
            try {
                entities = Bukkit.selectEntities(commandSender, entityArgument);

            } catch (final IllegalArgumentException exception) {
                commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_VALIDATION_INVALID_SELECTOR)
                        .replaceText(builder -> builder.match("%input%").replacement(entityArgument)));
                return;
            }
        }
        for (final Entity entity : entities) {
            if (entity.getWorld().equals(world)) {
                continue;
            }
            entity.teleportAsync(world.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if ((arguments.length < 2) || (arguments.length > 3)) {
            return Collections.emptyList();
        }
        final boolean hasAdminPermission = commandSender.hasPermission("multiworld.command.world.teleport.admin");

        if ((arguments.length == 3) && (!hasAdminPermission)) {
            return Collections.emptyList();
        }
        final String input = arguments[arguments.length - 1].asString().toLowerCase(Locale.ROOT);

        Stream<String> completions = this.worldRegistry.getLoadedWorlds().stream()
                .map(WorldContext::getName)
                .filter(worldName -> commandSender.hasPermission("multiworld.command.world.teleport." + worldName));

        if ((arguments.length == 2) && (hasAdminPermission)) {
            completions = Stream.concat(completions, Stream.concat(
                    Stream.of("@a", "@p", "@r", "@s", "@e"),
                    Bukkit.getOnlinePlayers().stream().map(Player::getName)
            ));
        }
        return completions
                .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(input))
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    private @Nullable World findWorld(@NotNull final CommandSender commandSender, @NotNull final String worldName) {
        final WorldContext worldContext = this.worldRegistry.find(worldName).orElse(null);

        if (worldContext == null) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_EXISTS)
                    .replaceText(builder -> builder.match("%world_name%").replacement(worldName)));
            return null;
        }
        final World world = worldContext.getWorld();

        if ((!worldContext.isLoaded()) || (world == null)) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_LOADED)
                    .replaceText(builder -> builder.match("%world_name%").replacement(worldName)));
            return null;
        }
        return world;
    }

}