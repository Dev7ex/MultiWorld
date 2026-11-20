package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.ArgumentTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.api.user.WorldUserRegistry;
import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.WorldRegistry;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@PaperCommandProperties(name = "teleport", permission = "multiworld.command.world.teleport", aliases = "tp")
public class TeleportCommand extends PaperCommand implements ArgumentTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;
    private final WorldRegistry worldRegistry;
    private final WorldUserRegistry userRegistry;

    public TeleportCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);

        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.worldRegistry = plugin.getWorldRegistry();
        this.userRegistry = plugin.getUserRegistry();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if ((arguments.length < 2) || (arguments.length > 3)) {
            final boolean hasAdminPermission = commandSender.hasPermission("multiworld.command.world.teleport.admin");
            final WorldLocalizationEntry usageProperty = hasAdminPermission
                    ? WorldLocalizationEntry.COMMAND_WORLD_TELEPORT_USAGE_ADMIN
                    : WorldLocalizationEntry.COMMAND_WORLD_TELEPORT_USAGE_USER;
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, usageProperty));
            return;
        }
        if (arguments.length == 2) {
            if (!(commandSender instanceof final Player player)) {
                commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_NO_CONSOLE_COMMAND)
                        .replaceText(builder -> builder.match("%command_name%").replacement(this.getName())));
                return;
            }
            final String originalWorldName = arguments[1].asString();
            final String worldName;

            if (originalWorldName.equalsIgnoreCase("%creator_name%")) {
                worldName = originalWorldName.replaceAll("%creator_name%", commandSender.getName());

            } else {
                worldName = originalWorldName;
            }

            this.worldRegistry.find(worldName).ifPresentOrElse(worldContext -> {
                if (!worldContext.isLoaded()) {
                    commandSender.sendMessage(
                            this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_LOADED)
                                    .replaceText(builder -> builder.match("%world_name%").replacement(worldName)));
                    return;
                }
                final World world = Objects.requireNonNull(worldContext.getWorld()); // Safe

                if (Objects.requireNonNull(worldContext.getWorld()).getEntities().contains(player)) {

                    return;
                }
                player.teleport(world.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                return;

            }, () -> {
                commandSender.sendMessage(
                        this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_EXISTS)
                                .replaceText(builder -> builder.match("%world_name%").replacement(worldName)));
            });
            return;
        }
        final String entityArgument = arguments[1].asString();


        if (Bukkit.getPlayer(entityArgument) == null) {
            final List<Entity> entities;
            final String selector = entityArgument;

            try {
                entities = Bukkit.selectEntities(commandSender, selector);

            } catch (final IllegalArgumentException exception) {
                commandSender.sendMessage(
                        this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_VALIDATION_INVALID_SELECTOR)
                                .replaceText(builder -> builder.match("%input%").replacement(selector)));
                return;
            }
            final String originalWorldName = arguments[2].asString();
            final String worldName;

            if (originalWorldName.equalsIgnoreCase("%creator_name%")) {
                worldName = originalWorldName.replaceAll("%creator_name%", commandSender.getName());

            } else {
                worldName = originalWorldName;
            }

            this.worldRegistry.find(worldName).ifPresentOrElse(worldContext -> {
                if (!worldContext.isLoaded()) {
                    commandSender.sendMessage(
                            this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_LOADED)
                                    .replaceText(builder -> builder.match("%world_name%").replacement(worldName)));
                    return;
                }
                final World world = Objects.requireNonNull(worldContext.getWorld()); // Safe

                for (final Entity entity : entities) {
                    if (entity.getWorld().getName().equalsIgnoreCase(worldName)) {
                        continue;
                    }
                    entity.teleport(world.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                }

            }, () -> {
                commandSender.sendMessage(
                        this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_EXISTS)
                                .replaceText(builder -> builder.match("%world_name%").replacement(worldName)));
            });

        } else {
            final Player target = Bukkit.getPlayer(entityArgument);

            assert target != null;

            final String worldName = arguments[2].asString();

            this.worldRegistry.find(worldName).ifPresentOrElse(worldContext -> {
                if (!worldContext.isLoaded()) {
                    commandSender.sendMessage(
                            this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_LOADED)
                                    .replaceText(builder -> builder.match("%world_name%").replacement(worldName)));
                    return;
                }
                final World world = Objects.requireNonNull(worldContext.getWorld()); // Safe

                target.teleport(world.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);

            }, () -> {
                commandSender.sendMessage(
                        this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_NOT_EXISTS)
                                .replaceText(builder -> builder.match("%world_name%").replacement(worldName)));
            });

        }
    }

    @Override
    public List<String> onTabComplete(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (arguments.length < 1 || arguments.length > 4) {
            return Collections.emptyList();
        }

        // Nur Welten vorschlagen, für die der Spieler die Berechtigung hat
        final List<String> allowedWorlds = this.worldRegistry.getLoadedWorlds()
                .stream()
                .map(WorldContext::getName)
                .filter(worldName -> commandSender.hasPermission("multiworld.command.world.teleport." + worldName))
                .toList();

        if (arguments.length == 2) {
            Stream<String> selectorStream = Stream.empty();
            Stream<String> playerStream = Stream.empty();

            // Admins: Selektoren + alle Online-Spieler
            if (commandSender.hasPermission("multiworld.command.world.teleport.admin")) {
                selectorStream = Stream.of("@a", "@p", "@r", "@s", "@e");
                playerStream = Bukkit.getOnlinePlayers().stream().map(OfflinePlayer::getName);
            }

            // Alle Vorschläge zusammenführen
            return Stream.of(selectorStream, playerStream, allowedWorlds.stream())
                    .flatMap(Function.identity())
                    .distinct()
                    .sorted()
                    .toList();
        }

        // Bei anderen Argumenten: nur erlaubte Welten vorschlagen
        return allowedWorlds;
    }

}
