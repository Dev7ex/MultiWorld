
package com.dev7ex.multiworld.api.world.group;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 16.10.2025
 */
public interface WorldGroupBuilder {

    @NotNull WorldGroupBuilder setId(final int id);

    @NotNull WorldGroupBuilder setName(@NotNull final String name);

    @NotNull WorldGroupBuilder setDisplayName(@NotNull final String displayName);

    @NotNull WorldGroupBuilder setColor(@NotNull final TextColor color);

    @NotNull WorldGroupBuilder setIcon(@NotNull final Material icon);

    @NotNull WorldGroupBuilder setCreatorName(@NotNull final String creatorName);

    @NotNull WorldGroupBuilder setRegistrationTimestamp(final long registrationTimestamp);

    @NotNull WorldGroup build();

}
