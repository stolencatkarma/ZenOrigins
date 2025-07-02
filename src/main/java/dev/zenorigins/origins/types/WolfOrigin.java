package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Wolf origin with pack abilities and enhanced senses
 */
public class WolfOrigin extends Origin {
    
    public WolfOrigin() {
        super("wolf", 
              "Wolf", 
              "A loyal pack animal with enhanced senses and speed.",
              List.of("Speed boost", "Jump boost", "Enhanced smell (night vision)", "Pack loyalty"),
              List.of("Cannot eat vegetables", "Afraid of cats"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give permanent speed and jump boost
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
    }
    
    @Override
    public void onRemove(Player player) {
        // Remove effects
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.JUMP_BOOST);
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }
    
    @Override
    public void onTick(Player player) {
        // Check for nearby cats and apply fear effect
        player.getNearbyEntities(10, 10, 10).stream()
            .filter(entity -> entity.getType().name().equals("CAT"))
            .findFirst()
            .ifPresent(cat -> {
                // Apply slowness when near cats
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 1, false, false));
            });
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false),
            new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 0, false, false),
            new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false)
        );
    }
}
