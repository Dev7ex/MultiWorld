package com.dev7ex.multiworld.command;

import com.dev7ex.common.bukkit.command.SubCommand;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.user.WorldUser;
import com.dev7ex.multiworld.user.WorldUserService;
import com.dev7ex.multiworld.world.WorldManager;

import java.util.UUID;

/**
 *
 * @author Dev7ex
 * @since 20.05.2021
 *
 */

public abstract class WorldSubCommand extends SubCommand {

    protected final WorldUserService worldUserService;
    protected final WorldManager worldManager;

    public WorldSubCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        this.worldUserService = plugin.getWorldUserService();
        this.worldManager = plugin.getWorldManager();
    }

    public final WorldUser getWorldUser(final UUID uniqueId) {
        return this.worldUserService.getUsers().get(uniqueId);
    }

}
