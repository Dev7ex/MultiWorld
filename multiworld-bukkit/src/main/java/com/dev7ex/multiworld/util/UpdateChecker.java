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
 * @since 01.07.2023
 */
@Getter(AccessLevel.PUBLIC)
@Deprecated
public class UpdateChecker {

    private final BukkitPlugin plugin;
    private boolean updateAvailable = false;
    private String newVersion;

    public UpdateChecker(@NotNull final BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    public void getVersion(@NotNull final Consumer<Boolean> consumer) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (final InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.plugin.getPluginIdentification().spigotResourceId()).openStream()) {
                try (final Scanner scanner = new Scanner(inputStream)) {
                    final String currentVersion = this.plugin.getDescription().getVersion();
                    this.newVersion = scanner.next();

                    if (!currentVersion.equalsIgnoreCase(this.newVersion)) {
                        this.updateAvailable = true;
                        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
                            this.plugin.getLogger().info("There is a new update available.");
                            this.plugin.getLogger().info("Current Version: " + currentVersion);
                            this.plugin.getLogger().info("New Version: " + this.newVersion);
                        });
                        return;
                    }
                    consumer.accept(this.updateAvailable);
                }

            } catch (final IOException exception) {
                this.updateAvailable = false;
                consumer.accept(false);
                this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
                    this.plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
                });
            }
        });
    }

}
