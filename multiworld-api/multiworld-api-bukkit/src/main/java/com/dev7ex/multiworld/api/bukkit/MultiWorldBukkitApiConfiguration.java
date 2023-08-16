package com.dev7ex.multiworld.api.bukkit;

import com.dev7ex.common.bukkit.plugin.configuration.DefaultPluginConfiguration;
import com.dev7ex.multiworld.api.MultiWorldApiConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * @author Dev7ex
 * @since 16.08.2023
 */
public abstract class MultiWorldBukkitApiConfiguration extends DefaultPluginConfiguration implements MultiWorldApiConfiguration {

    public MultiWorldBukkitApiConfiguration(@NotNull final Plugin plugin) {
        super(plugin);
    }

    @Getter(AccessLevel.PUBLIC)
    public enum Entry {

        PREFIX("prefix", "§8[§bMultiWorld§8]§r"),
        NO_PERMISSION("no-permission",
                "§cIm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that is in error."),
        PLAYER_NOT_FOUND("player-not-found", "%prefix% §cThis player could not be found"),
        ONLY_PLAYER_COMMAND("only-player-command", "%prefix% §cThis command can only performed by a player"),

        SETTINGS_AUTO_LOAD("settings.auto-load", Collections.emptyList()),
        SETTINGS_RECEIVE_UPDATE_MESSAGE("settings.receive-update-message", true),
        SETTINGS_AUTO_GAMEMODE_ENABLED("settings.auto-game-mode-enabled", true),
        SETTINGS_ACCESS_NETHER_WORLD_VIA_COMMAND("settings.access-nether-world-via-command", true),
        SETTINGS_ACCESS_END_WORLD_VIA_COMMAND("settings.access-end-world-via-command", true),
        SETTINGS_WORLD_LINK_ENABLED("settings.world-link-enabled", true),

        SETTINGS_DEFAULTS_NORMAL_WORLD("settings.defaults.normal-world", "world"),
        SETTINGS_DEFAULTS_END_WORLD("settings.defaults.end-world", "end-world"),
        SETTINGS_DEFAULTS_NETHER_WORLD("settings.defaults.nether_world", "nether_world"),
        SETTINGS_DEFAULTS_DIFFICULTY("settings.defaults.difficulty", "PEACEFUL"),
        SETTINGS_DEFAULTS_GAME_MODE("settings.defaults.game-mode", "SURVIVAL"),
        SETTINGS_DEFAULTS_PVP_ENABLED("settings.defaults.pvp-enabled", true),
        SETTINGS_DEFAULTS_SPAWN_ANIMALS("settings.defaults.spawn-animals", true),
        SETTINGS_DEFAULTS_SPAWN_MONSTERS("settings.defaults.spawn-monsters", true),
        SETTINGS_DEFAULTS_END_PORTAL_ACCESSIBLE("settings.defaults.end-portal-accessible", true),
        SETTINGS_DEFAULTS_NETHER_PORTAL_ACCESSIBLE("defaults.nether-portal-accessible", true),
        SETTINGS_DEFAULTS_WHITELIST_ENABLED("settings.defaults.whitelist-enabled", false),

        MESSAGES_GENERAL_UPDATE_MESSAGE_PLAYER("messages.general.update-message-player", "%prefix% §7There is a new update available. §8[§bhttps://www.spigotmc.org/resources/multiworld.92559§8]"),
        MESSAGES_GENERAL_UPDATE_MESSAGE_VERSION_PLAYER("messages.general.update-message-version-player", "%prefix% §7Current Version: §b%current_version% §7New Version §b%new_version%"),
        MESSAGES_GENERAL_WORLD_NOT_EXISTS("messages.general.world-not-exists", "%prefix% §cThe specified world does not exist!"),
        MESSAGES_GENERAL_WORLD_NOT_LOADED("messages.general.world-not-loaded", "%prefix% §cThe specified world is not loaded!"),
        MESSAGES_GENERAL_WORLD_ALREADY_EXISTS("messages.general.world-already-exists", "%prefix% §cThe specified world already exists!"),
        MESSAGES_GENERAL_WORLD_TYPE_NOT_EXISTS("messages.general.world-type-not-exists", "%prefix% §cThe specified WorldType does not exist"),
        MESSAGES_GENERAL_WORLD_FOLDER_NOT_EXISTS("messages.general.world-folder-not-exists", "%prefix% §cNo world folder could be found!"),
        MESSAGES_GENERAL_WORLD_WHITELIST_BLOCK_TRESPASSING("messages.general.world-whitelist-block-trespassing", "%prefix% §7You are not on the whitelist of this world!"),

