package com.dev7ex.multiworld.api.world;

import com.dev7ex.common.collect.map.ParsedMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Represents a configuration for a world.
 * This interface provides methods for adding, removing, and retrieving properties of a world.
 *
 * @param <T> The type of WorldHolder associated with this configuration.
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldConfiguration<T extends WorldHolder> {

    /**
     * Adds the specified world holder to this configuration.
     *
     * @param worldHolder The world holder to add.
     */
    void add(@NotNull final T worldHolder);

    /**
     * Removes the specified world holder from this configuration.
     *
     * @param worldHolder The world holder to remove.
     */
    void remove(@NotNull final T worldHolder);

    /**
     * Removes the world with the specified name from this configuration.
     *
     * @param name The name of the world to remove.
     */
    void remove(@NotNull final String name);

    /**
     * Checks if this configuration contains the world with the specified name.
     *
     * @param name The name of the world to check.
     * @return true if this configuration contains the world, otherwise false.
     */
    boolean contains(@NotNull final String name);

    /**
     * Gets the value of the specified property for the given world holder.
     *
     * @param worldHolder The world holder.
     * @param property    The property to retrieve.
     * @return The value of the property.
     */
    Object getValue(@NotNull final WorldHolder worldHolder, @NotNull final WorldProperty property);

    /**
     * Checks if the specified world has the given property.
     *
     * @param name     The name of the world.
     * @param property The property to check.
     * @return true if the world has the property, otherwise false.
     */
    boolean hasProperty(@NotNull final String name, @NotNull final WorldProperty property);

    /**
     * Adds the missing property for the specified world holder.
     *
     * @param worldHolder The world holder.
     * @param property    The missing property to add.
     */
    void addMissingProperty(@NotNull final T worldHolder, @NotNull final WorldProperty property);

    /**
     * Updates the flag for the specified world holder.
     *
     * @param worldHolder The world holder.
     * @param worldFlag   The flag to update.
     * @param value       The new value of the flag.
     */
    void updateFlag(@NotNull final T worldHolder, @NotNull final WorldFlag worldFlag, @NotNull final String value);

    /**
     * Writes the specified world data to the configuration.
     *
     * @param worldHolder The world holder.
     * @param worldData   The world data to write.
     */
    void write(@NotNull final T worldHolder, @NotNull final ParsedMap<WorldProperty, Object> worldData);

    /**
     * Writes the specified data for the given property to the configuration.
     *
     * @param worldHolder The world holder.
     * @param property    The property.
     * @param data        The data to write.
     */
    void write(@NotNull final T worldHolder, @NotNull final WorldProperty property, @NotNull final Object data);

    /**
     * Gets the world holder with the specified name.
     *
     * @param name The name of the world.
     * @return The world holder, or null if not found.
     */
    T getWorldHolder(@NotNull final String name);

    /**
     * Gets a map of all world holders in this configuration, where the key is the name of the world.
     *
     * @return A map of world holders.
     */
    Map<String, T> getWorldHolders();

}