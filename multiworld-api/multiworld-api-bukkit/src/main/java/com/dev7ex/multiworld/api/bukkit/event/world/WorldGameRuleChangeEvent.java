package com.dev7ex.multiworld.api.bukkit.event.world;

import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameRule;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 29.03.2024
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class WorldGameRuleChangeEvent extends WorldEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private GameRule<?> gameRule;
    private String value;

    public WorldGameRuleChangeEvent(@NotNull final BukkitWorldHolder worldHolder, @NotNull final CommandSender commandSender, @NotNull final GameRule<?> gameRule, @NotNull final String value) {
        super(worldHolder, commandSender);
        this.gameRule = gameRule;
        this.value = value;
    }


    public static HandlerList getHandlerList() {
        return WorldGameRuleChangeEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return WorldGameRuleChangeEvent.HANDLERS;
    }

}
