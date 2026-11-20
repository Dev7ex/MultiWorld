package com.dev7ex.multiworld.api.module;

import com.dev7ex.multicommon.api.module.Module;
import com.dev7ex.multiworld.api.MultiWorldApi;

/**
 * @author Dev7ex
 * @since 13.04.2025
 */
public interface WorldModule extends Module {

    MultiWorldApi getMultiWorldApi();

}