        MESSAGES_COMMANDS_BACK_USAGE("messages.commands.back.usage", "%prefix% §cUsage: /world back"),
        MESSAGES_COMMANDS_BACK_WORLD_NOT_EXISTS("messages.commands.back.world-not-exists", "%prefix% §cThere is no world you can go!"),
        MESSAGES_COMMANDS_BACK_SENDER_ALREADY_THERE("messages.commands.back.sender-already-there", "%prefix% §cYou are already in the world §b%world_name%"),

        MESSAGES_COMMANDS_BACKUP_USAGE("messages.commands.backup.usage", "%prefix% §cUsage: /world backup <World>"),
        MESSAGES_COMMANDS_BACKUP_STARTING("messages.commands.backup.starting", "%prefix% §7A backup of the world §b%world_name% is created..."),
        MESSAGES_COMMANDS_BACKUP_FINISHED("messages.commands.backup.finished", "%prefix% §7The backup of the world §b%world_name% §7has been successfully created!"),

        MESSAGES_COMMANDS_CLONE_USAGE("messages.commands.clone.usage", "%prefix% §cUsage: /world clone <World> <Name>"),
        MESSAGES_COMMANDS_CLONE_STARTING("messages.commands.clone.starting", "%prefix% §7The world §b%world_name% §7will be copied..."),
        MESSAGES_COMMANDS_CLONE_FINISHED("messages.commands.clone.finished", "%prefix% §7The world §b%world_name% §7has been successfully copied!"),

        MESSAGES_COMMANDS_CREATE_USAGE("messages.commands.create.usage", "%prefix% §cUsage: /world create <Name> <Generator | Seed | WorldType>"),
        MESSAGES_COMMANDS_CREATE_STARTING("messages.commands.create.starting", "%prefix% §7The world §b%world_name% §7will be created..."),
        MESSAGES_COMMANDS_CREATE_FINISHED("messages.commands.create.finished", "%prefix% §7The world §b%world_name% §7was created successfully!"),

        MESSAGES_COMMANDS_DELETE_USAGE("messages.commands.delete.usage", "%prefix% §cUsage: /world delete <World>"),
        MESSAGES_COMMANDS_DELETE_WORLD_CANNOT_DELETED("messages.commands.delete.world-cannot-deleted", "%prefix% §cThe specified world may not be deleted!"),
        MESSAGES_COMMANDS_DELETE_STARTING("messages.commands.delete.starting", "%prefix% §7The world §b%world_name% §7will be deleted..."),
        MESSAGES_COMMANDS_DELETE_FINISHED("messages.commands.delete.finished", "%prefix% §7The world §b%world_name% §7has been successfully deleted!"),

        MESSAGES_COMMANDS_FLAG_USAGE("messages.commands.flag.usage", "%prefix% §cUsage: /world flag <World> <Flag> <Value>"),
        MESSAGES_COMMANDS_FLAG_NOT_EXISTING("messages.commands.flag.not-existing", "%prefix% §cThis flag does not exist"),
        MESSAGES_COMMANDS_FLAG_VALUE_NOT_EXISTING("messages.commands.flag.value-not-existing", "%prefix% §cThis value does not exist for the flag §b%flag%"),
        MESSAGES_COMMANDS_FLAG_SUCCESSFULLY_SET("messages.commands.flag.successfully-set", "%prefix% §7The flag §b%flag% §7was set to §b%value% §7!"),

