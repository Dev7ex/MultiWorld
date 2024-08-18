package com.dev7ex.multiworld.hook.placeholder;

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
 * @since 19.08.2024
 */
@Getter(AccessLevel.PUBLIC)
public class PlaceholderHook extends PlaceholderExpansion {

    private final String identifier = "multiworld";
    private final String author = "Dev7ex";
    private final String version = "1.0.0";
    private final String requiredPlugin = "MultiWorld";

    private final MultiWorldBukkitApi multiWorldApi;

    /**
     * Constructs a new PlaceholderHook with the given MultiWorldBukkitApi.
     *
     * @param multiWorldApi The MultiWorldBukkitApi instance.
     */
    public PlaceholderHook(@NotNull final MultiWorldBukkitApi multiWorldApi) {
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
        final Optional<Type> typeOptional = Type.getByParameter(parameter);

        if (typeOptional.isEmpty()) {
            return null;
        }
        final Type type = typeOptional.get();

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

    @Getter(AccessLevel.PUBLIC)
    public enum Type {

        PLAYER_COUNT("_player_count", true),
        GAME_MODE("_game_mode", false),
        DIFFICULTY("_difficulty", false),
        SPAWN_ANIMALS("_spawn_animals", false),
        SPAWN_MONSTERS("_spawn_monsters", false),
        PVP_ENABLED("_pvp-enabled", false),
        TYPE("_type", false);

        private final String parameter;
        private final boolean requiresLoadedWorld;

        /**
         * Constructs a MultiWorldExpansionType with the given parameter and requirement for loaded world.
         *
         * @param parameter           The parameter associated with this type of placeholder.
         * @param requiresLoadedWorld Whether this type requires the world to be loaded.
         */
        Type(@NotNull final String parameter, final boolean requiresLoadedWorld) {
            this.parameter = parameter;
            this.requiresLoadedWorld = requiresLoadedWorld;
        }

        /**
         * Retrieves the MultiWorldExpansionType associated with the given parameter.
         *
         * @param parameter The parameter to search for.
         * @return An Optional containing the MultiWorldExpansionType if found, empty otherwise.
         */
        public static Optional<Type> getByParameter(@NotNull final String parameter) {
            for (final Type type : Type.values()) {
                if (parameter.contains(type.getParameter())) {
                    return Optional.of(type);
                }
            }
            return Optional.empty();
        }

    }

}
