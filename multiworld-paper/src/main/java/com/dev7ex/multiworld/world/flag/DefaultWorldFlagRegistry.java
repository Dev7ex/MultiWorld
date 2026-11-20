package com.dev7ex.multiworld.world.flag;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.world.flag.WorldFlagRegistry;
import com.dev7ex.multiworld.module.DefaultWorldModule;
import org.jetbrains.annotations.NotNull;

public class DefaultWorldFlagRegistry extends DefaultWorldModule implements WorldFlagRegistry {
    public DefaultWorldFlagRegistry(@NotNull MultiWorldApi multiWorldApi) {
        super(multiWorldApi);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
