package com.dev7ex.multiworld.world;

import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.WorldDirectory;
import com.dev7ex.multiworld.api.world.WorldEnvironment;
import com.dev7ex.multiworld.api.world.WorldType;
import com.dev7ex.multiworld.api.world.group.WorldGroup;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;

/**
 * @author Dev7ex
 * @since 19.10.2025
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Builder(setterPrefix = "set")
public class DefaultWorldContext implements WorldContext {

    private NamespacedKey key;
    private String name;
    private String displayName;
    private TextColor color;
    private Material icon;
    private String creatorName;
    private long registrationTimestamp;
    private int groupId;
    private WorldGroup group;
    private WorldDirectory directory;

    private WorldEnvironment environment;
    private WorldType type;
    private long seed;
    private String generatorName;

    @Override
    public Component getColoredDisplayName() {
        return Component.text(this.displayName).color(this.color);
    }

    @Override
    public World getWorld() {
        return Bukkit.getWorld(this.name);
    }

    @Override
    public boolean isLoaded() {
        return Bukkit.getWorld(this.name) != null;
    }

    @Override
    public DefaultWorldContext clone() {
        try {
            return (DefaultWorldContext) super.clone();

        } catch (final CloneNotSupportedException exception) {
            throw new AssertionError();
        }
    }

}
