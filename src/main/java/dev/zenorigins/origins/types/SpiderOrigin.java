package dev.zenorigins.origins.types;

import dev.zenorigins.origins.Origin;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Spider origin with climbing abilities and arthropod weaknesses
 */
public class SpiderOrigin extends Origin {
    
    public SpiderOrigin() {
        super("spider", 
              "Spider", 
              "An arachnid with climbing abilities and night vision.",
              List.of("Wall climbing", "Night vision", "No fall damage", "Jump boost"),
              List.of("Extra damage from Bane of Arthropods", "Slower movement"));
    }
    
    @Override
    public void onApply(Player player) {
        // Give permanent night vision and jump boost
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 1, false, false));
    }
    
    @Override
    public void onRemove(Player player) {
        // Remove effects
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.JUMP_BOOST);
    }
    
    @Override
    public void onTick(Player player) {
        // Improved wall climbing and ledge mantling
        if (!player.isSneaking() || player.isFlying() || player.isInsideVehicle()) return;

        org.bukkit.util.Vector direction = player.getLocation().getDirection().normalize();
        org.bukkit.Location eyeLoc = player.getEyeLocation();
        org.bukkit.Location frontBlockLoc = eyeLoc.clone().add(direction.multiply(0.5));
        org.bukkit.Location blockLoc = new org.bukkit.Location(
            player.getWorld(),
            Math.floor(frontBlockLoc.getX()),
            Math.floor(frontBlockLoc.getY()),
            Math.floor(frontBlockLoc.getZ())
        );
        org.bukkit.Location aboveBlockLoc = blockLoc.clone().add(0, 1, 0);
        org.bukkit.Location playerFeet = player.getLocation();
        org.bukkit.Location frontFeetBlockLoc = playerFeet.clone().add(direction.multiply(0.5));
        org.bukkit.Location feetBlockLoc = new org.bukkit.Location(
            player.getWorld(),
            Math.floor(frontFeetBlockLoc.getX()),
            Math.floor(frontFeetBlockLoc.getY()),
            Math.floor(frontFeetBlockLoc.getZ())
        );

        boolean wallInFront = blockLoc.getBlock().getType().isSolid();
        boolean ledgeAbove = !aboveBlockLoc.getBlock().getType().isSolid();
        boolean feetWall = feetBlockLoc.getBlock().getType().isSolid();

        if (wallInFront) {
            // If at the top of a wall and there's a ledge, mantle up
            if (ledgeAbove && !feetWall) {
                // Give a forward and upward boost to get onto the ledge
                org.bukkit.util.Vector climb = direction.clone().multiply(0.4).setY(0.5);
                player.setVelocity(climb);
            } else {
                // Standard climbing up the wall
                if (!player.isOnGround()) {
                    player.setVelocity(player.getVelocity().setY(0.3));
                } else {
                    player.setVelocity(player.getVelocity().setY(0.15));
                }
            }
            player.setFallDistance(0f);
        }
    }
    
    @Override
    public List<PotionEffect> getPassiveEffects() {
        return List.of(
            new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false),
            new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 1, false, false)
        );
    }
}
