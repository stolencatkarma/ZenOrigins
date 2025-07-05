package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Slime origin - bouncy and flexible
 */
public class SlimeOrigin extends Origin {
    
    public SlimeOrigin() {
        super("slime", 
              "Slime", 
              "A bouncy gelatinous creature that can jump high and absorb falls",
              List.of("No fall damage", "Jump boost", "Bouncy movement", "Fire resistance"),
              List.of("Takes extra damage from fire", "Slower movement on land", "Weak to piercing"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give slime items
        player.getInventory().addItem(new ItemStack(Material.SLIME_BALL, 16));
        player.getInventory().addItem(new ItemStack(Material.SLIME_BLOCK, 4));
        
        player.sendMessage(net.kyori.adventure.text.Component.text("You feel bouncy and squishy!", net.kyori.adventure.text.format.NamedTextColor.GREEN));
    }
    
    @Override
    public void onRemove(Player player) {
        player.sendMessage(net.kyori.adventure.text.Component.text("You solidify back to normal form.", net.kyori.adventure.text.format.NamedTextColor.GRAY));
    }
    
    @Override
    public void onTick(Player player) {
        // Bounce effect when landing - check if player is on ground without deprecated method
        if (player.getLocation().getBlock().getRelative(org.bukkit.block.BlockFace.DOWN).getType().isSolid() && player.getFallDistance() > 0) {
            // Add a small bounce
            player.setVelocity(player.getVelocity().setY(0.3));
        }
        
        // Show bouncy particles occasionally
        if (player.getTicksLived() % 40 == 0) {
            player.getWorld().spawnParticle(org.bukkit.Particle.HAPPY_VILLAGER, player.getLocation(), 3, 0.3, 0.3, 0.3, 0.1);
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 1, true, false),
            new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 0, true, false),
            new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false)
        );
    }
}
