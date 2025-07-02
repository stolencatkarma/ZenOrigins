package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Enderman origin with teleportation abilities and water weakness
 */
public class EndermanOrigin extends Origin {
    
    public EndermanOrigin() {
        super("enderman", 
              "Enderman", 
              "A tall, dark entity with teleportation abilities but weakness to water.",
              List.of("Night vision", "Teleportation", "Tall reach", "No fall damage"),
              List.of("Water damage", "Can't wear pumpkin helmets"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give permanent night vision
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
    }
    
    @Override
    public void onRemove(Player player) {
        // Remove night vision
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }
    
    @Override
    public void onTick(Player player) {
        // Check if player is in water or rain
        if (player.isInWater() || (player.getWorld().hasStorm() && player.getLocation().getBlock().getLightFromSky() > 0)) {
            // Deal damage if in water or rain
            if (player.getTicksLived() % 20 == 0) { // Every second
                player.damage(1.0);
            }
        }
        
        // Remove pumpkin helmet if wearing one
        if (player.getInventory().getHelmet() != null && 
            player.getInventory().getHelmet().getType() == Material.CARVED_PUMPKIN) {
            player.getInventory().setHelmet(null);
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false)
        );
    }
}
