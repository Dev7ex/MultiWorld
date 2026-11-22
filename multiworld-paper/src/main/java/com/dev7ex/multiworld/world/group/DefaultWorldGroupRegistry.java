package com.dev7ex.multiworld.world.group;

import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.world.group.WorldGroup;
import com.dev7ex.multiworld.api.world.group.WorldGroupRegistry;
import com.dev7ex.multiworld.api.world.group.WorldGroupRepository;
import com.dev7ex.multiworld.module.DefaultWorldModule;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Dev7ex
 * @since 09.07.2025
 */
@Getter(AccessLevel.PUBLIC)
public class DefaultWorldGroupRegistry extends DefaultWorldModule implements WorldGroupRegistry {

    private final Map<Integer, WorldGroup> groups = new HashMap<>();
    private final WorldGroupRepository repository;
    private WorldGroup defaultGroup;

    public DefaultWorldGroupRegistry(@NotNull final MultiWorldApi multiWorldApi) {
        super(multiWorldApi);
        this.repository = multiWorldApi.getGroupRepository();
    }

    @Override
    public void onEnable() {
        this.groups.putAll(this.repository.findAll());

        if (!this.groups.containsKey(0)) {
            this.defaultGroup = MultiWorldPlugin.getInstance().getGroupFactory().builder()
                    .setId(0)
                    .setName("default")
                    .setDisplayName("Default")
                    .setColor(TextColor.fromHexString("#3F51B5"))
                    .setIcon(Material.ENDER_EYE)
                    .setCreatorName(Bukkit.getConsoleSender().getName())
                    .setRegistrationTimestamp(System.currentTimeMillis())
                    .build();
            this.repository.insert(this.defaultGroup);
            this.groups.put(0, this.defaultGroup);

        } else {
            this.defaultGroup = this.groups.get(0);
        }

        super.getMultiWorldApi().getLogger().info(
                "Registered %d world group(s).".formatted(this.groups.size())
        );
    }

    @Override
    public void onDisable() {
        this.groups.clear();
    }

    @Override
    public void register(@NotNull final WorldGroup group) {
        this.groups.put(group.getId(), group);
    }

    @Override
    public void unregister(@NotNull final WorldGroup group) {
        this.groups.remove(group.getId());
    }

    @Override
    public void unregister(final int id) {
        this.groups.remove(id);
    }

    @Override
    public @NotNull Optional<WorldGroup> find(final int id) {
        return Optional.ofNullable(this.groups.get(id));
    }

    @Override
    public @NotNull Optional<WorldGroup> find(@NotNull final String groupName) {
        return this.groups.values()
                .stream()
                .filter(group -> group.getName().equalsIgnoreCase(groupName))
                .findFirst();
    }

    @Override
    public @NotNull List<WorldGroup> getGroups() {
        return List.copyOf(this.groups.values());
    }

}
