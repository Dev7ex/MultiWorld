package com.dev7ex.multiworld.listener.plugin;

import com.dev7ex.multiworld.MultiWorldPlugin;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class MultiWorldFileChangeListener implements FileAlterationListener {

    private final MultiWorldPlugin plugin;

    public MultiWorldFileChangeListener(@NotNull final MultiWorldPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onDirectoryChange(final File file) {

    }

    @Override
    public void onDirectoryCreate(final File file) {

    }

    @Override
    public void onDirectoryDelete(final File file) {

    }

    @Override
    public void onFileChange(final File file) {

    }

    @Override
    public void onFileCreate(final File file) {

    }

    @Override
    public void onFileDelete(final File file) {

    }

    @Override
    public void onStart(final FileAlterationObserver fileAlterationObserver) {

    }

    @Override
    public void onStop(final FileAlterationObserver fileAlterationObserver) {

    }

}
