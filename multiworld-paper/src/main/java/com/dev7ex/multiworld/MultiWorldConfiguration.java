package com.dev7ex.multiworld;

import com.dev7ex.multicommon.api.configuration.ConfigurationHolder;
import com.dev7ex.multicommon.api.configuration.ConfigurationProperties;
import com.dev7ex.multicommon.api.configuration.JsonConfiguration;
import com.dev7ex.multicommon.api.plugin.configuration.DefaultPluginConfiguration;
import com.dev7ex.multicommon.api.text.TextFormatter;
import com.dev7ex.multiworld.api.MultiWorldApiConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 20.12.2022
 */
@Getter(AccessLevel.PUBLIC)
@ConfigurationProperties(fileName = "config.json", provider = JsonConfiguration.class)
public class MultiWorldConfiguration extends DefaultPluginConfiguration implements MultiWorldApiConfiguration {

   // private final ParsedMap<WorldDefaultProperty, Object> defaultWorldProperties = new ParsedMap<>();

    public MultiWorldConfiguration(@NotNull final ConfigurationHolder configurationHolder) {
        super(configurationHolder);
    }

    @Override
    public void loadFile() {
        super.loadFile();

        /*super.getFileConfiguration().getSection("settings.defaults").getKeys()
                .forEach(entry -> this.defaultWorldProperties.put(WorldDefaultProperty.valueOf(entry.replace("-", "_").toUpperCase()),
                        super.getFileConfiguration().get("settings.defaults." + entry)));*/
    }

    @NotNull
    @Override
    public Component getPrefix() {
        return TextFormatter.parse(super.getFileConfiguration().getString("prefix"));
    }

}
