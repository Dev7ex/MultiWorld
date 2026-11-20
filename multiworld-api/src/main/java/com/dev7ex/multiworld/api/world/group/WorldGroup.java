package com.dev7ex.multiworld.api.world.group;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 09.07.2025
 */
public interface WorldGroup extends Comparable<WorldGroup> {

    int getId();

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

    @Override
    default int compareTo(@NotNull final WorldGroup group) {
        return Integer.compare(this.getId(), group.getId());
    }

}