        MESSAGES_COMMANDS_HELP_MESSAGE("messages.commands.help.message", "DELETE YOUR CONFIG.YML AND RELOAD"),

        MESSAGES_COMMANDS_IMPORT_USAGE("messages.commands.import.usage", "%prefix% §cUsage: /world import <Name> <WorldType | Generator>"),
        MESSAGES_COMMANDS_IMPORT_WORLD_ALREADY_IMPORTED("messages.commands.import.world-already-imported", "%prefix% §7The world §b%world_name% §7is already imported!"),
        MESSAGES_COMMANDS_IMPORT_STARTING("messages.commands.import.starting", "%prefix% §7The world §b%world_name% §7will import..."),
        MESSAGES_COMMANDS_IMPORT_FINISHED("messages.commands.import.finished", "%prefix% §7The world §b%world_name% §7was successfully imported!"),

        MESSAGES_COMMANDS_INFO_USAGE("messages.commands.info.usage", "'%prefix% §cUsage: /world info <World>'"),
        MESSAGES_COMMANDS_INFO_MESSAGE("messages.commands.info.message", "DELETE YOUR CONFIG.YML AND RELOAD"),

        MESSAGES_COMMANDS_LIST_USAGE("messages.commands.list.usage", "%prefix% §cUsage: /world list"),
        MESSAGES_COMMANDS_LIST_MESSAGE("messages.commands.list.message", "%prefix% §aWorlds: %world_names%"),

        MESSAGES_COMMANDS_LINK_USAGE("messages.commands.link.usage", "%prefix% §cUsage: /world link <World> <End | Nether> <World>"),
        MESSAGES_COMMANDS_LINK_ENVIRONMENT_NOT_EXISTS("messages.commands.link.environment-not-exists", "%prefix% §cThe specified environment does not exist!"),
        MESSAGES_COMMANDS_LINK_SUCCESSFULLY_SET("messages.commands.link.successfully-set", "%prefix% §7You have connected the portal of the environment §b%environment_name% §7in the world §b%world_name% §7with the world §b%target_world_name%"),

        MESSAGES_COMMANDS_LOAD_USAGE("messages.commands.load.usage", "%prefix% §cUsage: /world load <Name>"),
        MESSAGES_COMMANDS_LOAD_WORLD_ALREADY_LOADED("messages.commands.load.world-already-loaded", "%prefix% §7The world §bworld_name% §7is already loaded!"),
        MESSAGES_COMMANDS_LOAD_STARTING("messages.commands.load.starting", "%prefix% §7The world §b%world_name% §7will loaded..."),
        MESSAGES_COMMANDS_LOAD_FINISHED("messages.commands.load.finished", "%prefix% §7The world §b%world_name% §7was successfully loaded!"),

        MESSAGES_COMMANDS_RELOAD_USAGE("messages.commands.reload.usage", "%prefix% §cUsage: /world reload"),
        MESSAGES_COMMANDS_RELOAD_MESSAGE("messages.commands.reload.message", "%prefix% §7The configurations has been reloaded!"),

        MESSAGES_COMMANDS_TELEPORT_USAGE("messages.commands.teleport.usage", "%prefix% §cUsage: /world teleport <Player> <World>"),
        MESSAGES_COMMANDS_TELEPORT_MESSAGE("messages.commands.teleport.message", "%prefix% §a%player_name% §7is teleported to the world §b%world_name% §7!"),
        MESSAGES_COMMANDS_TELEPORT_SENDER_ALREADY_THERE("messages.commands.teleport.sender-already-there", "%prefix% §7You are already in the world §b%world_name%"),
        MESSAGES_COMMANDS_TARGET_ALREADY_THERE("messages.commands.teleport.target-already-there", "%prefix% §7The player §a%player_name% §7is already in the world §b%world_name%"),
        MESSAGES_COMMANDS_TELEPORT_NETHER_NOT_ACCESSIBLE("messages.commands.teleport.nether-not-accessible", "%prefix% §cYou cant enter the Nether via the command!"),
        MESSAGES_COMMANDS_TELEPORT_END_NOT_ACCESSIBLE("messages.commands.teleport.end-not-accessible", "%prefix% §cYou cant enter the end via the command!"),

