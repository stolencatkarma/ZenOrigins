package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Cat origin with agility and stealth abilities
 */
public class CatOrigin extends Origin {
    
    public CatOrigin() {
        super("cat", 
              "Cat", 
              "An agile feline with stealth abilities and soft landings.",
              List.of("No fall damage", "Speed boost", "Night vision", "Soft landing", "Stealth"),
              List.of("Afraid of water", "Smaller health"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give permanent speed and night vision
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 1, false, false));
        
        // Reduce max health slightly
        player.setHealthScale(16.0); // 8 hearts instead of 10
    }
    
    @Override
    public void onRemove(Player player) {
        // Remove effects
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.JUMP_BOOST);
        
        // Restore normal health
        player.setHealthScale(20.0);
    }
    
    @Override
    public void onTick(Player player) {
        // Fear of water - apply negative effects when in water
        if (player.isInWater()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20, 2, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 60, 0, false, false));
        }
        
        // Always land softly (no fall damage)
        if (player.getFallDistance() > 0) {
            player.setFallDistance(0);
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false),
            new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false),
            new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 1, false, false)
        );
    }
}
