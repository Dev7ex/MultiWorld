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
 * @author Dev7ex
 * @since 29.03.2024
 */
public class PlayerAdvancementDoneListener extends MultiWorldListener {

    public PlayerAdvancementDoneListener(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        super(multiWorldApi);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handlePlayerAdvancementDone(final PlayerAdvancementDoneEvent event) {
        final Player player = event.getPlayer();
        final World world = player.getWorld();

        if (super.getWorldProvider().getWorldHolder(world.getName()).isEmpty()) {
            return;
        }
        final BukkitWorldHolder worldHolder = super.getWorldProvider().getWorldHolder(world.getName()).get();

        if (worldHolder.isReceiveAchievements()) {
            return;
        }
        final Advancement advancement = event.getAdvancement();
        final AdvancementProgress progress = player.getAdvancementProgress(advancement);

        if (progress.isDone()) {
            progress.getAwardedCriteria().forEach(progress::revokeCriteria);
        }

    }

}
