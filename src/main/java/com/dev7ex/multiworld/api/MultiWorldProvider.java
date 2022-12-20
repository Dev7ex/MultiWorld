package com.dev7ex.multiworld.api;

/**
 * @author Dev7ex
 * @since 20.12.2022
 */
public class MultiWorldProvider {

    private static MultiWorldApi multiWorldApi;

    public static void registerApi(final MultiWorldApi multiWorldApi) {
        MultiWorldProvider.multiWorldApi = multiWorldApi;
    }

    public static void unregisterApi() {
        MultiWorldProvider.multiWorldApi = null;
    }

    public static MultiWorldApi getMultiWorldApi() {
        return multiWorldApi;
    }

}
