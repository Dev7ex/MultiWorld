# MultiWorld

![SpigotMC Downloads](https://img.shields.io/spiget/downloads/92559?label=Downloads)
![Spiget Rating](https://img.shields.io/spiget/rating/92559?label=Rating&style=flat-square)
![GitHub](https://img.shields.io/github/license/dev7ex/multiworld)
[![CodeFactor](https://www.codefactor.io/repository/github/dev7ex/multiworld/badge)](https://www.codefactor.io/repository/github/dev7ex/multiworld)
![GitHub Release](https://img.shields.io/github/v/release/Dev7ex/MultiWorld)


## What is MultiWorld?

MultiWorld is a World Management plugin for Minecraft Server.

- This plugin is highly customizable.
- PVP settings can be adjusted for each world individually.
- GameMode settings for each world. Automatically changes your game mode upon entering a world (can be deactivated).
- Custom Generator Support. Detects and integrates with other world generation plugins.
- Each world has its own whitelist, allowing you to control access.
- Disable access to the Nether or End with the Teleport command.
- Option to completely disable portals.

## Links

* https://www.spigotmc.org/resources/multiworld.92559/
* https://bstats.org/plugin/bukkit/MultiWorld/15446

# Commands

```
* /world                                                                [multiworld.command.world]
* /world back                                                           [multiworld.command.world.back]
* /world backup <World>                                                 [multiworld.command.world.backup]
* /world clone <World> <Clone>                                          [multiworld.command.world.clone]
* /world create <Name> <WorldType|Seed|Generator>                       [multiworld.command.world.create]
* /world delete <World>                                                 [multiworld.command.world.delete]
* /world flag <World> <Option> <Value>                                  [multiworld.command.world.flag]
* /world gamerule <World> <Gamerule> <Value>                            [multiworld.command.world.gamerule]
* /world help                                                           [multiworld.command.world]
* /world import <Name> <WorldType>                                      [multiworld.command.world.import]
* /world info <World>                                                   [multiworld.command.world.info]
* /world list                                                           [multiworld.command.world.list]
* /world link <World> <End | Nether> <World>                            [multiworld.command.world.link]
* /world load <World>                                                   [multiworld.command.world.load]
* /world reload                                                         [multiworld.command.world.reload]
* /world teleport <Player> <World>                                      [multiworld.command.world.teleport]
* /world unload <World>                                                 [multiworld.command.world.unload]
* /world whitelist <World> <Enable|Disable|Add|Remove|List> <Name>      [multiworld.command.world.whitelist]
```

# Configuration

```
#      __  ___      ____  _ _       __           __    __
#    /  |/  /_  __/ / /_(_) |     / /___  _____/ /___/ /
#   / /|_/ / / / / / __/ /| | /| / / __ \/ ___/ / __  /
#  / /  / / /_/ / / /_/ / | |/ |/ / /_/ / /  / / /_/ /
# /_/  /_/\__,_/_/\__/_/  |__/|__/\____/_/  /_/\__,_/
#
# Copyright (c) 2021 - 2024 by Dev7ex
# Version: ${project.version}
config-version: ${project.version}
# General
prefix: '§8[§bMultiWorld§8]§r'
no-permission: '§cIm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that is in error.'
no-console-command: '%prefix% §cThis command can only performed by a player'
no-player-command: '%prefix% §cThis command can only performed by the console'
no-player-found: '%prefix% §cThis player could not be found'

settings:
  # The time format in which information is displayed
  time-format: dd.MM.yyyy HH:mm:ss
  # Should all players with the permission (multiworld.notify.update)
  # get a message when entering server
  receive-update-message: true
  # Should the auto-game-mode per world work?
  auto-game-mode-enabled: true
  # Should MultiWorld connect the worlds with each other via the registered data?
  world-link-enabled: true
  # Should you be able to enter Nether/End worlds with the command /world telport <Player> <World>
  access-nether-world-via-command: true
  access-end-world-via-command: true
  # Standard values for new worlds
  defaults:
    normal-world: world
    end-world: world_the_end
    nether-world: world_nether
    load-auto: false
    difficulty: PEACEFUL
    game-mode: SURVIVAL
    pvp-enabled: true
    spawn-animals: true
    spawn-monsters: true
    spawn-entities: true
    receive-achievements: true
    end-portal-accessible: true
    nether-portal-accessible: true
    whitelist-enabled: false

messages:
  general:
    update-message-player: '%prefix% §7There is a new update available. §8[§bhttps://www.spigotmc.org/resources/multiworld.92559§8]'
    update-message-version-player: '%prefix% §7Current Version: §b%current_version% §7New Version §b%new_version%'
    world-not-exists: '%prefix% §cThe specified world does not exist!'
    world-not-loaded: '%prefix% §cThe specified world is not loaded!'
    world-already-exists: '%prefix% §cThe specified world already exists!'
    world-type-not-exists: '%prefix% §cThe specified WorldType does not exist'
    world-folder-not-exists: '%prefix% §cNo world folder could be found!'
    world-whitelist-block-trespassing: '%prefix% §7You are not on the whitelist of this world!'
  commands:
    back:
      usage: '%prefix% §cUsage: /world back'
      world-not-exists: '%prefix% §cThere is no world you can go!'
      sender-already-there: '%prefix% §cYou are already in the world §b%world_name%'
    backup:
      usage: '%prefix% §cUsage: /world backup <World>'
      starting: '%prefix% §7A backup of the world §b%world_name% is created...'
      finished: '%prefix% §7The backup of the world §b%world_name% §7has been successfully created!'
    clone:
      usage: '%prefix% §cUsage: /world clone <World> <Name>'
      starting: '%prefix% §7The world §b%world_name% §7will be copied...'
      finished: '%prefix% §7The world §b%world_name% §7has been successfully copied!'
    create:
      usage: '%prefix% §cUsage: /world create <Name> <Generator | Seed | WorldType>'
      starting: '%prefix% §7The world §b%world_name% §7will be created...'
      finished: '%prefix% §7The world §b%world_name% §7was created successfully!'
    delete:
      usage: '%prefix% §cUsage: /world delete <World>'
      world-cannot-deleted: '%prefix% §cThe specified world may not be deleted!'
      starting: '%prefix% §7The world §b%world_name% §7will be deleted...'
      finished: '%prefix% §7The world §b%world_name% §7has been successfully deleted!'
    flag:
      usage: '%prefix% §cUsage: /world flag <World> <Flag> <Value>'
      not-existing: '%prefix% §cThis flag does not exist'
      value-not-existing: '%prefix% §cThis value does not exist for the flag §b%flag%'
      successfully-set: '%prefix% §7The flag §b%flag% §7was set to §b%value%§7!'
    gamerule:
      usage: '%prefix% §cUsage: /world flag <World> <Gamerule> <Value>'
      not-existing: '%prefix% §cThis GameRule does not exist'
      value-not-existing: '%prefix% §cThis value does not exist for the GameRule §b%gamerule%'
      successfully-set: '%prefix% §7The GameRule §b%gamerule% §7was set to §b%value%§7!'
    help:
      message:
        - ''
        - '§f§m                    §r§r %prefix% §f§m                    '
        - ''
        - '§7» §7/world §bback'
        - '§7» §7/world §bbackup §7<World>'
        - '§7» §7/world §bclone §7<World> <Name>'
        - '§7» §7/world §bcreate §7<Name> <WorldType | Seed | Generator>'
        - '§7» §7/world §bdelete §7<World>'
        - '§7» §7/world §bflag §7<World> <Property> <Value>'
        - '§7» §7/world §bgamerule §7<World> <GameRule> <Value>'
        - '§7» §7/world §bhelp'
        - '§7» §7/world §bimport §7<World> <WorldType>'
        - '§7» §7/world §binfo §7<World>'
        - '§7» §7/world §blist'
        - '§7» §7/world §blink §7<World> <Nether | End> <Welt>'
        - '§7» §7/world §bload §7<World>'
        - '§7» §7/world §breload'
        - '§7» §7/world §bteleport §7<Player> <World>'
        - '§7» §7/world §bunload §7<World>'
        - '§7» §7/world §bwhitelist §7<World> <Enable | Disable | Add | Remove | List> <Name>'
        - ''
        - '§f§m                    §r§r %prefix% §f§m                    '
        - ''
    import:
      usage: '%prefix% §cUsage: /world import <Name> <WorldType | Generator>'
      world-already-imported: '%prefix% §7The world §b%world_name% §7is already imported!'
      starting: '%prefix% §7The world §b%world_name% §7will import...'
      finished: '%prefix% §7The world §b%world_name% §7was successfully imported!'
    info:
      usage: '%prefix% §cUsage: /world info <World>'
      message:
        - ''
        - '§f§m               §r§r §b%world_name% §f§m               '
        - ''
        - '§7» Creator: §b%world_creator_name%'
        - '§7» Created at: §b%creation_timestamp%'
        - '§7» Load-Auto: §b%load_auto%'
        - '§7» Loaded: §b%loaded%'
        - '§7» WorldType: §b%world_type%'
        - '§7» Environment: §b%environment%'
        - '§7» Difficulty: §b%difficulty%'
        - '§7» GameMode: §b%gamemode%'
        - '§7» Pvp: §b%pvp_enabled%'
        - '§7» Receive-Achievements: §b%receive_achievements%'
        - '§7» Spawn-Monster: §b%spawn_monsters%'
        - '§7» Spawn-Animals: §b%spawn_animals%'
        - '§7» Spawn-Entities: §b%spawn_entities%'
        - '§7» Normal-World: §b%normal_world%'
        - '§7» Nether-World: §b%nether_world%'
        - '§7» End-World: §b%end_world%'
        - '§7» End-Portal-Accessible: §b%end-portal-accessible%'
        - '§7» Nether-Portal-Accessible: §b%nether-portal-accessible%'
        - '§7» Whitelist: §b%whitelist_enabled%'
        - ''
        - '§f§m               §r§r §b%world_name% §f§m               '
        - ''
    list:
      usage: '%prefix% §cUsage: /world list'
      message: '%prefix% §aWorlds: %world_names%'
    link:
      usage: '%prefix% §cUsage: /world link <World> <End | Nether> <World>'
      environment-not-exists: '%prefix% §cThe specified environment does not exist!'
      successfully-set: '%prefix% §7You have connected the portal of the environment §b%environment_name% §7in the world §b%world_name% §7with the world §b%target_world_name%'
    load:
      usage: '%prefix% §cUsage: /world load <Name>'
      world-already-loaded: '%prefix% §7The world §b%world_name% §7is already loaded!'
      starting: '%prefix% §7The world §b%world_name% §7will loaded...'
      finished: '%prefix% §7The world §b%world_name% §7was successfully loaded!'
    reload:
      usage: '%prefix% §cUsage: /world reload'
      message: '%prefix% §7The configurations has been reloaded!'
    teleport:
      usage: '%prefix% §cUsage: /world teleport <Player> <World>'
      message: '%prefix% §a%player_name% §7is teleported to the world §b%world_name% §7!'
      sender-already-there: '%prefix% §7You are already in the world §b%world_name%'
      target-already-there: '%prefix% §7The player §a%player_name% §7is already in the world §b%world_name%'
      nether-not-accessible: '%prefix% §cYou cant enter the Nether via the command!'
      end-not-accessible: '%prefix% §cYou cant enter the end via the command!'
    unload:
      usage: '%prefix% §cUsage: /world unload <World>'
      world-cannot-unloaded: '%prefix% §cThe specified world must not be unloaded!'
      starting: '%prefix% §7The world §b%world_name% §7will be unloaded...'
      finished: '%prefix% §7The world §b%world_name% §7was successfully unloaded!'
      chunk-starting: '%prefix% §7The chunks in §b%world_name% §7are unloaded...'
      chunk-finished: '%prefix% §7The chunks in §b%world_name% §7were successfully unloaded!'
      chunk-teleport: '%prefix% §7The world you were in will be unloaded. You will be teleported!'
    whitelist:
      usage: '%prefix% §cUsage: /world whitelist <World> <On | Off | Add | List | Remove> <Player>'
      add:
        already-added: '%prefix% §7The player %player_name% §7is already §7on the whitelist!'
        successfully-added: '%prefix% §7You have added %player_name% §7to the whitelist of world §b%world_name%'
      list:
        empty: '%prefix% §7The whitelist for world §b%world_name% §7is empty'
        message: '%prefix% §7Whitelist: %player_names%'
      disable:
        already-disabled: '%prefix% §7World whitelist §b%world_name% §7is already disabled!'
        successfully-disabled: '%prefix% §7You have disabled the whitelist in the world §b%world_name%§7!'
      enable:
        already-enabled: '%prefix% §7The world whitelist §b%world_name% §7is already activated!'
        successfully-enabled: '%prefix% §7You have activated the whitelist in the world §b%world_name% §7!'
      remove:
        already-removed: '%prefix% §7The player %player_name% §7is §cnot §7on the whitelist!'
        successfully-removed: '%prefix% §7You have %player_name% §7removed from the §b%world_name% §7whitelist'
```

# Developer

1. Install the the project in your local repository
2. Add the dependency to your project
