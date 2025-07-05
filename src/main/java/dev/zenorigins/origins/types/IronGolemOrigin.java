package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Iron Golem origin - strong protector
 */
public class IronGolemOrigin extends Origin {
    
    public IronGolemOrigin() {
        super("irongolem", 
              "Iron Golem", 
              "A powerful iron construct built to protect villages",
              List.of("Incredible strength", "High durability", "Knockback immunity", "Villager protection"),
              List.of("Cannot use ranged weapons", "Slow movement", "Sinks in water", "Cannot eat most foods"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give iron items
        player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 16));
        player.getInventory().addItem(new ItemStack(Material.POPPY, 4));
        
        player.sendMessage(net.kyori.adventure.text.Component.text("You become an unstoppable iron guardian!", net.kyori.adventure.text.format.NamedTextColor.GRAY));
    }
    
    @Override
    public void onRemove(Player player) {
        player.sendMessage(net.kyori.adventure.text.Component.text("Your iron form crumbles away.", net.kyori.adventure.text.format.NamedTextColor.GRAY));
    }
    
    @Override
    public void onTick(Player player) {
        // Heavy footsteps
        if (player.getTicksLived() % 20 == 0 && player.getLocation().getBlock().getRelative(org.bukkit.block.BlockFace.DOWN).getType().isSolid()) {
            player.getWorld().spawnParticle(org.bukkit.Particle.CLOUD, player.getLocation(), 3, 0.3, 0.1, 0.3, 0.1);
        }
        
        // Sink in water
        if (player.isInWater()) {
            player.setVelocity(player.getVelocity().setY(-0.5));
            if (player.getTicksLived() % 20 == 0) {
                player.sendActionBar(net.kyori.adventure.text.Component.text("Your iron body sinks in water!", net.kyori.adventure.text.format.NamedTextColor.RED));
            }
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2, true, false),
            new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1, true, false),
            new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 1, true, false)
        );
    }
}
