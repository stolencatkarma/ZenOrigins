package dev.zenorigins.gui;

import dev.zenorigins.ZenOrigins;
import dev.zenorigins.origins.Origin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * Handles GUI interactions
 */
public class GUIListener implements Listener {
    
    private final ZenOrigins plugin;
    
    public GUIListener(ZenOrigins plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        
        if (!(holder instanceof OriginSelectionGUI gui)) {
            return;
        }
        
        event.setCancelled(true); // Cancel all clicks in our GUI
        
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        
        int slot = event.getSlot();
        
        // Handle close button
        if (slot == 53 && event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BARRIER) {
            player.closeInventory();
            return;
        }
        
        // Handle origin selection
        Origin clickedOrigin = gui.getOriginFromSlot(slot);
        if (clickedOrigin == null) {
            return;
        }
        
        // Check if player has permission
        if (!clickedOrigin.hasPermission(player)) {
            player.sendMessage(Component.text("You don't have permission to choose this origin!", NamedTextColor.RED));
            return;
        }
        
        // Check if player can change origin today
        long currentDay = player.getWorld().getFullTime() / 24000L;
        if (!plugin.getPlayerDataManager().canChangeOrigin(player.getUniqueId(), currentDay)) {
            player.sendMessage(Component.text("You can only change your origin once per in-game day!", NamedTextColor.RED));
            return;
        }
        
        // Get current origin for comparison
        String currentOriginId = plugin.getPlayerDataManager().getPlayerOrigin(player.getUniqueId());
        
        // Check if they're selecting the same origin
        if (clickedOrigin.getId().equals(currentOriginId)) {
            player.sendMessage(Component.text("You are already this origin!", NamedTextColor.YELLOW));
            return;
        }
        
        // Apply the origin
        boolean success = plugin.getOriginManager().applyOrigin(player, clickedOrigin.getId());
        
        if (success) {
            // Update last change day
            plugin.getPlayerDataManager().setLastChangeDay(player.getUniqueId(), currentDay);
            
            // Send success message
            player.sendMessage(Component.text("You are now a ", NamedTextColor.GREEN)
                .append(Component.text(clickedOrigin.getDisplayName(), NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text("!", NamedTextColor.GREEN)));
            
            // Close the GUI
            player.closeInventory();
        } else {
            player.sendMessage(Component.text("Failed to change origin. Please try again.", NamedTextColor.RED));
        }
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // We can add any cleanup logic here if needed in the future
    }
}
