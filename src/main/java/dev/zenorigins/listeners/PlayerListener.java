package dev.zenorigins.listeners;

import dev.zenorigins.ZenOrigins;
import dev.zenorigins.origins.Origin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Handles player events for origins
 */
public class PlayerListener implements Listener {
    
    private final ZenOrigins plugin;
    
    public PlayerListener(ZenOrigins plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Apply stored origin if player has one
        String originId = plugin.getPlayerDataManager().getPlayerOrigin(player.getUniqueId());
        if (originId != null) {
            Origin origin = plugin.getOriginManager().getOrigin(originId);
            if (origin != null && origin.hasPermission(player)) {
                // Re-apply origin effects
                origin.onApply(player);
            } else {
                // Remove invalid origin
                plugin.getPlayerDataManager().setPlayerOrigin(player.getUniqueId(), null);
            }
        } else {
            // New player - set default origin
            plugin.getOriginManager().applyOrigin(player, "human");
        }
        
        // Welcome message for new players
        if (!player.hasPlayedBefore()) {
            player.sendMessage(Component.text("Welcome to the server! Use ", NamedTextColor.GREEN)
                .append(Component.text("/origin list", NamedTextColor.YELLOW))
                .append(Component.text(" to see available origins you can become!", NamedTextColor.GREEN)));
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Save player data when they leave
        plugin.getPlayerDataManager().saveAllData();
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        
        // Reapply origin effects after respawn
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            Origin origin = plugin.getOriginManager().getPlayerOrigin(player);
            if (origin != null) {
                origin.onApply(player);
            }
        }, 5L); // Wait 5 ticks to ensure player is fully respawned
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        
        Origin origin = plugin.getOriginManager().getPlayerOrigin(player);
        if (origin == null) {
            return;
        }
        
        // Handle fall damage for origins that should be immune
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            // Cat, Spider, and Phantom origins don't take fall damage
            if (origin.getId().equals("cat") || origin.getId().equals("spider") || origin.getId().equals("phantom")) {
                event.setCancelled(true);
            }
        }
        
        // Handle fire damage for Blaze origin
        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || 
            event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK ||
            event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
            if (origin.getId().equals("blaze")) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        
        Origin origin = plugin.getOriginManager().getPlayerOrigin(player);
        if (origin == null) {
            return;
        }
        
        // Some origins might have different hunger mechanics
        // For example, Phantom origin could have slower hunger loss
        if (origin.getId().equals("phantom")) {
            // Phantoms don't need to eat as much
            if (event.getFoodLevel() < player.getFoodLevel()) {
                // Slow down hunger loss
                if (Math.random() < 0.5) { // 50% chance to cancel hunger loss
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Player player = event.getPlayer();
            Origin origin = plugin.getOriginManager().getPlayerOrigin(player);
            
            // Enderman origin can teleport without ender pearls
            if (origin != null && origin.getId().equals("enderman")) {
                // Allow enderman players to teleport more freely
                // This could be expanded to add special teleportation abilities
            }
        }
    }
}
