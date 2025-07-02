package dev.zenorigins.origins;

import dev.zenorigins.ZenOrigins;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;

/**
 * Represents an origin that a player can choose
 */
public abstract class Origin {
    
    protected final String id;
    protected final String displayName;
    protected final String description;
    protected final List<String> abilities;
    protected final List<String> weaknesses;
    
    public Origin(String id, String displayName, String description, List<String> abilities, List<String> weaknesses) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.abilities = abilities;
        this.weaknesses = weaknesses;
    }
    
    /**
     * Called when a player chooses this origin
     */
    public abstract void onApply(Player player);
    
    /**
     * Called when a player switches away from this origin
     */
    public abstract void onRemove(Player player);
    
    /**
     * Called every tick for players with this origin
     */
    public abstract void onTick(Player player);
    
    /**
     * Get passive effects for this origin
     */
    public abstract List<PotionEffect> getPassiveEffects();
    
    /**
     * Check if player has permission to use this origin
     */
    public boolean hasPermission(Player player) {
        return player.hasPermission("zenorigins.origin." + id);
    }
    
    // Getters
    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public List<String> getAbilities() { return abilities; }
    public List<String> getWeaknesses() { return weaknesses; }
}
