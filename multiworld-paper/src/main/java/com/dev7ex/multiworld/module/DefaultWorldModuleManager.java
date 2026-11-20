package com.dev7ex.multiworld.module;

import com.dev7ex.multiworld.api.module.WorldModule;
import com.dev7ex.multiworld.api.module.WorldModuleManager;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Dev7ex
 * @since 13.04.2025
 */
@Getter(AccessLevel.PUBLIC)
public class DefaultWorldModuleManager implements WorldModuleManager {

    private final LinkedList<WorldModule> registeredModules = new LinkedList<>();
    private final Map<UUID, WorldModule> modules = new HashMap<>();

    @Override
    public void register(@NotNull final WorldModule module) {
        this.registeredModules.add(module);
        this.modules.put(module.getUniqueId(), module);
    }

    @Override
    public void unregister(@NotNull final WorldModule module) {
        this.registeredModules.remove(module);
        this.modules.remove(module.getUniqueId());
    }

    @Override
    public void unregister(@NotNull final Class<? extends WorldModule> moduleClazz) {
        final Optional<WorldModule> moduleOptional = this.find(moduleClazz);

        if (moduleOptional.isEmpty()) {
            return;
        }
        this.unregister(moduleOptional.get());
    }

    @Override
    public Optional<WorldModule> find(@NotNull final Class<? extends WorldModule> moduleClazz) {
        return this.modules.values().stream()
                .filter(module -> module.getClass().equals(moduleClazz))
                .findFirst();
    }

    @Override
    public void disable(@NotNull final WorldModule module) {
        module.onDisable();
    }

    @Override
    public void disable(@NotNull final Class<? extends WorldModule> moduleClazz) {
        this.modules.values().stream()
                .filter(m -> m.getClass().equals(moduleClazz))
                .findFirst()
                .ifPresent(WorldModule::onDisable);
    }

    @Override
    public void enableAll() {
        while (!this.registeredModules.isEmpty()) {
            final WorldModule module = this.registeredModules.pollFirst();
            if (module != null) {
                module.onEnable();
            }
        }
    }

    @Override
    public void disableAll() {
        for (final WorldModule module : new ArrayList<>(this.modules.values())) {
            if (module != null) {
                module.onDisable();
            }
        }
        this.modules.clear();
    }

}
