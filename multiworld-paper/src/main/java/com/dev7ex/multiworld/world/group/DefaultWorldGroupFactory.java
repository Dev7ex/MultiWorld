package com.dev7ex.multiworld.world.group;

import com.dev7ex.multiworld.api.world.group.WorldGroup;
import com.dev7ex.multiworld.api.world.group.WorldGroupBuilder;
import com.dev7ex.multiworld.api.world.group.WorldGroupFactory;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 16.10.2025
 */
@Getter(AccessLevel.PUBLIC)
public class DefaultWorldGroupFactory implements WorldGroupFactory {

    @Override
    public @NotNull WorldGroup create(final int id, @NotNull final String groupName, @NotNull final String creatorName) {
        if (groupName.isBlank()) {
            throw new IllegalArgumentException("Group name must not be blank"); // TODO: LOG REPORT SYSTEM INTEGRATION
        }
        return this.builder()
                .setId(id)
                .setName(groupName)
                .setDisplayName(Character.toUpperCase(groupName.charAt(0)) + groupName.substring(1))
                .setColor(NamedTextColor.WHITE)
                .setIcon(Material.GRASS_BLOCK)
                .setCreatorName(creatorName)
                .setRegistrationTimestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public @NotNull WorldGroupBuilder builder() {
        return new DefaultWorldGroupBuilder();
    }

}
