package com.dev7ex.multiworld.api.permission;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 16.07.2025
 */
@Getter(AccessLevel.PUBLIC)
public enum MultiWorldPermission {

    // Root
    COMMAND_WORLD("multiworld.command.world", PermissionDefault.OP),

    // /world
    COMMAND_WORLD_BACKUP("multiworld.command.world.backup", PermissionDefault.OP),
    COMMAND_WORLD_CLONE("multiworld.command.world.clone", PermissionDefault.OP),
    COMMAND_WORLD_CONFIG("multiworld.command.world.config", PermissionDefault.OP),
    COMMAND_WORLD_CREATE("multiworld.command.world.create", PermissionDefault.OP),
    COMMAND_WORLD_DEBUG("multiworld.command.world.debug", PermissionDefault.OP),
    COMMAND_WORLD_DELETE("multiworld.command.world.delete", PermissionDefault.OP),
    COMMAND_WORLD_FLAG("multiworld.command.world.flag", PermissionDefault.OP),
    COMMAND_WORLD_GAMERULE("multiworld.command.world.gamerule", PermissionDefault.OP),
    COMMAND_WORLD_GROUP("multiworld.command.world.group", PermissionDefault.OP),
    COMMAND_WORLD_HELP("multiworld.command.world.help", PermissionDefault.OP),
    COMMAND_WORLD_IMPORT("multiworld.command.world.import", PermissionDefault.OP),
    COMMAND_WORLD_INFO("multiworld.command.world.info", PermissionDefault.OP),
    COMMAND_WORLD_LINK("multiworld.command.world.link", PermissionDefault.OP),
    COMMAND_WORLD_LIST("multiworld.command.world.list", PermissionDefault.OP),
    COMMAND_WORLD_LOAD("multiworld.command.world.load", PermissionDefault.OP),
    COMMAND_WORLD_SETSPAWN("multiworld.command.world.setspawn", PermissionDefault.OP),
    COMMAND_WORLD_TELEPORT("multiworld.command.world.teleport", PermissionDefault.OP),
    COMMAND_WORLD_UNLOAD("multiworld.command.world.unload", PermissionDefault.OP),
    COMMAND_WORLD_VERSION("multiworld.command.world.version", PermissionDefault.OP),
    COMMAND_WORLD_WHITELIST("multiworld.command.world.whitelist", PermissionDefault.OP),

    // /world backup
    COMMAND_WORLD_BACKUP_CLEAR("multiworld.command.world.backup.clear", PermissionDefault.OP),
    COMMAND_WORLD_BACKUP_CREATE("multiworld.command.world.backup.create", PermissionDefault.OP),
    COMMAND_WORLD_BACKUP_DELETE("multiworld.command.world.backup.delete", PermissionDefault.OP),
    COMMAND_WORLD_BACKUP_HELP("multiworld.command.world.backup.help", PermissionDefault.OP),
    COMMAND_WORLD_BACKUP_LIST("multiworld.command.world.backup.list", PermissionDefault.OP),
    COMMAND_WORLD_BACKUP_RESTORE("multiworld.command.world.backup.restore", PermissionDefault.OP),

    // /world config
    COMMAND_WORLD_CONFIG_HELP("multiworld.command.world.config.help", PermissionDefault.OP),
    COMMAND_WORLD_CONFIG_RELOAD("multiworld.command.world.config.reload", PermissionDefault.OP),

    // /world group
    COMMAND_WORLD_GROUP_CREATE("multiworld.command.world.group.create", PermissionDefault.OP),
    COMMAND_WORLD_GROUP_DELETE("multiworld.command.world.group.delete", PermissionDefault.OP),
    COMMAND_WORLD_GROUP_HELP("multiworld.command.world.group.help", PermissionDefault.OP),
    COMMAND_WORLD_GROUP_INFO("multiworld.command.world.group.info", PermissionDefault.OP),
    COMMAND_WORLD_GROUP_LIST("multiworld.command.world.group.list", PermissionDefault.OP),

    // /world version
    COMMAND_WORLD_VERSION_CHECK("multiworld.command.world.version.check", PermissionDefault.OP),
    COMMAND_WORLD_VERSION_HELP("multiworld.command.world.version.help", PermissionDefault.OP),
    COMMAND_WORLD_VERSION_INFO("multiworld.command.world.version.info", PermissionDefault.OP),
    COMMAND_WORLD_VERSION_REPORT("multiworld.command.world.version.report", PermissionDefault.OP);

    private final String node;
    private final PermissionDefault defaultValue;

    MultiWorldPermission(@NotNull final String node, @NotNull final PermissionDefault defaultValue) {
        this.node = node;
        this.defaultValue = defaultValue;
    }

    public Permission asBukkit() {
        return new Permission(this.node, this.defaultValue);
    }

}
