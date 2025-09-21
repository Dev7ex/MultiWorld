![Icon-Bild](multiworld-resources/src/main/resources/images/title-github.png)

![Latest Release](https://img.shields.io/github/v/release/Dev7ex/MultiWorld)
![SpigotMC Downloads](https://img.shields.io/spiget/downloads/92559?label=Downloads)
![Spiget Rating](https://img.shields.io/spiget/rating/92559?label=Rating&style=flat-square)
![Java](https://img.shields.io/badge/Java-17+-orange)
![Spigot](https://img.shields.io/badge/Spigot-1.16--1.20-red)
[![CodeFactor](https://www.codefactor.io/repository/github/dev7ex/multiworld/badge)](https://www.codefactor.io/repository/github/dev7ex/multiworld)
![Last Commit](https://img.shields.io/github/last-commit/Dev7ex/MultiWorld)
![GitHub](https://img.shields.io/github/license/dev7ex/multiworld)
![Discord](https://img.shields.io/discord/834580308543668264)
![Modrinth Followers](https://img.shields.io/modrinth/followers/multiworld-bukkit)

---

# Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Commands](#commands)
4. [Installation](#installation)
5. [Requirements](#requirements)
6. [Configuration](#configuration)
7. [Contributing](#contributing)
8. [License](#license)
9. [Contact](#contact)

---

# Overview

- The name ‘MultiWorld’ actually says exactly what it does. It allows you to have multiple worlds at one server.
- MultiWorld was created because I myself was not very enthusiastic about Multiverse at the time because there is no way
  to
  set messages and Multiverse is also hardly maintainable because the code is very messy in my opinion

# Features

* This plugin is highly customizable.
* PVP settings can be adjusted for each world individually.
* GameMode settings for each world. Automatically changes your game mode upon entering a world (can be deactivated).
* Custom Generator Support. Detects and integrates with other world generation plugins.
* Each world has its own whitelist, allowing you to control access.
* Disable access to the Nether or End with the Teleport command.
* Option to completely disable portals.
* It is possible to connect portals with other worlds

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
* /world version                                                        [multiworld.command.world.version]
* /world whitelist <World> <Enable|Disable|Add|Remove|List> <Name>      [multiworld.command.world.whitelist]
```

# Installation

1. Download the latest version of `MultiWorld` from [GitHub Releases](https://github.com/Dev7ex/MultiWorld/releases).
2. Download the required version of `FacilisCommon`
   from [GitHub Releases](https://github.com/Dev7ex/FacilisCommon/releases).
3. Copy the downloaded `.jar` file into the `plugins` directory of your Spigot server.
4. Restart the server to activate the plugin.

# Requirements

- Minecraft Version: 1.16 - 1.20
- Java Version: 17 or higher
- Spigot Server

# Configuration

- After installation, a configuration file will be created in the `plugins/MultiWorld` directory. Here, you can make
  various settings.

<details>
<summary>config.yml</summary>

```yaml
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

settings:
  # Should you be able to enter Nether/End worlds with the command /world telport <Player> <World>
  access-nether-world-via-command: true
  access-end-world-via-command: true
  # Should the auto-game-mode per world work?
  auto-game-mode-enabled: true
  # Standard values for new worlds
  defaults:
    # Specifies whether the server should automatically load the worlds upon starting
    auto-load-enabled: false
    # Determines whether the server should automatically unload chunks that are not being used
    auto-unload-enabled: false
    # Sets the game's difficulty level. "PEACEFUL" means no hostile mobs will spawn, and players cannot starve
    difficulty: PEACEFUL
    # Determines whether players have access to the End portal, allowing them to enter the End
    end-portal-accessible: true
    # Specifies the name of the world that serves as the End
    end-world: world_the_end
    # Sets the game mode to Survival, where players must gather resources, manage health, and survive against mobs
    game-mode: SURVIVAL
    # Enables the hunger mechanic, causing players to lose food points over time and need to eat to maintain their health and energy
    hunger-enabled: true
    # Determines whether the server should keep the spawn chunks loaded in memory even when no players are nearby.
    # Set this to true to ensure that spawn chunks are always active, which can be useful for certain redstone machines or farms
    keep-spawn-in-memory: false
    # Determines whether players have access to Nether portals, allowing them to enter the Nether
    nether-portal-accessible: true
    # Specifies the name of the world that serves as the Nether
    nether-world: world_nether
    # Specifies the name of the main world
    normal-world: world
    # Enables player versus player combat, allowing players to attack each other
    pvp-enabled: true
    # Allows players to receive achievements during gameplay
    receive-achievements: true
    # Enables the functionality of Redstone, allowing players to use Redstone mechanisms and devices
    redstone-enabled: true
    # Allows animals to spawn naturally in the world
    spawn-animals: true
    # Allows all entities, including items, mobs, and other non-player characters, to spawn in the world
    spawn-entities: true
    # Allows hostile mobs to spawn naturally in the world
    spawn-monsters: true
    # Enables weather changes such as rain, thunderstorms, and snow
    weather-enabled: true
    # Disables the whitelist feature, allowing any player to join the world without needing to be added to a whitelist
    whitelist-enabled: false
  # The time format in which information is displayed
  time-format: dd.MM.yyyy HH:mm:ss
  # Should MultiWorld connect the worlds with each other via the registered data?
  world-link-enabled: true
```

</details>

# Contributing

We welcome contributions to MultiWorld! If you'd like to contribute, please follow these guidelines:

1. Fork the repository and clone it to your local machine.
2. Create a new branch for your feature or bug fix.
3. Make your changes and ensure the code passes any existing tests.
4. Commit your changes and push them to your fork.
5. Submit a pull request, explaining the changes you've made and why they should be merged.
6. Ensure your pull request adheres to the code style and guidelines of the project.

Thank you for contributing to MultiWorld!

# License

The MultiWorld project is licensed under the GNU General Public License v3.0. See the [LICENSE](LICENSE) file for
details.

# Contact

If you have any questions or need support, you can reach out to Dev7ex via:

- Twitter: [@Dev7ex](https://twitter.com/Dev7ex)
- Discord: [Dev7ex's Discord Server](http://discord.dev7ex.com)