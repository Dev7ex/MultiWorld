package com.dev7ex.multiworld.hook;

import com.dev7ex.common.bukkit.plugin.module.PluginModule;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.hook.permission.MultiPermsHook;
import org.bukkit.Bukkit;

/**
 * @author Dev7ex
 * @since 19.08.2024
 */
public class DefaultHookProvider implements PluginModule {

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("MultiPerms")) {
            new MultiPermsHook().register();
            MultiWorldPlugin.getInstance().getLogger().info("Successfully injected into MultiPerms");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new MultiPermsHook().register();
            MultiWorldPlugin.getInstance().getLogger().info("Successfully injected into PlaceholderAPI");
        }
    }

    @Override
    public void onDisable() {}

}
