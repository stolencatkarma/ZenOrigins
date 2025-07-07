package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Chicken origin - The most chaotic poultry experience
 */
public class ChickenOrigin extends Origin {
    
    public ChickenOrigin() {
        super("chicken", 
              "Chicken", 
              "A clucky, egg-laying feathered friend with questionable intelligence",
              List.of("Lays eggs randomly", "Can glide short distances", "Immune to fall damage", "Attracts other chickens", "Panic mode gives speed boost"),
              List.of("Extremely fragile", "Only eats seeds", "Runs around randomly when hurt", "Afraid of everything", "Makes constant noise"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give chicken items
        player.getInventory().addItem(new ItemStack(Material.EGG, 16));
        player.getInventory().addItem(new ItemStack(Material.WHEAT_SEEDS, 64));
        player.getInventory().addItem(new ItemStack(Material.FEATHER, 32));
        player.getInventory().addItem(new ItemStack(Material.LEATHER_BOOTS)); // Chicken feet!
        
        player.sendMessage(net.kyori.adventure.text.Component.text("Bawk bawk! You feel incredibly... chicken-y!", net.kyori.adventure.text.format.NamedTextColor.YELLOW));
    }
    
    @Override
    public void onRemove(Player player) {
        player.sendMessage(net.kyori.adventure.text.Component.text("Your feathers molt away and your brain grows three sizes.", net.kyori.adventure.text.format.NamedTextColor.GRAY));
    }
    
    @Override
    public void onTick(Player player) {
        // Randomly lay eggs
        if (player.getTicksLived() % 400 == 0 && Math.random() < 0.3) {
            player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.EGG));
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
            player.sendActionBar(net.kyori.adventure.text.Component.text("*plop* You laid an egg!", net.kyori.adventure.text.format.NamedTextColor.YELLOW));
        }
        
        // Random chicken sounds
        if (player.getTicksLived() % 80 == 0 && Math.random() < 0.4) {
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_CHICKEN_AMBIENT, 1.0f, (float)(0.8 + Math.random() * 0.4));
        }
        
        // Panic mode when low health
        if (player.getHealth() <= 6.0) {
            if (player.getTicksLived() % 20 == 0) {
                // Run in random direction
                double angle = Math.random() * Math.PI * 2;
                player.setVelocity(player.getVelocity().add(new org.bukkit.util.Vector(
                    Math.cos(angle) * 0.5, 0.1, Math.sin(angle) * 0.5
                )));
                player.sendActionBar(net.kyori.adventure.text.Component.text("BAWK BAWK BAWK! PANIC!", net.kyori.adventure.text.format.NamedTextColor.RED));
            }
        }
        
        // Attract nearby chickens
        if (player.getTicksLived() % 100 == 0) {
            player.getNearbyEntities(10, 10, 10).stream()
                .filter(entity -> entity instanceof org.bukkit.entity.Chicken)
                .forEach(chicken -> {
                    chicken.teleport(player.getLocation().add(Math.random() * 4 - 2, 0, Math.random() * 4 - 2));
                });
        }
        
        // Feather particles
        if (player.getTicksLived() % 30 == 0) {
            player.getWorld().spawnParticle(org.bukkit.Particle.ITEM, player.getLocation().add(0, 1, 0), 
                2, 0.3, 0.3, 0.3, 0.1, new ItemStack(Material.FEATHER));
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0, true, false),
            new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false) // Always a bit jittery
        );
    }
}
