package com.dev7ex.multiworld.util;

import com.dev7ex.common.bukkit.plugin.BukkitPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

/**
 * @author Dev7ex
 * @since 05.11.2024
 */
@Getter(AccessLevel.PUBLIC)
public class PluginUpdater {

    private final BukkitPlugin plugin;
    private boolean updateAvailable = false;
    private String newVersion;

    public PluginUpdater(@NotNull final BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    public void checkAsync() {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (final InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.plugin.getPluginIdentification().spigotResourceId()).openStream()) {
                try (final Scanner scanner = new Scanner(inputStream)) {
                    final String currentVersion = this.plugin.getDescription().getVersion();
                    this.newVersion = scanner.next();

                    if (!currentVersion.equalsIgnoreCase(this.newVersion)) {
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

    /**
     * Compares two versions in the format x.y.z-SNAPSHOT.
     *
     * @param currentVersion The current version of the plugin.
     * @param onlineVersion  The latest available version online.
     * @return -1 if the current version is older than the online version,
     *          1 if the current version is newer,
     *          0 if both versions are equal.
     */
    public int compareVersions(@NotNull final String currentVersion, @NotNull final String onlineVersion) {
        final String[] currentParts = currentVersion.replace("-SNAPSHOT", "").split("\\.");
        final String[] onlineParts = onlineVersion.replace("-SNAPSHOT", "").split("\\.");

        for (int i = 0; i < Math.max(currentParts.length, onlineParts.length); i++) {
            int currentPart = (i < currentParts.length) ? Integer.parseInt(currentParts[i]) : 0;
            int onlinePart = (i < onlineParts.length) ? Integer.parseInt(onlineParts[i]) : 0;

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
        this.plugin.getLogger().info("There is a new update for MultiWorld");
        this.plugin.getLogger().info("Please note that if you do not update, some functions may be deactivated.");
        this.plugin.getLogger().info("Current Version: " + this.plugin.getDescription().getVersion());
        this.plugin.getLogger().info("New Version: " + this.newVersion);
        this.plugin.getLogger().info("");
    }

    public void logNoUpdateMessage() {
        this.plugin.getLogger().info("");
        this.plugin.getLogger().info("Your MultiWorld version is up to date!");
        this.plugin.getLogger().info("Version: " + this.plugin.getDescription().getVersion());
        this.plugin.getLogger().info("");
    }

}
