# Multiworld
***
MultiWorld is a World plugin for Minecraft Servers.  
It allows creating a lot of worlds of different types

# Download
***
* https://www.spigotmc.org/resources/multiworld.92559/

# Features
***
* GameMode per World - if you change the world your gamemode change also! (can be deactivated)
* PvP per World - allow to deactivate pvp in specific worlds
* Almost all configurable!

# Commands
***
```
* /world                                      [multiworld.command.world]
* /world back                                 [multiworld.command.world.back]
* /world create <Name> <WorldType>            [multiworld.command.world.create]
* /world delete <Name>                        [multiworld.command.world.delete]
* /world help                                 [multiworld.command.world]
* /world import <Name> <WorldType>            [multiworld.command.world.import]
* /world info <Name>                          [multiworld.command.world.info]
* /world list                                 [multiworld.command.world.list]
* /world load <Name>                          [multiworld.command.world.load]
* /world options <World> <Option> <Value>     [multiworld.command.world.options]
* /world teleport <Player> <World>            [multiworld.command.world.teleport]
* /world unload <World>                       [multiworld.command.world.unload]
```

# Configuration
***
```
#     __  ___      ____  _ _       __           __    __
#   /  |/  /_  __/ / /_(_) |     / /___  _____/ /___/ /
#  / /|_/ / / / / / __/ /| | /| / / __ \/ ___/ / __  /
# / /  / / /_/ / / /_/ / | |/ |/ / /_/ / /  / / /_/ /
#/_/  /_/\__,_/_/\__/_/  |__/|__/\____/_/  /_/\__,_/
#
# Copyright (c) 2022 by Dev7ex
# Version: 1.1.8

prefix: '§8[§bMultiWorld§8]§r '
no-permission: '§cIm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that is in error.'
player-not-found: '%prefix%§cThis player cannot be found'
only-player-command: '%prefix% This command can only performed by a player'
usage: '%prefix%§cUsage: %command%'

# Worlds that should be loaded when the server starts
# WARNING: When the world is not registered by MultiWorld the world will not bee loaded
# The world has to be imported manually! (Attention choose the correct WorldType)
auto-load: []

# Standard values for new worlds
defaults:
  world: world
  difficulty: PEACEFUL
  gameMode: SURVIVAL
  pvp-enabled: true
  spawn-animals: false
  spawn-monsters: false

# Settings
settings:
  # Should the auto-gamemode per world work?
  auto-gamemode: true

messages:
  world:
    general:
      already-exists: '%prefix%§7This world already exists!'
      type-not-available: '%prefix%§7This world type does not exist'
      waiting: '%prefix%§cPlease wait a moment...'
      cannot-deleted: '%prefix%§cThis world cannot be deleted!'
      cannot-unloaded: '%prefix%§cThis world cannot be unloaded!'
      not-exists: '%prefix%§7This world dont exist!'
      folder-not-exists: '%prefix%§7World folder %folder% was not found'
      error-message: '%prefix%§cAn error has occurred! Contact an Administrator!'
    back:
      world-already-there: '%prefix%§§7You are already in this world'
      world-not-loaded: '%prefix%§7This world is no longer active'
      world-not-found: '%prefix%§7There is no world you can be teleported'
    create:
      starting: '%prefix%§7The world §a%world% §7is being created...'
      finished: '%prefix%§7The world §a%world% §7was successfully created!'
    delete:
      starting: '%prefix%§7The world §a%world% §7will be deleted...'
      finished: '%prefix%§7The world §a%world% §7has been deleted!'
    help:
      - ''
      - '§f§m------------------§r§r %prefix% §f§m------------------'
      - ''
      - '§7- §7/multiworld §bback'
      - '§7- §7/multiworld §bcreate §7<Name> <WorldType>'
      - '§7- §7/multiworld §bdelete §7<Name>'
      - '§7- §7/multiworld §bhelp'
      - '§7- §7/multiworld §bimport §7<World>'
      - '§7- §7/multiworld §binfo §7<World>'
      - '§7- §7/multiworld §blist'
      - '§7- §7/multiworld §bload §7<World>'
      - '§7- §7/multiworld §boption §7<World> <Option> <Value>'
      - '§7- §7/multiworld §bteleport §7<World>'
      - '§7- §7/multiworld §bunload §7<World>'
      - ''
      - '§f§m------------------§r§r %prefix% §f§m------------------'
      - ''
    import:
      already-imported: '%prefix%§7Skipped §a%world% §7already imported'
      starting: '%prefix%§7Starting import of §a%world%§7...'
      finished: '%prefix%§7Finished import of §a%world%§7!'
    info:
      - ''
      - '§f§m------------------§r§r §b%world% §f§m------------------'
      - ''
      - '§7- Creator: §b%worldCreator%'
      - '§7- CreationTime: §b%creationDate%'
      - '§7- Loaded: §b%loaded%'
      - '§7- WorldType: §b%worldType%'
      - '§7- Environment: §b%environment%'
      - '§7- Difficulty: §b%difficulty%'
      - '§7- GameMode: §b%gameMode%'
      - '§7- PvP: §b%pvpEnabled%'
      - ''
      - '§f§m------------------§r§r §b%world% §f§m------------------'
      - ''
    list:
      message: '%prefix%§7Worlds: %worlds%'
    unloading:
      starting: '%prefix%§7Trying to unload the world §a%world%'
      finished: '%prefix%§7The World §a%world% §7has been unloaded!'
      chunk-starting: '%prefix%§7Trying to unload all chunks...'
      chunk-finished: '%prefix%§7All chunks are unloaded'
      chunk-teleport: '%prefix%§7This world has been unloaded you will be teleported'
    loading:
      starting: '%prefix%§7The world §a%world% §7is loading...'
      finished: '%prefix%§7The world §a%world% §7was successfully loaded!'
      already-loaded: '%prefix%§7This world is loaded'
      not-loaded: '%prefix%§7This world is not loaded!'
      not-registered: '%prefix%§7Couldnt load the world §a%world%§7. Use /world import'
    options:
      updating: '%prefix%§7Updating §a%option% §7with value §a%value% §7in §a%world%'
      value-wrong: '%prefix%§7The value §a%value% §7is not present'
      not-available: '%prefix%§7This world option does not exist'
    teleport:
      message: '%prefix%§7Teleporting §a%player% §7to %world%'
      component-message: '%prefix%§8[§aTeleport§8]'
      component-hover-text: '%prefix%§7Click to teleport'
      target-already-in-world: '%prefix%§7Player §a%target% already in this world'
      sender-already-in-world: '%prefix%§7You already in this world!'
```