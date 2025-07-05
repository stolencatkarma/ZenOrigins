package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Parrot origin - colorful flying companion
 */
public class ParrotOrigin extends Origin {
    
    public ParrotOrigin() {
        super("parrot", 
              "Parrot", 
              "A vibrant tropical bird capable of flight and mimicry",
              List.of("Can fly and glide", "Mimics nearby player sounds", "Tropical diet gives energy", "Shoulder perching", "Enhanced hearing"),
              List.of("Takes fall damage if not gliding", "Only eats seeds and fruits", "Fragile build", "Loud and attracts attention"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give parrot items
        player.getInventory().addItem(new ItemStack(Material.WHEAT_SEEDS, 64));
        player.getInventory().addItem(new ItemStack(Material.MELON_SEEDS, 32));
        player.getInventory().addItem(new ItemStack(Material.PUMPKIN_SEEDS, 32));
        player.getInventory().addItem(new ItemStack(Material.BEETROOT_SEEDS, 32));
        player.getInventory().addItem(new ItemStack(Material.COOKIE, 16)); // Parrots love cookies (though toxic in real life!)
        
        player.sendMessage(net.kyori.adventure.text.Component.text("You feel light as a feather and ready to soar!", net.kyori.adventure.text.format.NamedTextColor.YELLOW));
    }
    
    @Override
    public void onRemove(Player player) {
        player.sendMessage(net.kyori.adventure.text.Component.text("Your wings fade away.", net.kyori.adventure.text.format.NamedTextColor.GRAY));
    }
    
    @Override
    public void onTick(Player player) {
        // Enhanced gliding
        if (player.isGliding()) {
            if (player.getTicksLived() % 10 == 0) {
                player.setVelocity(player.getVelocity().multiply(1.1)); // Boost gliding speed
                player.getWorld().spawnParticle(org.bukkit.Particle.HAPPY_VILLAGER, player.getLocation(), 2, 0.5, 0.5, 0.5, 0.1);
            }
        }
        
        // Colorful particles when flying
        if (player.getTicksLived() % 30 == 0 && player.getLocation().getY() > player.getLocation().getWorld().getHighestBlockYAt(player.getLocation())) {
            player.getWorld().spawnParticle(org.bukkit.Particle.NOTE, player.getLocation(), 3, 0.5, 0.5, 0.5, 0.1);
        }
        
        // Mimic nearby player sounds occasionally
        if (player.getTicksLived() % 200 == 0) {
            player.getNearbyEntities(10, 10, 10).stream()
                .filter(entity -> entity instanceof Player && entity != player)
                .findFirst()
                .ifPresent(nearbyPlayer -> {
                    player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PARROT_AMBIENT, 1.0f, 1.0f);
                    player.sendActionBar(net.kyori.adventure.text.Component.text("Squawk! Mimicking nearby sounds!", net.kyori.adventure.text.format.NamedTextColor.GREEN));
                });
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0, true, false), // Always slow falling
            new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 1, true, false) // Enhanced jumping
        );
    }
}
