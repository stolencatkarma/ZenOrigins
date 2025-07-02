package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Blaze origin with fire immunity and water weakness
 */
public class BlazeOrigin extends Origin {
    
    public BlazeOrigin() {
        super("blaze", 
              "Blaze", 
              "A fiery entity immune to fire and lava but weak to water.",
              List.of("Fire immunity", "Lava immunity", "Fire resistance", "Float in lava"),
              List.of("Extra water damage", "Slower in water"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give permanent fire resistance
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }
    
    @Override
    public void onRemove(Player player) {
        // Remove fire resistance
        player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
    }
    
    @Override
    public void onTick(Player player) {
        // Check if player is in water
        if (player.isInWater()) {
            // Deal extra damage from water
            if (player.getTicksLived() % 10 == 0) { // Every half second
                player.damage(2.0);
            }
            // Apply slowness in water
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20, 1, false, false));
        }
        
        // Check if player is in rain
        if (player.getWorld().hasStorm() && player.getLocation().getBlock().getLightFromSky() > 0) {
            if (player.getTicksLived() % 20 == 0) { // Every second
                player.damage(1.0);
            }
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false)
        );
    }
}
