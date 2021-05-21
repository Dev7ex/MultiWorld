package com.dev7ex.multiworld.world;

import com.google.common.collect.Lists;

import java.util.List;

/**
 *
 * @author Dev7ex
 * @since 20.05.2021
 *
 */

public enum WorldType {

    NORMAL(),
    FLAT(),
    VOID();

    public static List<String> toStringList() {
        final List<String> constants = Lists.newArrayList();

        for(final WorldType type : WorldType.values()) {
            constants.add(type.name());
        }
        return constants;
    }

}
