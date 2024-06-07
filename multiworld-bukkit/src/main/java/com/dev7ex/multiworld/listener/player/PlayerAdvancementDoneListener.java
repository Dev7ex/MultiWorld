package com.dev7ex.multiworld.listener.player;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.event.MultiWorldListener;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Listener for player advancements.
 * This listener handles the event when a player completes an advancement.
 * It revokes the advancement if the world settings specify that achievements should not be received.
 *
 * @since 29.03.2024
 */
public class PlayerAdvancementDoneListener extends MultiWorldListener {

    /**
     * Constructs a new PlayerAdvancementDoneListener.
     *
     * @param multiWorldApi The MultiWorldBukkitApi instance.
     */
    public PlayerAdvancementDoneListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        super(multiWorldApi);
    }

    /**
     * Handles the PlayerAdvancementDoneEvent.
     * If the world does not allow receiving achievements, the completed advancement is revoked.
     *
     * @param event The PlayerAdvancementDoneEvent.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerAdvancementDone(final PlayerAdvancementDoneEvent event) {
        final Player player = event.getPlayer();
        final World world = player.getWorld();

        // Get the world holder for the current world
        final BukkitWorldHolder worldHolder = super.getWorldProvider().getWorldHolder(world.getName()).orElse(null);

        // If the worldHolder is null or the world allows receiving achievements, return
        if (worldHolder == null || worldHolder.isReceiveAchievements()) {
            return;
        }

        // Get the advancement and its progress
        final Advancement advancement = event.getAdvancement();
        final AdvancementProgress progress = player.getAdvancementProgress(advancement);

        // If the advancement is complete, revoke all awarded criteria
        if (progress.isDone()) {
            progress.getAwardedCriteria().forEach(progress::revokeCriteria);
        }
    }
}
