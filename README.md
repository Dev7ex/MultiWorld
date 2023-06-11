# Multiworld

![SpigotMC Downloads](https://img.shields.io/spiget/downloads/92559?label=Downloads)
![Spiget Rating](https://img.shields.io/spiget/rating/92559?label=Rating&style=flat-square)
![GitHub](https://img.shields.io/github/license/dev7ex/multiworld)

## What is MultiWorld?
MultiWorld is a World Management plugin for Minecraft Server.

* Almost 100% configurable
* Pvp is adjustable for each world
* GameMode per World - if you change the world your gamemode change also! (can be deactivated)

## Links
***
* https://www.spigotmc.org/resources/multiworld.92559/
* https://bstats.org/plugin/bukkit/MultiWorld/15446

# Commands
```
* /world                                      [multiworld.command.world]
* /world back                                 [multiworld.command.world.back]
* /world clone <Name> <Clone>                 [multiworld.command.world.clone]
* /world create <Name> <WorldType>            [multiworld.command.world.create]
* /world delete <Name>                        [multiworld.command.world.delete]
* /world help                                 [multiworld.command.world]
* /world import <Name> <WorldType>            [multiworld.command.world.import]
* /world info <Name>                          [multiworld.command.world.info]
* /world list                                 [multiworld.command.world.list]
* /world load <Name>                          [multiworld.command.world.load]
* /world options <World> <Option> <Value>     [multiworld.command.world.options]
* /world reload                               [multiworld.command.world.reload]
* /world teleport <Player> <World>            [multiworld.command.world.teleport]
* /world unload <World>                       [multiworld.command.world.unload]
```

# Configuration
```
#      __  ___      ____  _ _       __           __    __
#    /  |/  /_  __/ / /_(_) |     / /___  _____/ /___/ /
#   / /|_/ / / / / / __/ /| | /| / / __ \/ ___/ / __  /
#  / /  / / /_/ / / /_/ / | |/ |/ / /_/ / /  / / /_/ /
# /_/  /_/\__,_/_/\__/_/  |__/|__/\____/_/  /_/\__,_/
#
# Copyright (c) 2023 by Dev7ex
# Version: ${project.version}
config-version: ${project.version}
# General
prefix: '§8[§bMultiWorld§8]§r'
no-permission: '§cIm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that is in error.'
player-not-found: '%prefix% §cThis player cannot be found'
only-player-command: '%prefix% §cThis command can only performed by a player'

# Worlds that should be loaded when the server starts
# WARNING: When the world is not registered by MultiWorld the world will not bee loaded
# The world has to be imported manually! (Attention choose the correct WorldType)
auto-load: []

# Standard values for new worlds
defaults:
  world: world
  difficulty: PEACEFUL
  gamemode: SURVIVAL
  pvp-enabled: true
  spawn-animals: true
  spawn-monsters: true

# Settings
settings:
  # Should all players with the permission (multiworld.notify.update)
  # get a message when entering server
  receive-update-message: true
  # Should the auto-gamemode per world work?
  auto-gamemode: true

messages:
  back:
    usage: '%prefix% §cUsage: /world back'
    world-already-there: '%prefix%§ §7You are already in this world'
    world-not-loaded: '%prefix% §7This world is not longer active'
    world-not-found: '%prefix% §7There is no world you can be teleported'
  clone:
    usage: '%prefix% §cUsage: /world clone <Name> <Clone>'
    starting: '%prefix% §7The world §a%world% §7will be cloned..'
    finished: '%prefix% §7The world §a%world% §7was successfully cloned!'
  create:
    usage: '%prefix% §cUsage: /world create <Name> <WorldType | Seed | Generator>'
    starting: '%prefix% §7The world §a%world% §7is being created...'
    finished: '%prefix% §7The world §a%world% §7was successfully created!'
  delete:
    usage: '%prefix% §cUsage: /world delete <Name>'
    starting: '%prefix% §7The world §a%world% §7will be deleted...'
    finished: '%prefix% §7The world §a%world% §7has been deleted!'
  help:
    messages:
      - ''
      - '§f§m               §r§r %prefix% §f§m               '
      - ''
      - '§7» §7/world §bback'
      - '§7» §7/world §bclone §7<World> <Name>'
      - '§7» §7/world §bcreate §7<Name> <WorldType | Seed | Generator>'
      - '§7» §7/world §bdelete §7<World>'
      - '§7» §7/world §bhelp'
      - '§7» §7/world §bimport §7<World> <WorldType>'
      - '§7» §7/world §binfo §7<World>'
      - '§7» §7/world §blist'
      - '§7» §7/world §bload §7<World>'
      - '§7» §7/world §boptions §7<World> <Option> <Value>'
      - '§7» §7/world §breload'
      - '§7» §7/world §bteleport §7<Player> <World>'
      - '§7» §7/world §bunload §7<World>'
      - ''
      - '§f§m               §r§r %prefix% §f§m               '
      - ''
  import:
    usage: '%prefix% §cUsage: /world import <Name> <WorldType>'
    already-imported: '%prefix% §7Skipped §a%world% §7already imported'
    starting: '%prefix% §7Starting import of §a%world%§7...'
    finished: '%prefix% §7Finished import of §a%world%§7!'
  info:
    usage: '%prefix% §cUsage: /world info <Name>'
    messages:
      - ''
      - '§f§m               §r§r §b%world% §f§m               '
      - ''
      - '§7» Creator: §b%worldCreator%'
      - '§7» CreationTime: §b%creationDate%'
      - '§7» Loaded: §b%loaded%'
      - '§7» WorldType: §b%worldType%'
      - '§7» Environment: §b%environment%'
      - '§7» Difficulty: §b%difficulty%'
      - '§7» Gamemode: §b%gamemode%'
      - '§7» PvP: §b%pvpEnabled%'
      - ''
      - '§f§m               §r§r §b%world% §f§m               '
      - ''
  list:
    usage: '%prefix% §cUsage: /world list'
    message: '%prefix% §7Worlds: %worlds%'
  load:
    usage: '%prefix% §cUsage: /world load <Name>'
    starting: '%prefix% §7The world §a%world% §7is loading...'
    finished: '%prefix% §7The world §a%world% §7was successfully loaded!'
    already-loaded: '%prefix% §7This world is loaded'
    not-registered: '%prefix% §7Couldnt load the world §a%world%§7. Use /world import'
    not-loaded: '%prefix% §7This world is not loaded!'
  options:
    usage: '%prefix% §cUsage: /world options <Name> <Option> <Value>'
    updating: '%prefix% §7Updating §a%option% §7with value §a%value% §7in §a%world%'
    value-wrong: '%prefix% §7The value §a%value% §7is not present'
    not-available: '%prefix% §7This world option does not exist'
  unload:
    usage: '%prefix% §cUsage: /world unload <Name>'
    starting: '%prefix% §7Trying to unload the world §a%world%'
    finished: '%prefix% §7The World §a%world% §7has been unloaded!'
    chunk-starting: '%prefix% §7Trying to unload all chunks...'
    chunk-finished: '%prefix% §7All chunks are unloaded'
    chunk-teleport: '%prefix% §7This world has been unloaded you will be teleported'
  reload:
    usage: '%prefix% §cUsage: /world reload'
    successfully-reloaded: '%prefix% §7The config was §asuccessfully §7reloaded'
  teleport:
    usage: '%prefix% §cUsage: /world teleport <Player> <World>'
    message: '%prefix% §7Teleporting §a%player% §7to %world%'
    component-message: '%prefix% §8[§aTeleport§8]'
    component-hover-text: '%prefix% §7Click to teleport'
    target-already-in-world: '%prefix% §7Player §a%target% already in this world'
    sender-already-in-world: '%prefix% §7You already in this world!'
  general:
    update-message-player: '%prefix% §7There is a new update available. https://www.spigotmc.org/resources/multiworld.92559/'
    invalid-input: '%prefix% §cThis is not a valid value'
    already-exists: '%prefix% §7This world already exists!'
    type-not-available: '%prefix% §7This world type does not exist'
    waiting: '%prefix% §cPlease wait a moment...'
    cannot-deleted: '%prefix% §cThis world cannot be deleted!'
    cannot-unloaded: '%prefix% §cThis world cannot be unloaded!'
    not-exists: '%prefix% §7This world dont exist!'
    folder-not-exists: '%prefix% §7World folder %folder% was not found'
    error-message: '%prefix% §cAn error has occurred! Contact an Administrator!'
```
# Developer

1. Install the the project in your local repository
2. Add the dependency to your project