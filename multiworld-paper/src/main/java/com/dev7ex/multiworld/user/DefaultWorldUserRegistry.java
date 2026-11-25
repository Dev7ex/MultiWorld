package com.dev7ex.multiworld.user;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.user.WorldUser;
import com.dev7ex.multiworld.api.user.WorldUserRegistry;
import com.dev7ex.multiworld.api.user.WorldUserSkin;
import com.dev7ex.multiworld.api.user.offline.WorldOfflineUser;
import com.dev7ex.multiworld.module.DefaultWorldModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
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
        Bukkit.getOnlinePlayers().forEach(player -> this.login(player.getUniqueId(), player.getName(), DefaultWorldUserSkin.fromProfile(player.getPlayerProfile()), null));
    }

    @Override
    public void onDisable() {
        final long now = System.currentTimeMillis();
        final List<WorldUser> onlineUsers = this.users.values()
                .stream()
                .filter(WorldUser.class::isInstance)
                .map(WorldUser.class::cast)
                .toList();

        onlineUsers.forEach(user -> {
            user.setLastLogin(now);

            if (super.getMultiWorldApi().getScheduler().isRegionized()) {
                return;
            }
            final Player player = Bukkit.getPlayer(user.getUniqueId());

            if (player != null) {
                user.setLastLocation(player.getLocation());
            }
        });
        super.getMultiWorldApi().getUserRepository().saveAll(onlineUsers);

        if (super.getMultiWorldApi().getUserRepository() instanceof final DefaultWorldUserRepository repository) {
            repository.close();
        }
        this.users.clear();
    }

    public void login(@NotNull final UUID uniqueId, @NotNull final String name, @Nullable final WorldUserSkin skin, @Nullable final WorldUser storedUser) {
        final long now = System.currentTimeMillis();
        final long firstLogin = (storedUser == null) ? now : storedUser.getFirstLogin();
        final WorldUser user = super.getMultiWorldApi().getUserFactory().create(uniqueId, name, firstLogin, now);

        final WorldUserSkin storedSkin = (storedUser == null) ? null : storedUser.getSkin();
        user.setSkin((skin == null) ? storedSkin : skin);

        if (storedUser != null) {
            storedUser.getLastLocations().values().forEach(user::setLastLocation);
        }
        this.register(user);

        if ((storedUser != null) && (storedUser.getName().equals(name)) && (!this.isSkinChanged(storedSkin, skin))) {
            return;
        }
        final CompletableFuture<Void> saved = super.getMultiWorldApi().getUserRepository().save(user);

        if (storedUser != null) {
            return;
        }
        saved.thenCompose(ignored -> super.getMultiWorldApi().getUserRepository().find(uniqueId))
                .thenAccept(found -> found.ifPresent(stored -> {
                    user.setFirstLogin(stored.getFirstLogin());

                    if (user.getSkin() == null) {
                        user.setSkin(stored.getSkin());
                    }
                    stored.getLastLocations().values().stream()
                            .filter(location -> !user.getLastLocations().containsKey(location.getWorldKey()))
                            .forEach(user::setLastLocation);
                }));
    }

    private boolean isSkinChanged(@Nullable final WorldUserSkin storedSkin, @Nullable final WorldUserSkin skin) {
        if (skin == null) {
            return false;
        }

        if (storedSkin == null) {
            return true;
        }
        return (!skin.getValue().equals(storedSkin.getValue())) || (!Objects.equals(skin.getSignature(), storedSkin.getSignature()));
    }

    public void logout(@NotNull final UUID uniqueId, @Nullable final Location location) {
        final WorldOfflineUser user = this.users.remove(uniqueId);

        if (!(user instanceof final WorldUser worldUser)) {
            return;
        }

        worldUser.setLastLogin(System.currentTimeMillis());
        super.getMultiWorldApi().getUserRepository().save(worldUser);

        if ((location != null) && (location.getWorld() != null)) {
            worldUser.setLastLocation(location);
            Optional.ofNullable(worldUser.getLastLocations().get(location.getWorld().getKey()))
                    .ifPresent(lastLocation -> super.getMultiWorldApi().getUserRepository().saveLocation(uniqueId, lastLocation));
        }
    }

    public void changeWorld(@NotNull final UUID uniqueId, @NotNull final Location from) {
        if (!(this.users.get(uniqueId) instanceof final WorldUser worldUser)) {
            return;
        }
        if (from.getWorld() == null) {
            return;
        }
        worldUser.setLastLocation(from);
        Optional.ofNullable(worldUser.getLastLocations().get(from.getWorld().getKey()))
                .ifPresent(location -> super.getMultiWorldApi().getUserRepository().saveLocation(uniqueId, location));
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
