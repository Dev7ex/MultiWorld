package com.dev7ex.multiworld.api;

import com.dev7ex.multiworld.user.WorldUserService;
import com.dev7ex.multiworld.world.WorldManager;
import com.dev7ex.multiworld.world.WorldService;

/**
 * @author Dev7ex
 * @since 20.12.2022
 */
public interface MultiWorldApi {

    WorldService getWorldService();

    WorldManager getWorldManager();

    WorldUserService getWorldUserService();

}
