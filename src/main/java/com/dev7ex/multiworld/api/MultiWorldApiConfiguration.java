package com.dev7ex.multiworld.api;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.util.List;

/**
 * @author Dev7ex
 * @since 25.01.2023
 */
public interface MultiWorldApiConfiguration {

    DefaultArtifactVersion getVersion();

    String getVersionAsString();

    String getDefaultWorldName();

    List<String> getAutoLoadableWorlds();

}
