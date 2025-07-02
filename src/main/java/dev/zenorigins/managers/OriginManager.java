package dev.zenorigins.managers;

import dev.zenorigins.ZenOrigins;
import dev.zenorigins.events.OriginChangeEvent;
import dev.zenorigins.origins.Origin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Manages all origins and their applications to players
 */
public class OriginManager {
    
    private final ZenOrigins plugin;
    private final Map<String, Origin> origins;
    private BukkitRunnable tickTask;
    
    public OriginManager(ZenOrigins plugin) {
        this.plugin = plugin;
        this.origins = new HashMap<>();
        startTickTask();
    }
    
    /**
     * Register a new origin
     */
    public void registerOrigin(Origin origin) {
        origins.put(origin.getId(), origin);
        plugin.getLogger().info("Registered origin: " + origin.getDisplayName());
    }
    
    /**
     * Get an origin by ID
     */
    public Origin getOrigin(String id) {
        return origins.get(id);
    }
    
    /**
     * Get all registered origins
     */
    public Set<String> getOriginIds() {
        return origins.keySet();
    }
    
    /**
     * Get all origins
     */
    public Map<String, Origin> getAllOrigins() {
        return new HashMap<>(origins);
    }
    
    /**
     * Apply an origin to a player
     */
    public boolean applyOrigin(Player player, String originId) {
        Origin origin = origins.get(originId);
        if (origin == null) {
            return false;
        }
        
        // Check permission
        if (!origin.hasPermission(player)) {
            return false;
        }
        
        // Get current origin for event
        String currentOriginId = plugin.getPlayerDataManager().getPlayerOrigin(player.getUniqueId());
        
        // Fire origin change event
        OriginChangeEvent event = new OriginChangeEvent(player, currentOriginId, originId);
        Bukkit.getPluginManager().callEvent(event);
        
        if (event.isCancelled()) {
            return false;
        }
        
        // Remove current origin first
        removeCurrentOrigin(player);
        
        // Apply new origin
        origin.onApply(player);
        plugin.getPlayerDataManager().setPlayerOrigin(player.getUniqueId(), originId);
        
        return true;
    }
    
    /**
     * Remove current origin from player
     */
    public void removeCurrentOrigin(Player player) {
        String currentOriginId = plugin.getPlayerDataManager().getPlayerOrigin(player.getUniqueId());
        if (currentOriginId != null) {
            Origin currentOrigin = origins.get(currentOriginId);
            if (currentOrigin != null) {
                currentOrigin.onRemove(player);
            }
        }
    }
    
    /**
     * Get player's current origin
     */
    public Origin getPlayerOrigin(Player player) {
        String originId = plugin.getPlayerDataManager().getPlayerOrigin(player.getUniqueId());
        return originId != null ? origins.get(originId) : null;
    }
    
    /**
     * Start the tick task that runs origin effects
     */
    private void startTickTask() {
        tickTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Origin origin = getPlayerOrigin(player);
                    if (origin != null) {
                        origin.onTick(player);
                    }
                }
            }
        };
        tickTask.runTaskTimer(plugin, 0L, 1L); // Run every tick
    }
    
    /**
     * Stop the tick task
     */
    public void stopTickTask() {
        if (tickTask != null) {
            tickTask.cancel();
        }
    }
}
