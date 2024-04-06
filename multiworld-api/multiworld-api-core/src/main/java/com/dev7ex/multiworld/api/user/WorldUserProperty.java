package com.dev7ex.multiworld.api.user;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldUserProperty {

    UNIQUE_ID("unique-id"),
    NAME("name"),
    LAST_LOCATION("last-location"),
    LAST_LOGIN("last-login");

    private final String storagePath;

    WorldUserProperty(@NotNull final String storagePath) {
        this.storagePath = storagePath;
    }

}
