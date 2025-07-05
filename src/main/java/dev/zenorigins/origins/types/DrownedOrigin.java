package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Drowned origin - underwater specialist
 */
public class DrownedOrigin extends Origin {
    
    public DrownedOrigin() {
        super("drowned", 
              "Drowned", 
              "An undead aquatic creature that thrives underwater",
              List.of("Water breathing", "Swim speed", "Trident proficiency", "Night vision underwater"),
              List.of("Takes damage in sunlight", "Slower on land", "Burns in daylight"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give aquatic items
        player.getInventory().addItem(new ItemStack(Material.TRIDENT));
        player.getInventory().addItem(new ItemStack(Material.KELP, 16));
        player.getInventory().addItem(new ItemStack(Material.PRISMARINE_SHARD, 8));
        
        player.sendMessage(net.kyori.adventure.text.Component.text("You feel the call of the deep ocean!", net.kyori.adventure.text.format.NamedTextColor.AQUA));
    }
    
    @Override
    public void onRemove(Player player) {
        player.sendMessage(net.kyori.adventure.text.Component.text("You lose your connection to the depths.", net.kyori.adventure.text.format.NamedTextColor.GRAY));
    }
    
    @Override
    public void onTick(Player player) {
        // Damage in sunlight
        if (player.getWorld().getTime() < 12300 || player.getWorld().getTime() > 23850) { // Daytime
            if (player.getLocation().getBlock().getLightFromSky() > 10) {
                if (player.getTicksLived() % 40 == 0) { // Every 2 seconds
                    player.damage(1.0);
                    player.sendActionBar(net.kyori.adventure.text.Component.text("Sunlight burns you!", net.kyori.adventure.text.format.NamedTextColor.RED));
                }
            }
        }
        
        // Extra effects when in water
        if (player.isInWater()) {
            // Give night vision when underwater
            if (!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200, 0, true, false));
            }
            
            // Show water particles
            if (player.getTicksLived() % 20 == 0) {
                player.getWorld().spawnParticle(org.bukkit.Particle.SPLASH, player.getLocation(), 5, 0.5, 0.5, 0.5, 0.1);
            }
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, true, false),
            new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 1, true, false),
            new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 0, true, false)
        );
    }
}
