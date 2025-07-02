# ZenOrigins

An Origins-like plugin for Minecraft Paper 1.21.4 that allows players to choose different entity types to play as, each with unique abilities and weaknesses.

## Features

- **8 Different Origins**: Choose from Human, Enderman, Blaze, Spider, Phantom, Wolf, Cat, and Bee
- **Unique Abilities**: Each origin has special powers like flight, teleportation, climbing, etc.
- **Balanced Weaknesses**: Every origin (except Human) has drawbacks to maintain game balance
- **Persistent Data**: Player origins are saved and restored between sessions
- **Permission System**: Fine-grained control over who can use which origins
- **Admin Commands**: Tools for server administrators to manage the plugin

## Origins

### Human
- **Description**: The standard human form with no special abilities or weaknesses
- **Abilities**: Standard gameplay, balanced stats
- **Weaknesses**: No special abilities

### Enderman
- **Description**: A tall, dark entity with teleportation abilities but weakness to water
- **Abilities**: Night vision, teleportation, tall reach, no fall damage
- **Weaknesses**: Water damage, can't wear pumpkin helmets

### Blaze
- **Description**: A fiery entity immune to fire and lava but weak to water
- **Abilities**: Fire immunity, lava immunity, fire resistance
- **Weaknesses**: Extra water damage, slower in water

### Spider
- **Description**: An arachnid with climbing abilities and night vision
- **Abilities**: Wall climbing, night vision, no fall damage, jump boost
- **Weaknesses**: Extra damage from Bane of Arthropods, slower movement

### Phantom
- **Description**: An undead flying creature of the night
- **Abilities**: Flight, night vision, phasing through blocks, no fall damage
- **Weaknesses**: Burn in sunlight, extra damage from Smite, cannot sleep

### Wolf
- **Description**: A loyal pack animal with enhanced senses and speed
- **Abilities**: Speed boost, jump boost, enhanced smell (night vision), pack loyalty
- **Weaknesses**: Cannot eat vegetables, afraid of cats

### Cat
- **Description**: An agile feline with stealth abilities and soft landings
- **Abilities**: No fall damage, speed boost, night vision, soft landing, stealth
- **Weaknesses**: Afraid of water, smaller health (8 hearts)

### Bee
- **Description**: A small flying insect that loves flowers and can't survive the cold
- **Abilities**: Flight, speed boost, flower power, small size advantages
- **Weaknesses**: Dies in cold weather, extra damage from arthropods, cannot swim, reduced health (6 hearts)

## Commands

### Player Commands
- `/origin list` - Show all available origins
- `/origin choose <origin>` - Choose an origin
- `/origin reset` - Reset to human origin
- `/origin info` - Show your current origin

### Admin Commands
- `/zenorigins reload` - Reload the plugin configuration
- `/zenorigins reset <player>` - Reset a player's origin
- `/zenorigins info` - Show plugin information

## Permissions

### Player Permissions
- `zenorigins.use` - Allows players to use origin commands (default: true)
- `zenorigins.origin.*` - Access to all origins (default: op)
- `zenorigins.origin.human` - Access to human origin (default: true)
- `zenorigins.origin.enderman` - Access to enderman origin (default: true)
- `zenorigins.origin.blaze` - Access to blaze origin (default: true)
- `zenorigins.origin.spider` - Access to spider origin (default: true)
- `zenorigins.origin.phantom` - Access to phantom origin (default: true)
- `zenorigins.origin.wolf` - Access to wolf origin (default: true)
- `zenorigins.origin.cat` - Access to cat origin (default: true)
- `zenorigins.origin.bee` - Access to bee origin (default: true)

### Admin Permissions
- `zenorigins.admin` - Allows access to admin commands (default: op)

## Installation

1. Download the latest release from the releases page
2. Place the `ZenOrigins.jar` file in your server's `plugins` folder
3. Start or restart your server
4. Configure the plugin by editing `plugins/ZenOrigins/config.yml`
5. Reload the plugin with `/zenorigins reload`

## Requirements

- **Minecraft Version**: 1.21.4
- **Server Software**: Paper (required for advanced features)
- **Java Version**: 21 or higher

## Building from Source

1. Clone this repository
2. Make sure you have Java 21 and Maven installed
3. Run `mvn clean package` in the project directory
4. The compiled JAR will be in the `target` folder

## Configuration

The plugin includes a comprehensive configuration file (`config.yml`) where you can:
- Enable/disable specific origins
- Adjust damage values and effects
- Set cooldowns and restrictions
- Configure welcome messages

## API for Developers

ZenOrigins provides a simple API for other plugins to interact with:

```java
// Get the plugin instance
ZenOrigins plugin = (ZenOrigins) Bukkit.getPluginManager().getPlugin("ZenOrigins");

// Get a player's current origin
Origin origin = plugin.getOriginManager().getPlayerOrigin(player);

// Apply an origin to a player
plugin.getOriginManager().applyOrigin(player, "enderman");

// Register a custom origin
plugin.getOriginManager().registerOrigin(new MyCustomOrigin());
```

## Support

If you encounter any issues or have suggestions, please:
1. Check the [Issues](https://github.com/yourusername/ZenOrigins/issues) page
2. Create a new issue if your problem isn't already reported
3. Include server logs and plugin version in your report

## Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Inspired by the Origins mod for Fabric/Forge
- Built for the Paper server software
- Thanks to the Minecraft modding community for inspiration
