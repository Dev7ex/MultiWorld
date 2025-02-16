package com.dev7ex.multiworld.util;

import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
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

    private final MultiWorldPlugin plugin;
    private volatile boolean updateAvailable = false;
    private volatile String newVersion;

    public PluginUpdater(@NotNull final MultiWorldPlugin plugin) {
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

                        } else if (this.compareVersions(currentVersion, this.newVersion) == 1) {
                            this.plugin.getServer().getScheduler().runTask(this.plugin, this::logNoUpdateMessage);
                        }
                    }
                }
            } catch (final IOException exception) {
                this.updateAvailable = false;
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
        final CommandSender commandSender = Bukkit.getConsoleSender();
        final String prefix = this.plugin.getConfiguration().getPrefix();

        commandSender.sendMessage("§7There is a new update for MultiWorld available on §aModrinth");
        commandSender.sendMessage("§cPlease update to ensure full functionality");
        commandSender.sendMessage("§7Current Version: §c" + this.plugin.getDescription().getVersion());
        commandSender.sendMessage("§7New Version: §a" + this.newVersion);
        commandSender.sendMessage("§7Download: §a" + this.plugin.getDescription().getWebsite());
    }

    public void logNoUpdateMessage() {
        final CommandSender commandSender = Bukkit.getConsoleSender();
        final String prefix = this.plugin.getConfiguration().getPrefix();

        commandSender.sendMessage(prefix + " §7Your §bMultiWorld §7version is up to date");
        commandSender.sendMessage(prefix + " §7Version: §a" + this.plugin.getDescription().getVersion());
        commandSender.sendMessage(prefix + " §7URL: §a" + this.plugin.getDescription().getWebsite());
    }
}
