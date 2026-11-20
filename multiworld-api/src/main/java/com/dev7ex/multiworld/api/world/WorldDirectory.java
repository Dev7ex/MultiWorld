package com.dev7ex.multiworld.api.world;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serial;

/**
 * @author Dev7ex
 * @since 25.05.2025
 */
@Getter(AccessLevel.PUBLIC)
public class WorldDirectory extends File {

    @Serial
    private static final long serialVersionUID = 411210630915638932L;

    public WorldDirectory(@NotNull final String worldName) {
        super(Bukkit.getWorldContainer().toPath()
                .resolve(worldName)
                .toFile()
                .getPath());
    }

}
