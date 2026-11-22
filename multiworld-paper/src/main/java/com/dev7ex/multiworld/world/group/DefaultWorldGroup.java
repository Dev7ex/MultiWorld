
package com.dev7ex.multiworld.world.group;

import com.dev7ex.multiworld.api.world.group.WorldGroup;
import lombok.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

/**
 * @author Dev7ex
 * @since 09.07.2025
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class DefaultWorldGroup implements WorldGroup {

    @EqualsAndHashCode.Include
    @ToString.Include
    private int id;

    @ToString.Include
    private String name;

    private String displayName;
    private TextColor color;
    private Material icon;
    private String creatorName;
    private long registrationTimestamp;

    @Override
    public Component getColoredDisplayName() {
        return Component.text(this.displayName, this.color);
    }

}
