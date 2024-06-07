package com.dev7ex.multiworld.api.bukkit.event.plugin;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event triggered when MultiWorld has finished loading.
 * This event provides information about the duration of the startup process.
 *
 * @author itsTyrion
 * @since 29.11.2022
 */
@Getter(AccessLevel.PUBLIC)
public class MultiWorldStartupCompleteEvent extends MultiWorldEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final long startupDuration;

    /**
     * Constructs a MultiWorldStartupCompleteEvent with the given MultiWorldBukkitApi instance and startup duration.
     *
     * @param multiWorldApi   The MultiWorldBukkitApi instance.
     * @param startupDuration The duration of the startup process, in milliseconds.
     */
    public MultiWorldStartupCompleteEvent(@NotNull final MultiWorldBukkitApi multiWorldApi, final long startupDuration) {
        super(multiWorldApi);
        this.startupDuration = startupDuration;
    }

    /**
     * Retrieves the handler list for this event.
     *
     * @return The handler list.
     */
    public static HandlerList getHandlerList() {
        return MultiWorldStartupCompleteEvent.HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return MultiWorldStartupCompleteEvent.HANDLERS;
    }

}
