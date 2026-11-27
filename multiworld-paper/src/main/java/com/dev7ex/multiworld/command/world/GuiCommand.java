package com.dev7ex.multiworld.command.world;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.EmptyTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.inventory.menu.main.WorldAdminMainMenu;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.api.user.WorldUser;
import com.dev7ex.multiworld.api.user.WorldUserRegistry;
import com.dev7ex.multiworld.inventory.menu.main.DefaultWorldAdminMainMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 22.11.2026
 */
@PaperCommandProperties(name = "gui", permission = "multiworld.command.world.gui")
public class GuiCommand extends PaperCommand implements EmptyTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;
    private final WorldUserRegistry userRegistry;

    public GuiCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.userRegistry = plugin.getUserRegistry();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        if (!(commandSender instanceof final Player player)) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_NO_CONSOLE_COMMAND)
                    .replaceText(builder -> builder.match("%command_name%").replacement("/world " + super.getName())));
            return;
        }

        if (arguments.length != 1) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_GUI_USAGE));
            return;
        }

        this.userRegistry.find(player.getUniqueId()).ifPresentOrElse(user -> {

            final WorldAdminMainMenu worldAdminMainMenu = new DefaultWorldAdminMainMenu(player,
                    this.localizationRegistry.getComponent(player, WorldLocalizationEntry.INVENTORY_WORLD_MAIN_ADMIN_TITLE),
                    1);

            ((WorldUser) user).openInventory(worldAdminMainMenu);

        }, () -> {

        });
    }

}
