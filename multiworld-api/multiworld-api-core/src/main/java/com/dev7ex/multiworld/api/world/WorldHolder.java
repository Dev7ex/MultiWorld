package com.dev7ex.multiworld.api.world;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Dev7ex
 * @since 18.06.2023
 */
public interface WorldHolder {

    String getName();

    String getCreatorName();

    long getCreationTimeStamp();

    WorldType getType();

    <T> T getGameMode();

    <T> T getDifficulty();

    boolean isPvpEnabled();

    boolean isLoaded();

    boolean isSpawnAnimals();

    boolean isSpawnMonsters();

    List<String> getWhitelist();

    boolean isWhitelistEnabled();

    void setWhitelistEnabled(final boolean enabled);

    @Nullable
    String getEndWorldName();

    @Nullable
    String getNetherWorldName();

    @Nullable
    String getNormalWorldName();

    void updateFlag(@NotNull final WorldFlag flag, @NotNull final String value);

}
