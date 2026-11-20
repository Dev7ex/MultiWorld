package com.dev7ex.multiworld.api.world;

import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldHolder {

    @Nullable
    World getWorld();

}