        MESSAGES_COMMANDS_UNLOAD_USAGE("messages.commands.unload.usage", "%prefix% §cUsage: /world unload <World>"),
        MESSAGES_COMMANDS_UNLOAD_WORLD_CANNOT_UNLOADED("messages.commands.unload.world-cannot-unloaded", "%prefix% §cThe specified world must not be unloaded!"),
        MESSAGES_COMMANDS_UNLOAD_STARTING("messages.commands.unload.starting", "%prefix% §7The world §b%world_name% §7will be unloaded..."),
        MESSAGES_COMMANDS_UNLOAD_FINISHED("messages.commands.unload.finished", "%prefix% §7The world §b%world_name% §7was successfully unloaded!"),
        MESSAGES_COMMANDS_UNLOAD_CHUNK_STARTING("messages.commands.unload.chunk-starting", "%prefix% §7The chunks in §b%world_name% §7are unloaded..."),
        MESSAGES_COMMANDS_UNLOAD_CHUNK_FINISHED("messages.commands.unload.chunk-finished", "%prefix% §7The chunks in §b%world_name% §7were successfully unloaded!"),
        MESSAGES_COMMANDS_UNLOAD_CHUNK_TELEPORT("messages.commands.unload.chunk-teleport", "%prefix% §7The world you were in will be unloaded. You will be teleported!"),

        MESSAGES_COMMANDS_WHITELIST_USAGE("messages.commands.whitelist.usage", "%prefix% §cUsage: /world whitelist <World> <On | Off | Add | List | Remove> <Player>"),
        MESSAGES_COMMANDS_WHITELIST_ADD_ALREADY_ADDED("messages.commands.whitelist.add.already-added", "%prefix% §The player %player_name% §7is already §7on the whitelist!"),
        MESSAGES_COMMANDS_WHITELIST_SUCCESSFULLY_ADDED("messages.commands.whitelist.add.successfully-added", "%prefix% §7You have added %player_name% §7to the whitelist of world §b%world_name%"),
        MESSAGES_COMMANDS_WHITELIST_LIST_EMPTY("messages.commands.whitelist.list.empty", "%prefix% §7The whitelist for world §b%world_name% §7is empty"),
        MESSAGES_COMMANDS_WHITELIST_LIST_MESSAGE("messages.commands.whitelist.list.message", "%prefix% §7Whitelist: %player_names%"),
        MESSAGES_COMMANDS_WHITELIST_DISABLE_ALREADY_DISABLED("messages.commands.whitelist.disable.already-disabled", "%prefix% §7World whitelist §b%world_name% §7is already disabled!"),
        MESSAGES_COMMANDS_WHITELIST_SUCCESSFULLY_DISABLED("messages.commands.whitelist.disable.successfully-disabled", "%prefix% §7You have disabled the whitelist in the world §b%world_name%§7!"),
        MESSAGES_COMMANDS_WHITELIST_ALREADY_ENABLED("messages.commands.whitelist.enable.already-enabled", "%prefix% §7The world whitelist §b%world_name% §7is already activated!"),
        MESSAGES_COMMANDS_WHITELIST_SUCCESSFULLY_ENABLED("messages.commands.whitelist.enable.successfully-enabled", "%prefix% §7You have activated the whitelist in the world §b%world_name% §7!"),
        MESSAGES_COMMANDS_WHITELIST_ALREADY_REMOVED("messages.commands.whitelist.remove.already-removed", "%prefix% §7The player %player_name% §7is §not §7on the whitelist!"),
        MESSAGES_COMMANDS_WHITELIST_SUCCESSFULLY_REMOVED("messages.commands.whitelist.remove.successfully-removed", "%prefix% §7You have %player_name% §7removed from the §b%world_name% §7whitelist");


        private final String path;
        private final Object defaultValue;

        Entry(@NotNull final String path, @NotNull final Object defaultValue) {
            this.path = path;
            this.defaultValue = defaultValue;
        }
    }

}
