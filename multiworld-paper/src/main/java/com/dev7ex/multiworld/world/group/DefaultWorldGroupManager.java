package com.dev7ex.multiworld.world.group;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.event.world.group.WorldGroupCreateEvent;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.world.group.WorldGroup;
import com.dev7ex.multiworld.api.world.group.WorldGroupManager;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Dev7ex
 * @since 22.11.2026
 */
public class DefaultWorldGroupManager implements WorldGroupManager {

    private final MultiWorldApi multiWorldApi;

    public DefaultWorldGroupManager(@NotNull final MultiWorldApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }


    @Override
    public Optional<WorldGroup> create(@NotNull final Audience audience, int groupId, @NotNull String groupName, @NotNull String creatorName) {
        final WorldGroup worldGroup = this.multiWorldApi.getGroupFactory().create(groupId, groupName, creatorName);

        final WorldGroupCreateEvent event = new WorldGroupCreateEvent(worldGroup);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            if (event.isNotifyOnCancel()) {
                //
            }
            return Optional.empty();
        }

        return Optional.of(event.getGroup());
    }

    @Override
    public void delete(int id) {

    }

    private void sendMessage(@NotNull final Audience audience, @NotNull final WorldLocalizationEntry localizationEntry, @NotNull final WorldGroup group) {
        audience.sendMessage(this.multiWorldApi.getLocalizationRegistry().getComponent(audience, localizationEntry)
                .replaceText(builder -> builder.matchLiteral("").replacement(""))
        );
    }

}
