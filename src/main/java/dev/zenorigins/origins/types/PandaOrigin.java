package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Panda origin - rare bamboo-loving peaceful giant
 */
public class PandaOrigin extends Origin {
    
    public PandaOrigin() {
        super("panda", 
              "Panda", 
              "A rare and peaceful bamboo-loving giant with unique personalities",
              List.of("Bamboo diet gives extra nutrition", "Peaceful nature calms hostile mobs", "Extra health", "Can sit to regenerate", "Immune to fall damage"),
              List.of("Only eats bamboo and cake", "Moves slowly", "Weak to loud noises", "Lazy - gets tired easily"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give panda items
        player.getInventory().addItem(new ItemStack(Material.BAMBOO, 64));
        player.getInventory().addItem(new ItemStack(Material.CAKE, 3));
        player.getInventory().addItem(new ItemStack(Material.SWEET_BERRIES, 16));
        
        player.sendMessage(net.kyori.adventure.text.Component.text("You feel peaceful and cuddly!", net.kyori.adventure.text.format.NamedTextColor.GREEN));
    }
    
    @Override
    public void onRemove(Player player) {
        player.sendMessage(net.kyori.adventure.text.Component.text("You lose your panda-like calm.", net.kyori.adventure.text.format.NamedTextColor.GRAY));
    }
    
    @Override
    public void onTick(Player player) {
        // Regenerate health when sneaking (sitting)
        if (player.isSneaking()) {
            if (player.getTicksLived() % 40 == 0) { // Every 2 seconds
                if (player.getHealth() < 20.0) {
                    player.setHealth(Math.min(player.getHealth() + 1.0, 20.0));
                    player.sendActionBar(net.kyori.adventure.text.Component.text("Resting peacefully...", net.kyori.adventure.text.format.NamedTextColor.GREEN));
                }
            }
        }
        
        // Peaceful aura - pacify nearby hostile mobs occasionally
        if (player.getTicksLived() % 100 == 0) {
            player.getNearbyEntities(8, 8, 8).stream()
                .filter(entity -> entity instanceof org.bukkit.entity.Monster)
                .forEach(entity -> {
                    if (Math.random() < 0.3) { // 30% chance
                        ((org.bukkit.entity.Monster) entity).setTarget(null);
                    }
                });
        }
        
        // Cute particles when happy
        if (player.getTicksLived() % 60 == 0) {
            player.getWorld().spawnParticle(org.bukkit.Particle.HEART, player.getLocation().add(0, 2, 0), 1, 0.3, 0.3, 0.3, 0.1);
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 0, true, false),
            new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, true, false)
        );
    }
}
