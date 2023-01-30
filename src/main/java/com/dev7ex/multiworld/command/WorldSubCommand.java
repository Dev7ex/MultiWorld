package com.dev7ex.multiworld.command;

import com.dev7ex.common.bukkit.command.BukkitCommand;

import com.dev7ex.multiworld.MultiWorldConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.user.WorldUser;
import com.dev7ex.multiworld.user.WorldUserService;
import com.dev7ex.multiworld.world.WorldManager;
import com.dev7ex.multiworld.world.WorldService;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
@Getter(AccessLevel.PUBLIC)
public abstract class WorldSubCommand extends BukkitCommand {

    private final WorldUserService worldUserService;
    private final WorldManager worldManager;
    private final MultiWorldConfiguration configuration;
    private final WorldService worldService;

    public WorldSubCommand(final MultiWorldPlugin plugin) {
        super(plugin);
        this.worldUserService = plugin.getWorldUserService();
        this.worldManager = plugin.getWorldManager();
        this.configuration = plugin.getConfiguration();
        this.worldService = plugin.getWorldService();
    }

    public final WorldUser getWorldUser(final UUID uniqueId) {
        return this.worldUserService.getUsers().get(uniqueId);
    }

}
