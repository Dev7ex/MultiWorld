package com.dev7ex.multiworld.api.world;

import com.dev7ex.multiworld.api.world.group.WorldGroup;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface WorldContext extends WorldHolder, Comparable<WorldContext>, Cloneable {

    NamespacedKey getKey();

    void setKey(@NotNull final NamespacedKey key);

    String getName();

    void setName(@NotNull final String name);

    String getDisplayName();

    void setDisplayName(@NotNull final String displayName);

    TextColor getColor();

    void setColor(@NotNull final TextColor color);

    Component getColoredDisplayName();

    Material getIcon();

    void setIcon(final Material material);

    String getCreatorName();

    void setCreatorName(@NotNull final String creatorName);

    long getRegistrationTimestamp();

    void setRegistrationTimestamp(final long registrationTimestamp);

    int getGroupId();

    void setGroupId(final int id);

    WorldGroup getGroup();

    void setGroup(@NotNull final WorldGroup group);

    WorldDirectory getDirectory();

    void setDirectory(@NotNull final WorldDirectory directory);

    WorldEnvironment getEnvironment();

    void setEnvironment(@NotNull final WorldEnvironment environment);

    long getSeed();

    void setSeed(final long seed);

    WorldType getType();

    void setType(@NotNull final WorldType type);

    String getGeneratorName();

    void setGeneratorName(@NotNull final String generatorName);

    @Override
    @Nullable
    World getWorld();

    boolean isLoaded();

    @Override
    default int compareTo(@NotNull final WorldContext worldContext) {
        return this.getName().compareTo(worldContext.getName());
    }

    WorldContext clone();

}
