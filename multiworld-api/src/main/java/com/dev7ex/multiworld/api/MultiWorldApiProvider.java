package com.dev7ex.multiworld.api;

import org.jetbrains.annotations.NotNull;

/**
 * Static access point for the {@link MultiWorldApi}.
 *
 * @author Dev7ex
 * @since 20.12.2022
 */
public final class MultiWorldApiProvider {

    private static volatile MultiWorldApi multiWorldApi;

    private MultiWorldApiProvider() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static synchronized void register(@NotNull final MultiWorldApi api) {
        if (multiWorldApi != null) {
            throw new IllegalStateException("MultiWorldApi is already registered");
        }
        multiWorldApi = api;
    }

    public static synchronized void unregister() {
        multiWorldApi = null;
    }

    @NotNull
    public static MultiWorldApi getMultiWorldApi() {
        final MultiWorldApi api = multiWorldApi;
        if (api == null) {
            throw new IllegalStateException("MultiWorldApi is not registered");
        }
        return api;
    }

    public static boolean isRegistered() {
        return multiWorldApi != null;
    }

}