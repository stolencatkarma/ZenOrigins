package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Ocelot origin - stealthy jungle hunter
 */
public class OcelotOrigin extends Origin {
    
    public OcelotOrigin() {
        super("ocelot", 
              "Ocelot", 
              "A wild and elusive jungle cat with incredible stealth and agility",
              List.of("Natural stealth and invisibility", "Enhanced jumping and climbing", "Night vision", "Scares away creepers", "Silent movement"),
              List.of("Afraid of sudden movements", "Only eats fish and meat", "Skittish around groups", "Cannot swim well"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give ocelot items
        player.getInventory().addItem(new ItemStack(Material.COD, 32));
        player.getInventory().addItem(new ItemStack(Material.SALMON, 32));
        player.getInventory().addItem(new ItemStack(Material.TROPICAL_FISH, 16));
        player.getInventory().addItem(new ItemStack(Material.JUNGLE_LEAVES, 64));
        
        player.sendMessage(net.kyori.adventure.text.Component.text("You become one with the jungle shadows!", net.kyori.adventure.text.format.NamedTextColor.GREEN));
    }
    
    @Override
    public void onRemove(Player player) {
        player.sendMessage(net.kyori.adventure.text.Component.text("You lose your wild instincts.", net.kyori.adventure.text.format.NamedTextColor.GRAY));
    }
    
    @Override
    public void onTick(Player player) {
        // Stealth when sneaking
        if (player.isSneaking()) {
            if (player.getTicksLived() % 60 == 0) { // Every 3 seconds
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0, true, false));
                player.sendActionBar(net.kyori.adventure.text.Component.text("Blending into the shadows...", net.kyori.adventure.text.format.NamedTextColor.DARK_GREEN));
            }
        }
        
        // Scare creepers
        if (player.getTicksLived() % 40 == 0) {
            player.getNearbyEntities(8, 8, 8).stream()
                .filter(entity -> entity instanceof org.bukkit.entity.Creeper)
                .forEach(entity -> {
                    entity.teleport(entity.getLocation().add(
                        Math.random() * 4 - 2, 0, Math.random() * 4 - 2
                    ));
                });
        }
        
        // Silent step particles in jungle
        if (player.getLocation().getBlock().getBiome().toString().contains("JUNGLE")) {
            if (player.getTicksLived() % 40 == 0) {
                player.getWorld().spawnParticle(org.bukkit.Particle.HAPPY_VILLAGER, player.getLocation(), 1, 0.2, 0.2, 0.2, 0.1);
            }
        }
        
        // Fear response to groups
        long nearbyPlayers = player.getNearbyEntities(5, 5, 5).stream()
            .filter(entity -> entity instanceof Player)
            .count();
        
        if (nearbyPlayers >= 3) {
            if (player.getTicksLived() % 100 == 0) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1, true, false));
                player.sendActionBar(net.kyori.adventure.text.Component.text("Too many people! Must escape!", net.kyori.adventure.text.format.NamedTextColor.RED));
            }
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false),
            new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 1, true, false),
            new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false)
        );
    }
}
