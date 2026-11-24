package com.dev7ex.multiworld.listener.player;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.event.MultiWorldListener;
import com.dev7ex.multiworld.api.user.WorldUser;
import com.dev7ex.multiworld.user.DefaultWorldUserRegistry;
import com.dev7ex.multiworld.user.DefaultWorldUserSkin;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dev7ex
 * @since 19.11.2026
 */
public class PlayerConnectionListener extends MultiWorldListener {

    private static final long LOAD_TIMEOUT_SECONDS = 3;
    private static final long PENDING_EXPIRE_NANOS = TimeUnit.SECONDS.toNanos(60);
    private static final long WARNING_INTERVAL_MILLIS = TimeUnit.MINUTES.toMillis(1);
    private static final long SWEEP_INTERVAL_NANOS = TimeUnit.SECONDS.toNanos(10);

    private final DefaultWorldUserRegistry userRegistry;
    private final Map<UUID, PendingUser> pendingUsers = new ConcurrentHashMap<>();
    private final AtomicLong lastWarning = new AtomicLong();
    private final AtomicLong lastSweep = new AtomicLong(System.nanoTime());

    public PlayerConnectionListener(@NotNull final MultiWorldApi multiWorldApi, @NotNull final DefaultWorldUserRegistry userRegistry) {
        super(multiWorldApi);
        this.userRegistry = userRegistry;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void handlePlayerPreLogin(final AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        final long now = System.nanoTime();
        final long previousSweep = this.lastSweep.get();

        if (((now - previousSweep) > PlayerConnectionListener.SWEEP_INTERVAL_NANOS) && (this.lastSweep.compareAndSet(previousSweep, now))) {
            this.pendingUsers.values().removeIf(pendingUser -> (now - pendingUser.getCreatedNanos()) > PlayerConnectionListener.PENDING_EXPIRE_NANOS);
        }
        final CompletableFuture<Optional<WorldUser>> future = this.multiWorldApi.getUserRepository().find(event.getUniqueId());

        try {
            final Optional<WorldUser> storedUser = future.get(PlayerConnectionListener.LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            storedUser.ifPresent(user -> this.pendingUsers.put(event.getUniqueId(), new PendingUser(user, now)));

        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();

        } catch (final ExecutionException | TimeoutException exception) {
            future.cancel(false);
            final long millis = System.currentTimeMillis();
            final long previous = this.lastWarning.get();

            if (((millis - previous) >= PlayerConnectionListener.WARNING_INTERVAL_MILLIS) && (this.lastWarning.compareAndSet(previous, millis))) {
                this.multiWorldApi.getLogger().warning(String.format("Can't load the data of %s in time, the player joins with fresh data", event.getName()));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void handlePlayerLogin(final PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            return;
        }
        this.pendingUsers.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handlePlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final PendingUser pendingUser = this.pendingUsers.remove(player.getUniqueId());

        this.userRegistry.login(player.getUniqueId(), player.getName(), DefaultWorldUserSkin.fromProfile(player.getPlayerProfile()), (pendingUser == null) ? null : pendingUser.getUser());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void handlePlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final Player current = Bukkit.getPlayer(player.getUniqueId());

        if ((current != null) && (current != player)) {
            return;
        }
        this.pendingUsers.remove(player.getUniqueId());
        this.userRegistry.logout(player.getUniqueId(), player.getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handlePlayerTeleport(final PlayerTeleportEvent event) {
        this.changeWorld(event.getPlayer(), event.getFrom(), event.getTo());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handlePlayerPortal(final PlayerPortalEvent event) {
        this.changeWorld(event.getPlayer(), event.getFrom(), event.getTo());
    }

    private void changeWorld(@NotNull final Player player, @NotNull final Location from, @Nullable final Location to) {
        if ((to == null) || (from.getWorld() == null) || (from.getWorld().equals(to.getWorld()))) {
            return;
        }
        this.userRegistry.changeWorld(player.getUniqueId(), from);
    }

    @Getter(AccessLevel.PRIVATE)
    private static class PendingUser {

        private final WorldUser user;
        private final long createdNanos;

        private PendingUser(@NotNull final WorldUser user, final long createdNanos) {
            this.user = user;
            this.createdNanos = createdNanos;
        }

    }

}
