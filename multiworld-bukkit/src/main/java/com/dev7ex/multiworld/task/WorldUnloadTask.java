package com.dev7ex.multiworld.task;

import com.dev7ex.common.scheduler.task.Task;
import com.dev7ex.multiworld.MultiWorldConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import com.dev7ex.multiworld.world.DefaultWorldProvider;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Dev7ex
 * @since 18.09.2024
 */
public class WorldUnloadTask implements Task {

    private final DefaultWorldProvider worldProvider;
    private final Queue<BukkitWorldHolder> worldUnloadQueue = new ConcurrentLinkedQueue<>();
    private final MultiWorldConfiguration configuration = MultiWorldPlugin.getInstance().getConfiguration();

    public WorldUnloadTask(@NotNull final DefaultWorldProvider worldProvider) {
        this.worldProvider = worldProvider;
    }

    @Override
    public void run() {
        for (final BukkitWorldHolder worldHolder : this.worldProvider.getWorldHolders().values()) {
            if (!worldHolder.isAutoUnloadEnabled()) {
                continue;
            }

            if (!worldHolder.isLoaded()) {
                continue;
            }

            if ((System.currentTimeMillis() - worldHolder.getLoadTimeStamp()) < TimeUnit.SECONDS.toMillis(this.configuration.getAutoUnloadLoadDelay())) {
                continue;
            }

            if (!worldHolder.getWorld().getPlayers().isEmpty()) {
                return;
            }

            if ((System.currentTimeMillis() - worldHolder.getLastActivity()) >= (this.configuration.getAutoUnloadSystemDelay() * 1000L)) {
                if (!this.worldUnloadQueue.contains(worldHolder)) {
                    this.worldUnloadQueue.add(worldHolder);
                }
            }
        }

        if (!this.worldUnloadQueue.isEmpty()) {
            final BukkitWorldHolder worldToUnload = this.worldUnloadQueue.poll();

            if (worldToUnload != null) {
                this.worldProvider.getWorldManager().unloadWorld(Bukkit.getConsoleSender().getName(), worldToUnload.getName());
            }
        }
    }

}
