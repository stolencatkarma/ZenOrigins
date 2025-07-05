package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Mooshroom origin - mystical mushroom cow
 */
public class MooshroomOrigin extends Origin {
    
    public MooshroomOrigin() {
        super("mooshroom", 
              "Mooshroom", 
              "A rare mushroom-covered cow from the mysterious mushroom islands",
              List.of("Produces mushroom soup when hungry", "Immune to all poisons", "Spreads beneficial spores", "Can survive on mushrooms alone", "Peaceful milk production"),
              List.of("Only spawns on mushroom islands", "Cannot eat meat", "Vulnerable to shearing", "Slow movement", "Attracts attention"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give mooshroom items
        player.getInventory().addItem(new ItemStack(Material.MUSHROOM_STEW, 16));
        player.getInventory().addItem(new ItemStack(Material.RED_MUSHROOM, 32));
        player.getInventory().addItem(new ItemStack(Material.BROWN_MUSHROOM, 32));
        player.getInventory().addItem(new ItemStack(Material.MILK_BUCKET, 4));
        player.getInventory().addItem(new ItemStack(Material.MYCELIUM, 16));
        
        player.sendMessage(net.kyori.adventure.text.Component.text("Mushrooms begin to sprout from your back!", net.kyori.adventure.text.format.NamedTextColor.RED));
    }
    
    @Override
    public void onRemove(Player player) {
        player.sendMessage(net.kyori.adventure.text.Component.text("The mushrooms wither away from your body.", net.kyori.adventure.text.format.NamedTextColor.GRAY));
    }
    
    @Override
    public void onTick(Player player) {
        // Produce mushroom stew when hungry
        if (player.getFoodLevel() <= 10) {
            if (player.getTicksLived() % 200 == 0) { // Every 10 seconds
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(new ItemStack(Material.MUSHROOM_STEW, 1));
                    player.sendActionBar(net.kyori.adventure.text.Component.text("Your body produces nourishing mushroom stew!", net.kyori.adventure.text.format.NamedTextColor.RED));
                }
            }
        }
        
        // Spread beneficial spores
        if (player.getTicksLived() % 100 == 0) {
            player.getNearbyEntities(6, 6, 6).stream()
                .filter(entity -> entity instanceof Player)
                .forEach(entity -> {
                    Player nearbyPlayer = (Player) entity;
                    if (Math.random() < 0.3) { // 30% chance
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0, true, false));
                    }
                });
                
            // Mushroom particles
            player.getWorld().spawnParticle(org.bukkit.Particle.ITEM, player.getLocation().add(0, 1, 0), 
                3, 0.5, 0.5, 0.5, 0.1, new ItemStack(Material.RED_MUSHROOM));
        }
        
        // Convert nearby grass to mycelium occasionally
        if (player.getTicksLived() % 300 == 0) { // Every 15 seconds
            int range = 2;
            for (int x = -range; x <= range; x++) {
                for (int z = -range; z <= range; z++) {
                    org.bukkit.block.Block block = player.getLocation().getBlock().getRelative(x, -1, z);
                    if (block.getType() == Material.GRASS_BLOCK && Math.random() < 0.1) {
                        block.setType(Material.MYCELIUM);
                        player.getWorld().spawnParticle(org.bukkit.Particle.PORTAL, block.getLocation().add(0.5, 1, 0.5), 5, 0.3, 0.3, 0.3, 0.1);
                    }
                }
            }
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 0, true, false),
            new PotionEffect(PotionEffectType.POISON, Integer.MAX_VALUE, 0, true, false) // Immunity through constant weak poison
        );
    }
}
