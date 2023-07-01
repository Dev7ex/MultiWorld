package com.dev7ex.multiworld.user;

import com.dev7ex.common.bukkit.entity.EntityResolver;
import com.dev7ex.multiworld.api.user.WorldUser;
import com.dev7ex.multiworld.api.user.WorldUserConfiguration;
import com.dev7ex.multiworld.api.world.location.WorldLocation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class User implements WorldUser, EntityResolver<Player> {

    private final UUID uniqueId;
    private final String name;
    private WorldUserConfiguration configuration;
    private WorldLocation lastLocation;

    public User(@NotNull final UUID uniqueId, @NotNull final String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }

    @Override
    public void sendMessage(@NotNull final String message) {
        this.getEntity().sendMessage(message);
    }

    @Override
    public Player getEntity() {
        return Bukkit.getPlayer(this.uniqueId);
    }


}
