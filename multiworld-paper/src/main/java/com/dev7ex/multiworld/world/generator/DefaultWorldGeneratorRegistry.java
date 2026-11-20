package com.dev7ex.multiworld.world.generator;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.world.generator.WorldGeneratorRegistry;
import com.dev7ex.multiworld.module.DefaultWorldModule;
import org.jetbrains.annotations.NotNull;

public class DefaultWorldGeneratorRegistry extends DefaultWorldModule implements WorldGeneratorRegistry {


    public DefaultWorldGeneratorRegistry(@NotNull MultiWorldApi multiWorldApi) {
        super(multiWorldApi);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
