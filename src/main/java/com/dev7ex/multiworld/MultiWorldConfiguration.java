package com.dev7ex.multiworld;

import com.dev7ex.common.bukkit.command.BukkitCommand;
import com.dev7ex.common.bukkit.configuration.ConfigurationProperties;
import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.common.bukkit.plugin.configuration.DefaultPluginConfiguration;
import com.dev7ex.common.map.ParsedMap;
import com.dev7ex.multiworld.api.MultiWorldApiConfiguration;
import com.dev7ex.multiworld.world.WorldOption;
import lombok.SneakyThrows;

import java.util.List;

/**
 * @author Dev7ex
 * @since 19.05.2021
 */
@ConfigurationProperties(fileName = "config.yml")
public final class MultiWorldConfiguration extends DefaultPluginConfiguration implements MultiWorldApiConfiguration {

    private final ParsedMap<WorldOption, Object> worldDefaults = new ParsedMap<>();
    private final String messagePath = "messages.";

    public MultiWorldConfiguration(final BukkitPlugin bukkitPlugin) {
        super(bukkitPlugin);
    }

    @Override @SneakyThrows
    public void load() {
        super.load();

        super.getFileConfiguration().getConfigurationSection("defaults").getKeys(false).stream()
                .filter(entry -> !entry.equalsIgnoreCase("world")) // Ignore this
                .forEach(entry -> this.worldDefaults.put(WorldOption.valueOf(entry.replaceAll("-", "_").toUpperCase()), super.getFileConfiguration().get(entry)));

        this.loadChanges();
    }

    public void loadChanges() {
        if (!super.getFileConfiguration().contains("config-version")) {
            super.getFileConfiguration().set("config-version", "1.3.1-SNAPSHOT");
            super.saveFile();
        }
    }

    @Override
    public List<String> getAutoLoadableWorlds() {
        return super.getStringList("auto-load");
    }

    @Override
    public String getDefaultWorldName() {
        return super.getFileConfiguration().getString("defaults.world");
    }

    public String getCommandUsage(final BukkitCommand bukkitCommand) {
        return super.getString(this.messagePath + bukkitCommand.getName() + ".usage").replaceAll("%prefix%", super.getPrefix());
    }

    public String getMessage(final String path) {
        return this.getString(this.messagePath + path).replaceAll("%prefix%", super.getPrefix());
    }

}
