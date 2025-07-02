package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;

/**
 * Default human origin - no special abilities or weaknesses
 */
public class HumanOrigin extends Origin {
    
    public HumanOrigin() {
        super("human", 
              "Human", 
              "The standard human form with no special abilities or weaknesses.",
              List.of("Standard gameplay", "Balanced stats"),
              List.of("No special abilities"));
    }
    
    @Override
    public void onApply(Player player) {
        // Nothing special for humans
    }
    
    @Override
    public void onRemove(Player player) {
        // Nothing to remove
    }
    
    @Override
    public void onTick(Player player) {
        // No special tick behavior
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(); // No passive effects
    }
}
