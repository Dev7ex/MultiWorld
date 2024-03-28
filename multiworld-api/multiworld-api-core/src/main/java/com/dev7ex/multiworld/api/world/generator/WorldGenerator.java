package com.dev7ex.multiworld.api.world.generator;

/**
 * @author Dev7ex
 * @since 26.03.2024
 */
public interface WorldGenerator {

    default String getName() {
        return this.getClass().getSimpleName();
    }

}
