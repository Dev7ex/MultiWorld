package com.dev7ex.multiworld.user;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.dev7ex.multiworld.api.user.WorldUserSkin;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author MGG
 * @since 24.11.2026
 */
@Getter(AccessLevel.PUBLIC)
public class DefaultWorldUserSkin implements WorldUserSkin {

    private static final String TEXTURES_PROPERTY = "textures";

    private final String value;
    private final String signature;

    public DefaultWorldUserSkin(@NotNull final String value, @Nullable final String signature) {
        this.value = value;
        this.signature = signature;
    }

    public static @Nullable DefaultWorldUserSkin fromProfile(@NotNull final PlayerProfile profile) {
        for (final ProfileProperty property : profile.getProperties()) {
            if (DefaultWorldUserSkin.TEXTURES_PROPERTY.equals(property.getName())) {
                return new DefaultWorldUserSkin(property.getValue(), property.getSignature());
            }
        }
        return null;
    }

}
