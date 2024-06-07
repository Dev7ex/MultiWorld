package com.dev7ex.multiworld.api.user;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Represents properties associated with a user in the multi-world system.
 * These properties include unique ID, name, last location, and last login time.
 *
 * @author Dev7ex
 * @since 18.06.2023
 */
@Getter(AccessLevel.PUBLIC)
public enum WorldUserProperty {

    /**
     * The unique ID property of the user.
     */
    UNIQUE_ID("unique-id"),

    /**
     * The name property of the user.
     */
    NAME("name"),

    /**
     * The last location property of the user.
     */
    LAST_LOCATION("last-location"),

    /**
     * The last login property of the user.
     */
    LAST_LOGIN("last-login");

    private final String storagePath;

    WorldUserProperty(@NotNull final String storagePath) {
        this.storagePath = storagePath;
    }

}