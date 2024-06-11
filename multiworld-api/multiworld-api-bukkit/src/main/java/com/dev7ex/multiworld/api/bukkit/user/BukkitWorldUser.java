package com.dev7ex.multiworld.api.bukkit.user;

import com.dev7ex.common.bukkit.entity.EntityResolver;
import com.dev7ex.multiworld.api.user.WorldUser;
import org.bukkit.entity.Player;

/**
 * @author Dev7ex
 * @since 07.06.2024
 */
public interface BukkitWorldUser extends WorldUser, EntityResolver<Player> {
}
