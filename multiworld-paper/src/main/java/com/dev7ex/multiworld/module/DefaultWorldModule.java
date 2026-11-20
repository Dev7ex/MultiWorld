package com.dev7ex.multiworld.module;

import com.dev7ex.multiworld.api.MultiWorldApi;
import com.dev7ex.multiworld.api.module.WorldModule;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 13.04.2025
 */
@Getter(AccessLevel.PUBLIC)
public abstract class DefaultWorldModule implements WorldModule {

    protected final MultiWorldApi multiWorldApi;

    protected DefaultWorldModule(@NotNull final MultiWorldApi multiWorldApi) {
        this.multiWorldApi = multiWorldApi;
    }

}
