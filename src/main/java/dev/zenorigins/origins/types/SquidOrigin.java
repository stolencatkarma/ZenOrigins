package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Squid origin - The wettest, slimiest, most tentacle-y experience
 */
public class SquidOrigin extends Origin {
    
    public SquidOrigin() {
        super("squid", 
              "Squid", 
              "A squishy aquatic cephalopod with too many arms and questionable life choices",
              List.of("Shoots ink clouds for escape", "Can breathe underwater", "Tentacle slaps do knockback", "Can squeeze through small spaces", "Ink vision underwater"),
              List.of("Dies quickly on land", "Extremely squishy and fragile", "Only eats fish", "Leaves ink trails everywhere", "Terrible at walking"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give squid items
        player.getInventory().addItem(new ItemStack(Material.INK_SAC, 64));
        player.getInventory().addItem(new ItemStack(Material.COD, 32));
        player.getInventory().addItem(new ItemStack(Material.KELP, 64));
        player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 8));
        
        player.sendMessage(net.kyori.adventure.text.Component.text("*squelch* You feel squishy and tentacle-y!", net.kyori.adventure.text.format.NamedTextColor.DARK_BLUE));
    }
    
    @Override
    public void onRemove(Player player) {
        player.sendMessage(net.kyori.adventure.text.Component.text("Your tentacles shrivel up and you grow a spine.", net.kyori.adventure.text.format.NamedTextColor.GRAY));
    }
    
    @Override
    public void onTick(Player player) {
        // Die slowly on land
        if (!player.isInWater()) {
            if (player.getTicksLived() % 60 == 0) { // Every 3 seconds
                player.damage(1.0);
                player.sendActionBar(net.kyori.adventure.text.Component.text("*flop flop* Need water! Drying out!", net.kyori.adventure.text.format.NamedTextColor.RED));
                
                // Desperate flopping
                if (Math.random() < 0.5) {
                    player.setVelocity(player.getVelocity().add(new org.bukkit.util.Vector(
                        (Math.random() - 0.5) * 0.3, 0.2, (Math.random() - 0.5) * 0.3
                    )));
                }
            }
        }
        
        // Ink cloud when sneaking or hurt
        if (player.isSneaking() || player.getHealth() <= 8.0) {
            if (player.getTicksLived() % 40 == 0) {
                // Create ink cloud
                for (int i = 0; i < 20; i++) {
                    player.getWorld().spawnParticle(org.bukkit.Particle.ITEM, 
                        player.getLocation().add(Math.random() * 3 - 1.5, Math.random() * 2, Math.random() * 3 - 1.5),
                        1, 0, 0, 0, 0, new ItemStack(Material.INK_SAC));
                }
                
                // Blind nearby entities
                player.getNearbyEntities(4, 4, 4).stream()
                    .filter(entity -> entity instanceof Player)
                    .forEach(entity -> {
                        ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1));
                    });
                
                player.sendActionBar(net.kyori.adventure.text.Component.text("*POOF* Ink cloud escape!", net.kyori.adventure.text.format.NamedTextColor.BLACK));
            }
        }
        
        // Leave ink trails when moving in water
        if (player.isInWater() && player.getVelocity().length() > 0.1) {
            if (player.getTicksLived() % 10 == 0) {
                player.getWorld().spawnParticle(org.bukkit.Particle.ITEM, 
                    player.getLocation(), 2, 0.3, 0.3, 0.3, 0.1, new ItemStack(Material.INK_SAC));
            }
        }
        
        // Squishy sounds when walking
        if (player.getTicksLived() % 20 == 0 && player.getVelocity().length() > 0.1) {
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.BLOCK_SLIME_BLOCK_STEP, 0.5f, 0.7f);
        }
        
        // Random tentacle wiggle particles
        if (player.getTicksLived() % 25 == 0) {
            player.getWorld().spawnParticle(org.bukkit.Particle.FALLING_WATER, 
                player.getLocation().add(0, 1, 0), 3, 0.5, 0.5, 0.5, 0.1);
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, true, false),
            new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 1, true, false),
            new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 1, true, false) // Very slow on land
        );
    }
}
