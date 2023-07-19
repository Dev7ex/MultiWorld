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
                return String.valueOf(worldHolder.getWorld().getEntities().stream().filter(entity -> entity.getType() == EntityType.PLAYER).toList().size());

            case GAME_MODE:
                return worldHolder.getGameMode().name();

            case DIFFICULTY:
                return worldHolder.getDifficulty().name();

            case SPAWN_ANIMALS:
                return String.valueOf(optional.get().isSpawnAnimals());

            case SPAWN_MONSTERS:
                return String.valueOf(optional.get().isSpawnMonsters());

            case TYPE:
                return String.valueOf(optional.get().getType().toString());

            case PVP_ENABLED:
                return String.valueOf(optional.get().isPvpEnabled());
        }
        return null;
    }

}
