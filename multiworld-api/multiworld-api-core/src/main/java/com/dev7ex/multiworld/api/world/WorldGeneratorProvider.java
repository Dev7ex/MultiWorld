package com.dev7ex.multiworld.api.world;

import java.util.Map;

/**
 * @author Dev7ex
 * @since 29.06.2023
 */
public interface WorldGeneratorProvider {

    Map<?, String> getCustomGenerators();

    boolean exists(final String generator);

}
