package com.dev7ex.multiworld.listener.world;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.event.MultiWorldListener;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldType;
import com.dev7ex.multiworld.api.world.WorldEnvironment;
import com.dev7ex.multiworld.api.world.WorldType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldInitEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 02.02.2025
 */
public class WorldInitializeListener extends MultiWorldListener {

    /**
     * Constructs a MultiWorldListener with the given MultiWorldBukkitApi.
     *
     * @param multiWorldApi The MultiWorldBukkitApi instance.
     */
    public WorldInitializeListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        super(multiWorldApi);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleWorldInitialize(final WorldInitEvent event) {
        final World world = event.getWorld();

        if (super.getWorldProvider().getWorldHolder(world.getName()).isPresent()) {
            return;
        }
        final WorldType worldType = BukkitWorldType.fromEnvironment(world.getEnvironment());
        super.getWorldManager().importWorld(Bukkit.getConsoleSender().getName(), world.getName(), WorldEnvironment.fromType(worldType), worldType);

        super.getConsoleSender().sendMessage(String.format("§cThe world %s was registered by MultiWorld.", event.getWorld().getName()));
        super.getConsoleSender().sendMessage("§cPlease note that you may have to change the flags yourself if this world has special properties like Void WorldType or similar");
    }

}
