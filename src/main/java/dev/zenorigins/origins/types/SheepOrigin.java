package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Sheep origin - The fluffiest, most woolly, and baah-d decision you'll ever make
 */
public class SheepOrigin extends Origin {
    
    public SheepOrigin() {
        super("sheep", 
              "Sheep", 
              "A fluffy woolly creature that's just trying to fit in with the flock",
              List.of("Grows wool over time", "Can be sheared for resources", "Follows the crowd", "Soft landing from falls", "Attracts wolves... wait that's bad"),
              List.of("Extremely gullible", "Follows other players blindly", "Afraid of wolves", "Makes sheep noises constantly", "Can be sheared bald"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give sheep items
        player.getInventory().addItem(new ItemStack(Material.WHITE_WOOL, 32));
        player.getInventory().addItem(new ItemStack(Material.WHEAT, 64));
        player.getInventory().addItem(new ItemStack(Material.SHEARS, 1));
        player.getInventory().addItem(new ItemStack(Material.WHITE_BED, 1)); // Comfy bed!
        
        player.sendMessage(net.kyori.adventure.text.Component.text("Baaah! You feel fluffy and ready to follow the herd!", net.kyori.adventure.text.format.NamedTextColor.WHITE));
    }
    
    @Override
    public void onRemove(Player player) {
        player.sendMessage(net.kyori.adventure.text.Component.text("Your wool falls away and you regain your independence.", net.kyori.adventure.text.format.NamedTextColor.GRAY));
    }
    
    @Override
    public void onTick(Player player) {
        // Grow wool over time (give wool items)
        if (player.getTicksLived() % 600 == 0) { // Every 30 seconds
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(new ItemStack(Material.WHITE_WOOL, 1));
                player.sendActionBar(net.kyori.adventure.text.Component.text("Your wool has grown thicker! *fluff*", net.kyori.adventure.text.format.NamedTextColor.WHITE));
            }
        }
        
        // Follow other players like a sheep
        Player nearestPlayer = player.getNearbyEntities(8, 8, 8).stream()
            .filter(entity -> entity instanceof Player && entity != player)
            .map(entity -> (Player) entity)
            .findFirst()
            .orElse(null);
            
        if (nearestPlayer != null && Math.random() < 0.1) {
            org.bukkit.util.Vector direction = nearestPlayer.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
            player.setVelocity(player.getVelocity().add(direction.multiply(0.1)));
            
            if (player.getTicksLived() % 200 == 0) {
                player.sendActionBar(net.kyori.adventure.text.Component.text("Following the herd... baaah!", net.kyori.adventure.text.format.NamedTextColor.YELLOW));
            }
        }
        
        // Sheep sounds
        if (player.getTicksLived() % 100 == 0 && Math.random() < 0.4) {
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_SHEEP_AMBIENT, 1.0f, (float)(0.8 + Math.random() * 0.4));
        }
        
        // Run from wolves
        boolean wolvesNearby = player.getNearbyEntities(10, 10, 10).stream()
            .anyMatch(entity -> entity instanceof org.bukkit.entity.Wolf);
            
        if (wolvesNearby) {
            if (player.getTicksLived() % 20 == 0) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
                player.sendActionBar(net.kyori.adventure.text.Component.text("WOLVES! BAAAH! RUN FOR YOUR WOOL!", net.kyori.adventure.text.format.NamedTextColor.RED));
                
                // Panic jumping
                player.setVelocity(player.getVelocity().add(new org.bukkit.util.Vector(
                    (Math.random() - 0.5) * 0.5, 0.3, (Math.random() - 0.5) * 0.5
                )));
            }
        }
        
        // Copy what other players are doing (gullible)
        if (player.getTicksLived() % 80 == 0) {
            player.getNearbyEntities(5, 5, 5).stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .findFirst()
                .ifPresent(otherPlayer -> {
                    if (otherPlayer.isSneaking() && Math.random() < 0.7) {
                        // Mimic sneaking
                        if (!player.isSneaking()) {
                            player.sendActionBar(net.kyori.adventure.text.Component.text("If they're doing it, I should too! Baaah!", net.kyori.adventure.text.format.NamedTextColor.YELLOW));
                        }
                    }
                });
        }
        
        // Fluffy particles
        if (player.getTicksLived() % 40 == 0) {
            player.getWorld().spawnParticle(org.bukkit.Particle.ITEM, player.getLocation().add(0, 1, 0), 
                2, 0.3, 0.3, 0.3, 0.1, new ItemStack(Material.WHITE_WOOL));
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 1, true, false), // Fluffy landing
            new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 0, true, false) // Bouncy wool
        );
    }
}
