package com.dev7ex.multiworld.api;

import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 20.12.2022
 */
public class MultiWorldApiProvider {

    private static MultiWorldApi multiWorldApi;

    public static void registerApi(@NotNull final MultiWorldApi multiWorldApi) {
        MultiWorldApiProvider.multiWorldApi = multiWorldApi;
    }

    public static void unregisterApi() {
        MultiWorldApiProvider.multiWorldApi = null;
    }

    public static MultiWorldApi getMultiWorldApi() {
        return MultiWorldApiProvider.multiWorldApi;
    }

}
