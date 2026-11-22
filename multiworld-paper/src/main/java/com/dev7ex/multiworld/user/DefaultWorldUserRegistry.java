package com.dev7ex.multiworld.user;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.user.WorldUser;
import com.dev7ex.multiworld.api.user.WorldUserRegistry;
import com.dev7ex.multiworld.api.user.offline.WorldOfflineUser;
import com.dev7ex.multiworld.module.DefaultWorldModule;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dev7ex
 * @since 14.11.2026
 */
public class DefaultWorldUserRegistry extends DefaultWorldModule implements WorldUserRegistry {

    private final Map<UUID, WorldOfflineUser> users = new ConcurrentHashMap<>();

    public DefaultWorldUserRegistry(@NotNull final MultiWorldApi multiWorldApi) {
        super(multiWorldApi);
    }

    @Override
    public void onEnable() {
        if (super.getMultiWorldApi().getUserRepository() instanceof final DefaultWorldUserRepository repository) {
            repository.open();
        }
        Bukkit.getOnlinePlayers().forEach(player -> this.login(player.getUniqueId(), player.getName(), null));
    }

    @Override
    public void onDisable() {
        final long now = System.currentTimeMillis();
        final List<WorldUser> onlineUsers = this.users.values()
                .stream()
                .filter(user -> user instanceof WorldUser)
                .map(user -> (WorldUser) user)
                .toList();

        onlineUsers.forEach(user -> user.setLastLogin(now));
        super.getMultiWorldApi().getUserRepository().saveAll(onlineUsers);

        if (super.getMultiWorldApi().getUserRepository() instanceof final DefaultWorldUserRepository repository) {
            repository.close();
        }
        this.users.clear();
    }

    public void login(@NotNull final UUID uniqueId, @NotNull final String name, @Nullable final WorldUser storedUser) {
        final long now = System.currentTimeMillis();
        final long firstLogin = (storedUser == null) ? now : storedUser.getFirstLogin();
        final WorldUser user = super.getMultiWorldApi().getUserFactory().create(uniqueId, name, firstLogin, now);

        this.register(user);

        if ((storedUser != null) && (storedUser.getName().equals(name))) {
            return;
        }
        final CompletableFuture<Void> saved = super.getMultiWorldApi().getUserRepository().save(user);

        if (storedUser != null) {
            return;
        }
        saved.thenCompose(ignored -> super.getMultiWorldApi().getUserRepository().find(uniqueId))
                .thenAccept(found -> found.ifPresent(stored -> user.setFirstLogin(stored.getFirstLogin())));
    }

    public void logout(@NotNull final UUID uniqueId) {
        final WorldOfflineUser user = this.users.remove(uniqueId);

        if (!(user instanceof final WorldUser worldUser)) {
            return;
        }
        worldUser.setLastLogin(System.currentTimeMillis());
        super.getMultiWorldApi().getUserRepository().save(worldUser);
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
