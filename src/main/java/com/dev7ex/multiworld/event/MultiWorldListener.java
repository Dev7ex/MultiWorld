package com.dev7ex.multiworld.event;

import com.dev7ex.multiworld.MultiWorldConfiguration;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.user.WorldUser;
import com.dev7ex.multiworld.user.WorldUserService;
import com.dev7ex.multiworld.world.WorldManager;

import org.bukkit.event.Listener;

import java.util.UUID;

/**
 * @author Dev7ex
 * @since 20.05.2021
 */
public abstract class MultiWorldListener implements Listener {

    protected final MultiWorldPlugin multiWorldPlugin;

    protected MultiWorldListener(final MultiWorldPlugin multiWorldPlugin) {
        this.multiWorldPlugin = multiWorldPlugin;
    }

    protected final WorldUser getWorldUser(final UUID uniqueId) {
        return this.getWorldUserService().getUsers().get(uniqueId);
    }

    protected final MultiWorldConfiguration getConfiguration() {
        return this.multiWorldPlugin.getConfiguration();
    }

    protected final WorldManager getWorldManager() {
        return this.multiWorldPlugin.getWorldManager();
    }

    protected final WorldUserService getWorldUserService() {
        return this.multiWorldPlugin.getWorldUserService();
    }

}
