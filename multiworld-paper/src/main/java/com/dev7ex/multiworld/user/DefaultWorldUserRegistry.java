package com.dev7ex.multiworld.user;

import com.dev7ex.multicommon.api.util.Files;
import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.user.WorldUser;
import com.dev7ex.multiworld.api.user.WorldUserFactory;
import com.dev7ex.multiworld.api.user.WorldUserRegistry;
import com.dev7ex.multiworld.api.user.offline.WorldOfflineUser;
import com.dev7ex.multiworld.module.DefaultWorldModule;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dev7ex
 * @since 14.11.2026
 */
public class DefaultWorldUserRegistry extends DefaultWorldModule implements WorldUserRegistry {

    private final Map<UUID, WorldOfflineUser> users = new ConcurrentHashMap<>();
    private final WorldUserFactory userFactory;

    public DefaultWorldUserRegistry(@NotNull final MultiWorldApi multiWorldApi) {
        super(multiWorldApi);
        this.userFactory = multiWorldApi.getUserFactory();
    }

    @Override
    public void onEnable() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            final WorldUser worldUser = this.userFactory.create(player);


        });

        super.getMultiWorldApi().getLogger().info(() ->
                String.format("Found [%d] User Files", Files.getFiles(Objects.requireNonNull(super.getMultiWorldApi().getUserDirectory())).size()));
    }

    @Override
    public void onDisable() {
        this.users.clear();
    }

    @Override
    public void register(@NotNull final WorldOfflineUser user) {
        this.users.put(user.getUniqueId(), user);
    }

    @Override
    public void unregister(@NotNull final WorldOfflineUser user) {
        this.users.remove(user.getUniqueId());
    }

    @Override
    public void unregister(@NotNull final UUID uniqueId) {
        this.users.remove(uniqueId);
    }

    @Override
    public Optional<WorldOfflineUser> find(@NotNull final UUID uniqueId) {
        return Optional.ofNullable(this.users.get(uniqueId));
    }

    @Override
    public Optional<WorldOfflineUser> find(@NotNull final String name) {
        return this.users.values().stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst();
    }

}
