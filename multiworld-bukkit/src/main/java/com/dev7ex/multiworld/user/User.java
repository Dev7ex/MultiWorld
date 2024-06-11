package com.dev7ex.multiworld.user;

import com.dev7ex.multiworld.api.bukkit.user.BukkitWorldUser;
import com.dev7ex.multiworld.api.bukkit.world.location.BukkitWorldLocation;
import com.dev7ex.multiworld.api.user.WorldUserConfiguration;
import com.dev7ex.multiworld.api.world.WorldHolder;
import com.dev7ex.multiworld.api.world.location.WorldLocation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a user in the MultiWorld system.
 * This class implements BukkitWorldUser and EntityResolver for Player entities.
 * It manages user-specific information and actions within the world.
 *
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class User implements BukkitWorldUser {

    private final UUID uniqueId;
    private final String name;
    private WorldUserConfiguration configuration;
    private WorldLocation lastLocation;
    private long lastLogin;

    /**
     * Constructs a new User with the specified unique ID and name.
     *
     * @param uniqueId The unique ID of the user.
     * @param name     The name of the user.
     */
    public User(@NotNull final UUID uniqueId, @NotNull final String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }

    /**
     * Sends a message to the user.
     *
     * @param message The message to send.
     */
    @Override
    public void sendMessage(@NotNull final String message) {
        this.getEntity().sendMessage(message);
    }

    /**
     * Teleports the user to the spawn location of the specified world.
     *
     * @param worldHolder The holder of the world to teleport to.
     */
    @Override
    public void teleport(@NotNull final WorldHolder worldHolder) {
        this.getEntity().teleport(BukkitWorldLocation.to(worldHolder.getSpawnLocation()), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    /**
     * Gets the Bukkit Player entity associated with this user.
     *
     * @return The Bukkit Player entity.
     */
    @Override
    public Player getEntity() {
        return Bukkit.getPlayer(this.uniqueId);
    }

}
