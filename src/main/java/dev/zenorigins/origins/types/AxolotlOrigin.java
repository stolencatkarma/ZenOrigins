package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Axolotl origin - aquatic healer of the depths
 */
public class AxolotlOrigin extends Origin {
    
    public AxolotlOrigin() {
        super("axolotl", 
              "Axolotl", 
              "A cute aquatic creature with amazing regenerative abilities",
              List.of("Breathes underwater", "Enhanced swimming", "Regeneration when in water", "Can play dead to avoid combat", "Aquatic diet boosts health"),
              List.of("Needs water regularly", "Vulnerable on land", "Only eats fish and aquatic food", "Fragile when dry"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give axolotl items
        player.getInventory().addItem(new ItemStack(Material.TROPICAL_FISH, 32));
        player.getInventory().addItem(new ItemStack(Material.COD, 32));
        player.getInventory().addItem(new ItemStack(Material.SALMON, 16));
        player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 4));
        player.getInventory().addItem(new ItemStack(Material.KELP, 64));
        
        player.sendMessage(net.kyori.adventure.text.Component.text("You feel the call of the deep waters!", net.kyori.adventure.text.format.NamedTextColor.AQUA));
    }
    
    @Override
    public void onRemove(Player player) {
        player.sendMessage(net.kyori.adventure.text.Component.text("Your gills close and your tail disappears.", net.kyori.adventure.text.format.NamedTextColor.GRAY));
    }
    
    @Override
    public void onTick(Player player) {
        // Enhanced regeneration in water
        if (player.isInWater()) {
            if (player.getTicksLived() % 30 == 0) { // Every 1.5 seconds in water
                if (player.getHealth() < 20.0) {
                    player.setHealth(Math.min(player.getHealth() + 1.0, 20.0));
                    player.sendActionBar(net.kyori.adventure.text.Component.text("Regenerating in your natural habitat...", net.kyori.adventure.text.format.NamedTextColor.AQUA));
                }
            }
            
            // Cute bubbles
            if (player.getTicksLived() % 20 == 0) {
                player.getWorld().spawnParticle(org.bukkit.Particle.BUBBLE_COLUMN_UP, player.getLocation(), 5, 0.5, 0.5, 0.5, 0.1);
            }
        } else {
            // Slowly lose health when out of water for too long
            if (player.getTicksLived() % 400 == 0) { // Every 20 seconds
                player.damage(1.0);
                player.sendActionBar(net.kyori.adventure.text.Component.text("You need water to survive!", net.kyori.adventure.text.format.NamedTextColor.RED));
            }
        }
        
        // Play dead when sneaking and low health
        if (player.isSneaking() && player.getHealth() <= 6.0) {
            if (player.getTicksLived() % 100 == 0) {
                player.getNearbyEntities(8, 8, 8).stream()
                    .filter(entity -> entity instanceof org.bukkit.entity.Monster)
                    .forEach(entity -> {
                        ((org.bukkit.entity.Monster) entity).setTarget(null);
                    });
                player.sendActionBar(net.kyori.adventure.text.Component.text("Playing dead to avoid predators...", net.kyori.adventure.text.format.NamedTextColor.YELLOW));
            }
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, true, false),
            new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 0, true, false)
        );
    }
}
