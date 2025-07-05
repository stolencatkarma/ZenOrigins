package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Witch origin - potion master
 */
public class WitchOrigin extends Origin {
    
    public WitchOrigin() {
        super("witch", 
              "Witch", 
              "A magical potion brewer with knowledge of alchemy",
              List.of("Instant potion brewing", "Poison immunity", "Magic resistance", "Potion effects last longer"),
              List.of("Takes extra damage from magic", "Weak melee attacks", "Burns in sunlight"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give witchy items
        player.getInventory().addItem(new ItemStack(Material.BREWING_STAND));
        player.getInventory().addItem(new ItemStack(Material.BLAZE_POWDER, 16));
        player.getInventory().addItem(new ItemStack(Material.NETHER_WART, 32));
        player.getInventory().addItem(new ItemStack(Material.SPIDER_EYE, 8));
        player.getInventory().addItem(new ItemStack(Material.GUNPOWDER, 8));
        
        player.sendMessage(net.kyori.adventure.text.Component.text("You feel magical energy coursing through your veins!", net.kyori.adventure.text.format.NamedTextColor.DARK_PURPLE));
    }
    
    @Override
    public void onRemove(Player player) {
        player.sendMessage(net.kyori.adventure.text.Component.text("Your magical powers fade away.", net.kyori.adventure.text.format.NamedTextColor.GRAY));
    }
    
    @Override
    public void onTick(Player player) {
        // Magic particles
        if (player.getTicksLived() % 30 == 0) {
            player.getWorld().spawnParticle(org.bukkit.Particle.WITCH, player.getLocation().add(0, 1, 0), 2, 0.3, 0.3, 0.3, 0.1);
        }
        
        // Random beneficial potion effects occasionally
        if (player.getTicksLived() % 600 == 0) { // Every 30 seconds
            if (Math.random() < 0.3) { // 30% chance
                PotionEffectType[] beneficialEffects = {
                    PotionEffectType.REGENERATION,
                    PotionEffectType.STRENGTH,
                    PotionEffectType.SPEED,
                    PotionEffectType.RESISTANCE
                };
                PotionEffectType randomEffect = beneficialEffects[(int) (Math.random() * beneficialEffects.length)];
                player.addPotionEffect(new PotionEffect(randomEffect, 200, 0, true, false));
                player.sendActionBar(net.kyori.adventure.text.Component.text("Your magic grants you a blessing!", net.kyori.adventure.text.format.NamedTextColor.LIGHT_PURPLE));
            }
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.POISON, Integer.MAX_VALUE, 0, true, false), // Immunity to poison
            new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, true, false)
        );
    }
}
