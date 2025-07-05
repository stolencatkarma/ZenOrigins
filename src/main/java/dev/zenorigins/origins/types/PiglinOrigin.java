package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Piglin origin - gold-loving nether dweller
 */
public class PiglinOrigin extends Origin {
    
    public PiglinOrigin() {
        super("piglin", 
              "Piglin", 
              "A gold-obsessed nether dweller with natural bartering skills",
              List.of("Fire immunity", "Gold armor bonuses", "Natural bartering", "Nether navigation"),
              List.of("Weak in Overworld", "Afraid of soul fire", "Obsessed with gold"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give piglin items
        player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 16));
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_SWORD));
        player.getInventory().addItem(new ItemStack(Material.CROSSBOW));
        
        player.sendMessage(net.kyori.adventure.text.Component.text("You feel the call of gold and the nether!", net.kyori.adventure.text.format.NamedTextColor.GOLD));
    }
    
    @Override
    public void onRemove(Player player) {
        player.sendMessage(net.kyori.adventure.text.Component.text("You lose your connection to the nether.", net.kyori.adventure.text.format.NamedTextColor.GRAY));
    }
    
    @Override
    public void onTick(Player player) {
        // Weak in overworld
        if (player.getWorld().getEnvironment() == org.bukkit.World.Environment.NORMAL) {
            if (player.getTicksLived() % 100 == 0) {
                player.sendActionBar(net.kyori.adventure.text.Component.text("The overworld weakens you...", net.kyori.adventure.text.format.NamedTextColor.RED));
            }
        }
        
        // Gold detection
        boolean hasGold = player.getInventory().contains(Material.GOLD_INGOT) || 
                         player.getInventory().contains(Material.GOLD_NUGGET) ||
                         player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType().name().contains("GOLDEN");
        
        if (hasGold && player.getTicksLived() % 200 == 0) {
            player.sendActionBar(net.kyori.adventure.text.Component.text("The gold pleases you!", net.kyori.adventure.text.format.NamedTextColor.GOLD));
        }
        
        // Nether particles
        if (player.getWorld().getEnvironment() == org.bukkit.World.Environment.NETHER) {
            if (player.getTicksLived() % 40 == 0) {
                player.getWorld().spawnParticle(org.bukkit.Particle.FLAME, player.getLocation(), 2, 0.3, 0.3, 0.3, 0.1);
            }
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false)
        );
    }
}
