package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Villager origin - trading specialist
 */
public class VillagerOrigin extends Origin {
    
    public VillagerOrigin() {
        super("villager", 
              "Villager", 
              "A peaceful trader with knowledge of commerce",
              List.of("Better villager trades", "Can detect nearby villages", "Immune to raid effects", "Natural trading instincts"),
              List.of("Cannot use weapons above stone tier", "Takes extra damage from illagers", "Slower movement speed"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give trading items
        player.getInventory().addItem(new ItemStack(Material.EMERALD, 5));
        player.getInventory().addItem(new ItemStack(Material.BREAD, 10));
        
        player.sendMessage(net.kyori.adventure.text.Component.text("You feel the wisdom of generations of traders flowing through you!", net.kyori.adventure.text.format.NamedTextColor.GREEN));
    }
    
    @Override
    public void onRemove(Player player) {
        player.sendMessage(net.kyori.adventure.text.Component.text("You lose your connection to the trading world.", net.kyori.adventure.text.format.NamedTextColor.GRAY));
    }
    
    @Override
    public void onTick(Player player) {
        // Detect nearby villages
        if (player.getTicksLived() % 100 == 0) {
            // Check for villagers nearby
            long villagerCount = player.getNearbyEntities(32, 32, 32).stream()
                .filter(entity -> entity instanceof org.bukkit.entity.Villager)
                .count();
            
            if (villagerCount > 0) {
                player.sendActionBar(net.kyori.adventure.text.Component.text("Village detected nearby!", net.kyori.adventure.text.format.NamedTextColor.GREEN));
            }
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 0, true, false)
        );
    }
}
