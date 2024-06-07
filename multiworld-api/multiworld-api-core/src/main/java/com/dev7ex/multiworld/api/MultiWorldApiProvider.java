package com.dev7ex.multiworld.api;

import org.jetbrains.annotations.NotNull;

/**
 * Acts as a gateway for obtaining an instance of the MultiWorldApi,
 * allowing users to manage various aspects across different worlds.
 *
 * @author Dev7ex
 * @since 20.12.2022
 */
public class MultiWorldApiProvider {

    private static MultiWorldApi multiWorldApi;

    /**
     * Registers the MultiWorldApi instance.
     *
     * @param multiWorldApi The MultiWorldApi instance to register.
     */
    public static void registerApi(@NotNull final MultiWorldApi multiWorldApi) {
        MultiWorldApiProvider.multiWorldApi = multiWorldApi;
    }

    /**
     * Unregisters the MultiWorldApi instance.
     */
    public static void unregisterApi() {
        MultiWorldApiProvider.multiWorldApi = null;
    }

    /**
     * Retrieves the registered MultiWorldApi instance.
     *
     * @return The registered MultiWorldApi instance.
     */
    public static MultiWorldApi getMultiWorldApi() {
        return MultiWorldApiProvider.multiWorldApi;
    }

}