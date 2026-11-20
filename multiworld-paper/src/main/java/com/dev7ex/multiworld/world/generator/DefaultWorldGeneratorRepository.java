package com.dev7ex.multiworld.world.generator;

import com.dev7ex.multicommon.api.configuration.Configuration;
import com.dev7ex.multicommon.api.configuration.ConfigurationHolder;
import com.dev7ex.multicommon.api.configuration.ConfigurationProperties;
import com.dev7ex.multicommon.api.configuration.JsonConfiguration;
import com.dev7ex.multiworld.api.world.generator.WorldGeneratorRepository;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 14.11.2026
 */
@ConfigurationProperties(fileName = "generator.json", provider = JsonConfiguration.class)
public class DefaultWorldGeneratorRepository extends Configuration implements WorldGeneratorRepository {

    public DefaultWorldGeneratorRepository(@NotNull final ConfigurationHolder configurationHolder) {
        super(configurationHolder);
    }

}
