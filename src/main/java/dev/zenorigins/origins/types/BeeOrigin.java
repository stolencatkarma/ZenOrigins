package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Bee origin with flying and pollination abilities
 */
public class BeeOrigin extends Origin {
    
    public BeeOrigin() {
        super("bee", 
              "Bee", 
              "A small flying insect that loves flowers and can't survive the cold.",
              List.of("Flight", "Speed boost", "Flower power", "Small size advantages"),
              List.of("Dies in cold weather", "Extra damage from arthropods", "Cannot swim"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give permanent speed boost and allow flight
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
        player.setAllowFlight(true);
        
        // Reduce health slightly (bees are fragile)
        player.setHealthScale(12.0); // 6 hearts
    }
    
    @Override
    public void onRemove(Player player) {
        // Remove effects
        player.removePotionEffect(PotionEffectType.SPEED);
        
        // Remove flight if not in creative/spectator
        if (!player.getGameMode().name().equals("CREATIVE") && !player.getGameMode().name().equals("SPECTATOR")) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
        
        // Restore normal health
        player.setHealthScale(20.0);
    }
    
    @Override
    public void onTick(Player player) {
        // Check for cold weather/biomes
        if (player.getLocation().getBlock().getTemperature() < 0.2) {
            // Take damage in cold areas
            if (player.getTicksLived() % 40 == 0) { // Every 2 seconds
                player.damage(0.5);
            }
        }
        
        // Cannot swim well - apply negative effects in water
        if (player.isInWater()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20, 3, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 1, false, false));
        }
        
        // Speed boost when near flowers
        boolean nearFlowers = player.getNearbyEntities(5, 5, 5).stream()
            .anyMatch(entity -> entity.getLocation().getBlock().getType().name().contains("FLOWER"));
        
        if (nearFlowers) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 2, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0, false, false));
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false)
        );
    }
}
