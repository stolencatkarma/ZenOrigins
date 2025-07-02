package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Phantom origin with flying abilities but undead weaknesses
 */
public class PhantomOrigin extends Origin {
    
    public PhantomOrigin() {
        super("phantom", 
              "Phantom", 
              "An undead flying creature of the night.",
              List.of("Flight", "Night vision", "Phasing through blocks", "No fall damage"),
              List.of("Burn in sunlight", "Extra damage from Smite", "Cannot sleep"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give permanent night vision and slow falling
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0, false, false));
        
        // Allow flight
        player.setAllowFlight(true);
    }
    
    @Override
    public void onRemove(Player player) {
        // Remove effects
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.SLOW_FALLING);
        
        // Remove flight if not in creative/spectator
        if (!player.getGameMode().name().equals("CREATIVE") && !player.getGameMode().name().equals("SPECTATOR")) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }
    
    @Override
    public void onTick(Player player) {
        // Burn in sunlight
        if (player.getWorld().getTime() < 12000 && // Daytime
            player.getLocation().getBlock().getLightFromSky() > 12 &&
            !player.getLocation().getBlock().getType().isSolid()) {
            
            if (player.getTicksLived() % 20 == 0) { // Every second
                player.setFireTicks(40); // Set on fire for 2 seconds
                player.damage(1.0);
            }
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false),
            new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0, false, false)
        );
    }
}
