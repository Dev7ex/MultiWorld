package com.dev7ex.multiworld.api.bukkit.expansion;

import com.dev7ex.multiworld.api.bukkit.MultiWorldBukkitApi;
import com.dev7ex.multiworld.api.bukkit.world.BukkitWorldHolder;
import lombok.AccessLevel;
import lombok.Getter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author Dev7ex
 * @since 02.07.2023
 */
@Getter(AccessLevel.PUBLIC)
public class MultiWorldExpansion extends PlaceholderExpansion {

    private final String identifier = "multiworld";
    private final String author = "Dev7ex";
    private final String version = "1.0.0";
    private final String requiredPlugin = "MultiWorld";

    private final MultiWorldBukkitApi multiWorldApi;

    public MultiWorldExpansion(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }

    @Override
    public boolean register() {
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return false;
        }
        return super.register();
    }

    @Override
    public @Nullable String onPlaceholderRequest(@Nullable final Player player, @NotNull final String parameter) {
        return this.onPlaceholderRequest(parameter);
    }

    @Nullable
    private String onPlaceholderRequest(@NotNull final String parameter) {
        if (parameter.contains("_player_count")) {
            final Optional<BukkitWorldHolder> optional = this.multiWorldApi
                    .getWorldProvider()
                    .getWorldHolder(parameter.replaceAll("_player_count", ""));

            if (optional.isEmpty()) {
                return "world-not-exists";
            }

            if (!optional.get().isLoaded()) {
                return "world-not-loaded";
            }
            return String.valueOf(optional.get().getWorld().getEntities().stream().filter(entity -> entity.getType() == EntityType.PLAYER).toList().size());
        }

        if (parameter.contains("_game_mode")) {
            final Optional<BukkitWorldHolder> optional = this.multiWorldApi
                    .getWorldProvider()
                    .getWorldHolder(parameter.replaceAll("_game_mode", ""));

            if (optional.isEmpty()) {
                return "world-not-exists";
            }
            return optional.get().getGameMode().name();
        }

        if (parameter.contains("_difficulty")) {
            final Optional<BukkitWorldHolder> optional = this.multiWorldApi
                    .getWorldProvider()
                    .getWorldHolder(parameter.replaceAll("_difficulty", ""));

            if (optional.isEmpty()) {
                return "world-not-exists";
            }
            return optional.get().getDifficulty().name();
        }

        if (parameter.contains("_spawn_monsters")) {
            final Optional<BukkitWorldHolder> optional = this.multiWorldApi
                    .getWorldProvider()
                    .getWorldHolder(parameter.replaceAll("_spawn_monsters", ""));

            if (optional.isEmpty()) {
                return "world-not-exists";
            }
            return String.valueOf(optional.get().isSpawnMonsters());
        }

        if (parameter.contains("_spawn_animals")) {
            final Optional<BukkitWorldHolder> optional = this.multiWorldApi
                    .getWorldProvider()
                    .getWorldHolder(parameter.replaceAll("_spawn_animals", ""));

            if (optional.isEmpty()) {
                return "world-not-exists";
            }
            return String.valueOf(optional.get().isSpawnAnimals());
        }

        if (parameter.contains("_pvp-enabled")) {
            final Optional<BukkitWorldHolder> optional = this.multiWorldApi
                    .getWorldProvider()
                    .getWorldHolder(parameter.replaceAll("_pvp-enabled", ""));

            if (optional.isEmpty()) {
                return "world-not-exists";
            }
            return String.valueOf(optional.get().isPvpEnabled());
        }

        if (parameter.contains("_type")) {
            final Optional<BukkitWorldHolder> optional = this.multiWorldApi
                    .getWorldProvider()
                    .getWorldHolder(parameter.replaceAll("_type", ""));

            if (optional.isEmpty()) {
                return "world-not-exists";
            }
            return String.valueOf(optional.get().getType().toString());
        }

        return null;
    }

}
