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
 * PlaceholderExpansion for MultiWorld plugin to provide world-related placeholders.
 * This class extends PlaceholderExpansion from PlaceholderAPI.
 * It allows players to access information about different worlds.
 *
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

    /**
     * Constructs a new MultiWorldExpansion with the given MultiWorldBukkitApi.
     *
     * @param multiWorldApi The MultiWorldBukkitApi instance.
     */
    public MultiWorldExpansion(@NotNull final MultiWorldBukkitApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }

    /**
     * Registers the expansion with PlaceholderAPI.
     *
     * @return True if registration is successful, false otherwise.
     */
    @Override
    public boolean register() {
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return false;
        }
        return super.register();
    }

    /**
     * Handles placeholder requests.
     *
     * @param player    The player requesting the placeholder, or null if not applicable.
     * @param parameter The placeholder parameter.
     * @return The placeholder value.
     */
    @Override
    public @Nullable String onPlaceholderRequest(@Nullable final Player player, @NotNull final String parameter) {
        return this.onPlaceholderRequest(parameter);
    }

    /**
     * Handles placeholder requests.
     *
     * @param parameter The placeholder parameter.
     * @return The placeholder value.
     */
    @Nullable
    private String onPlaceholderRequest(@NotNull final String parameter) {
        final Optional<MultiWorldExpansionType> typeOptional = MultiWorldExpansionType.getByParameter(parameter);

        if (typeOptional.isEmpty()) {
            return null;
        }
        final MultiWorldExpansionType type = typeOptional.get();

        final Optional<BukkitWorldHolder> optional = this.multiWorldApi
                .getWorldProvider()
                .getWorldHolder(parameter.replaceAll(type.getParameter(), ""));

        if (optional.isEmpty()) {
            return "world-not-exists";
        }
        final BukkitWorldHolder worldHolder = optional.get();

        if ((type.isRequiresLoadedWorld()) && (!worldHolder.isLoaded())) {
            return "world-not-loaded";
        }

        switch (type) {
            case PLAYER_COUNT:
                return String.valueOf(worldHolder.getWorld().getEntities().stream().filter(entity -> entity.getType() == EntityType.PLAYER).count());

            case GAME_MODE:
                return worldHolder.getGameMode().name();

            case DIFFICULTY:
                return worldHolder.getDifficulty().name();

            case SPAWN_ANIMALS:
                return String.valueOf(worldHolder.isSpawnAnimals());

            case SPAWN_MONSTERS:
                return String.valueOf(worldHolder.isSpawnMonsters());

            case TYPE:
                return String.valueOf(worldHolder.getType());

            case PVP_ENABLED:
                return String.valueOf(worldHolder.isPvpEnabled());
        }
        return null;
    }
}
