# ZenOrigins

A Paper plugin for Minecraft 1.21.4 that allows players to choose and play as different entity types with unique abilities and weaknesses.

## Features

- **14 Different Origins**: Human, Enderman, Blaze, Spider, Phantom, Wolf, Cat, Bee, Villager, Slime, Drowned, Iron Golem, Witch, and Piglin
- **Unique Abilities**: Each origin has special powers and characteristics
- **Balanced Weaknesses**: Every origin has drawbacks to maintain game balance
- **Once-per-day Restriction**: Players can only change their origin once per in-game day (anytime during that day)
- **Interactive GUI**: Beautiful server-side inventory GUI for easy origin selection
- **Persistent Data**: Player origins and change history are saved across server restarts
- **Permission System**: Full permission support for different origins
- **Event System**: Custom events for other plugins to hook into

## Commands

- `/origin` - Opens the origin selection GUI
- `/origin gui` - Opens the origin selection GUI
- `/origin list` - Shows all available origins in chat
- `/origin choose <origin>` - Choose a specific origin by name
- `/origin reset` - Reset to human origin
- `/origin info` - Shows your current origin information

### Admin Commands
- `/zenorigins reload` - Reloads the plugin configuration
- `/zenorigins reset <player>` - Reset a player's origin data

## Permissions

- `zenorigins.use` - Allows using origin commands and GUI (default: true)
- `zenorigins.admin` - Allows using admin commands (default: op)
- `zenorigins.origin.<name>` - Allows choosing specific origins (default: true)

## Origins

### Human
- **Description**: The default origin with no special abilities or weaknesses
- **Abilities**: None
- **Weaknesses**: None

### Enderman
- **Description**: A tall, dark entity that can teleport and manipulate blocks
- **Abilities**: Teleportation, block manipulation, water immunity
- **Weaknesses**: Takes damage from water and rain

### Blaze
- **Description**: A fiery entity immune to fire but weak to water
- **Abilities**: Fire immunity, shoots fireballs
- **Weaknesses**: Takes extra damage from water and snowballs

### Spider
- **Description**: An agile arachnid that can climb walls and see in the dark
- **Abilities**: Wall climbing, night vision, web immunity
- **Weaknesses**: Takes extra damage from sweeping attacks

### Phantom
- **Description**: A flying creature of the night skies
- **Abilities**: Flight, night vision, no fall damage
- **Weaknesses**: Burns in sunlight, slower movement during day

### Wolf
- **Description**: A loyal canine with enhanced senses and pack abilities
- **Abilities**: Enhanced speed, night vision, bone healing
- **Weaknesses**: Vegetarian diet only

### Cat
- **Description**: An agile feline with stealth and climbing abilities
- **Abilities**: Enhanced agility, stealth, creeper immunity
- **Weaknesses**: Reduced health, afraid of water

### Bee
- **Description**: A small flying insect that can pollinate and sting
- **Abilities**: Flight, flower interaction, poison immunity
- **Weaknesses**: Dies when attacking (like real bees)

### Villager
- **Description**: A peaceful trader with knowledge of commerce
- **Abilities**: Better villager trades, can detect nearby villages, immune to raid effects, natural trading instincts
- **Weaknesses**: Cannot use weapons above stone tier, takes extra damage from illagers, slower movement speed

### Slime
- **Description**: A bouncy gelatinous creature that can jump high and absorb falls
- **Abilities**: No fall damage, jump boost, bouncy movement, fire resistance
- **Weaknesses**: Takes extra damage from fire, slower movement on land, weak to piercing

### Drowned
- **Description**: An undead aquatic creature that thrives underwater
- **Abilities**: Water breathing, swim speed, trident proficiency, night vision underwater
- **Weaknesses**: Takes damage in sunlight, slower on land, burns in daylight

### Iron Golem
- **Description**: A powerful iron construct built to protect villages
- **Abilities**: Incredible strength, high durability, knockback immunity, villager protection
- **Weaknesses**: Cannot use ranged weapons, slow movement, sinks in water, cannot eat most foods

### Witch
- **Description**: A magical potion brewer with knowledge of alchemy
- **Abilities**: Instant potion brewing, poison immunity, magic resistance, potion effects last longer
- **Weaknesses**: Takes extra damage from magic, weak melee attacks, burns in sunlight

### Piglin
- **Description**: A gold-obsessed nether dweller with natural bartering skills
- **Abilities**: Fire immunity, gold armor bonuses, natural bartering, nether navigation
- **Weaknesses**: Weak in Overworld, afraid of soul fire, obsessed with gold

## Installation

1. Download the latest `ZenOrigins-1.0.0.jar` from the releases
2. Place it in your server's `plugins` folder
3. Start or restart your server
4. Configure permissions as needed

## Configuration

The plugin creates a `config.yml` file with customizable settings for each origin's abilities and restrictions.

## Building from Source

Requirements:
- Java 21
- Maven 3.6+

```bash
git clone https://github.com/yourusername/ZenOrigins.git
cd ZenOrigins
mvn clean package
```

The compiled JAR will be in the `target/` directory.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
