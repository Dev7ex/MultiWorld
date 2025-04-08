package com.dev7ex.multiworld.hook.permission;

import com.dev7ex.multiperms.api.MultiPermsApiProvider;
import com.dev7ex.multiperms.api.bukkit.MultiPermsBukkitApi;
import com.dev7ex.multiperms.api.hook.PermissionHook;
import com.dev7ex.multiworld.MultiWorldPlugin;

import java.util.List;

/**
 * @author Dev7ex
 * @since 19.08.2024
 */
public class MultiPermsHook implements PermissionHook {

    private final MultiPermsBukkitApi multiPermsApi = (MultiPermsBukkitApi) MultiPermsApiProvider.getMultiPermsApi();

    @Override
    public List<String> getPermissions() {
        return List.of("multiworld.bypass.autogamemode",
                "multiworld.command.world",
                "multiworld.command.world.back",
                "multiworld.command.world.backup",
                "multiworld.command.world.clone",
                "multiworld.command.world.create",
                "multiworld.command.world.delete",
                "multiworld.command.world.flag",
                "multiworld.command.world.gamerule",
                "multiworld.command.world.help",
                "multiworld.command.world.import",
                "multiworld.command.world.info",
                "multiworld.command.world.link",
                "multiworld.command.world.list",
                "multiworld.command.world.load",
                "multiworld.command.world.reload",
                "multiworld.command.world.teleport",
                "multiworld.command.world.unload",
                "multiworld.command.world.version",
                "multiworld.command.world.whitelist",
                "multiworld.command.world.whitelist.add",
                "multiworld.command.world.whitelist.disable",
                "multiworld.command.world.whitelist.enable",
                "multiworld.command.world.whitelist.list",
                "multiworld.command.world.whitelist.remove");
    }

    @Override
    public void register() {
        this.multiPermsApi.getPermissionHookProvider()
                .register(MultiWorldPlugin.getInstance(), this);
    }

    @Override
    public void unregister() {
        this.multiPermsApi.getPermissionHookProvider()
                .unregister(MultiWorldPlugin.getInstance());
    }

}
