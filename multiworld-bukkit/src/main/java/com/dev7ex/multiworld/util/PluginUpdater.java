package com.dev7ex.multiworld.util;

import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Dev7ex
 * @since 05.11.2024
 */
@Getter(AccessLevel.PUBLIC)
public class PluginUpdater {

    private final BukkitPlugin plugin;
    private volatile boolean updateAvailable = false;
    private volatile String newVersion;

    public PluginUpdater(@NotNull final BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    public void checkAsync() {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try {
                final String repoOwner = "Dev7ex";
                final String repoName = "MultiWorld";
                final String apiUrl = "https://api.github.com/repos/" + repoOwner + "/" + repoName + "/releases/latest";

                final HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try (final InputStream inputStream = connection.getInputStream();
                         final InputStreamReader reader = new InputStreamReader(inputStream)) {

                        final JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
                        this.newVersion = jsonObject.get("tag_name").getAsString();

                        final String currentVersion = this.plugin.getDescription().getVersion();
                        if (this.compareVersions(currentVersion, this.newVersion) == -1) {
                            this.updateAvailable = true;
                            this.plugin.getServer().getScheduler().runTask(this.plugin, this::logUpdateMessage);
                        }
                    }
                }
            } catch (final IOException exception) {
                this.updateAvailable = false;
                this.plugin.getServer().getScheduler().runTask(this.plugin, this::logNoUpdateMessage);
            }
        });
    }

    public int compareVersions(@NotNull final String currentVersion, @NotNull final String onlineVersion) {
        final String[] currentParts = currentVersion.replace("-SNAPSHOT", "").split("\\.");
        final String[] onlineParts = onlineVersion.replace("-SNAPSHOT", "").split("\\.");

        for (int i = 0; i < Math.max(currentParts.length, onlineParts.length); i++) {
            final int currentPart = (i < currentParts.length) ? Integer.parseInt(currentParts[i]) : 0;
            final int onlinePart = (i < onlineParts.length) ? Integer.parseInt(onlineParts[i]) : 0;

            if (currentPart < onlinePart) {
                return -1; // current version is older
            } else if (currentPart > onlinePart) {
                return 1; // current version is newer
            }
        }
        return 0; // versions are equal
    }

    public void logUpdateMessage() {
        this.plugin.getLogger().info("");
        this.plugin.getLogger().info("There is a new update for MultiWorld available on Modrinth!");
        this.plugin.getLogger().info("Please update to ensure full functionality.");
        this.plugin.getLogger().info("Current Version: " + this.plugin.getDescription().getVersion());
        this.plugin.getLogger().info("New Version: " + this.newVersion);
        this.plugin.getLogger().info("Download: https://modrinth.com/plugin/multiworld-bukkit");
        this.plugin.getLogger().info("");
    }

    public void logNoUpdateMessage() {
        this.plugin.getLogger().info("");
        this.plugin.getLogger().info("Your MultiWorld version is up to date!");
        this.plugin.getLogger().info("Version: " + this.plugin.getDescription().getVersion());
        this.plugin.getLogger().info("");
    }
}
