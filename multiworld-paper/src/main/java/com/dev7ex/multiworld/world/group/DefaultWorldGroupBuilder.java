
package com.dev7ex.multiworld.world.group;

import com.dev7ex.multiworld.api.world.group.WorldGroup;
import com.dev7ex.multiworld.api.world.group.WorldGroupBuilder;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 16.10.2025
 */
@Setter(AccessLevel.PUBLIC)
@Accessors(chain = true)
public class DefaultWorldGroupBuilder implements WorldGroupBuilder {

    private int id = -1;
    private String name;
    private String displayName;
    private TextColor color = NamedTextColor.WHITE;
    private Material icon = Material.GRASS_BLOCK;
    private String creatorName;
    private long registrationTimestamp = System.currentTimeMillis();

    @Override
    public @NotNull WorldGroup build() {
        if (this.id < 0) {
            throw new IllegalStateException("World group ID must not be negative");
        }
        if ((this.name == null) || (this.name.isBlank())) {
            throw new IllegalStateException("World group name must not be blank");
        }
        if ((this.creatorName == null) || (this.creatorName.isBlank())) {
            throw new IllegalStateException("World group creator name must not be blank");
        }
        final String displayName = ((this.displayName == null) || (this.displayName.isBlank())) ? this.name : this.displayName;

        return new DefaultWorldGroup(this.id, this.name, displayName, this.color, this.icon, this.creatorName, this.registrationTimestamp);
    }

}
