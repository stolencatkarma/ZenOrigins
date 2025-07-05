package dev.zenorigins.gui;

import dev.zenorigins.ZenOrigins;
import dev.zenorigins.origins.Origin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GUI for selecting origins
 */
public class OriginSelectionGUI implements InventoryHolder {
    
    private final ZenOrigins plugin;
    private final Player player;
    private final Inventory inventory;
    
    // Material mappings for each origin
    private static final Map<String, Material> ORIGIN_MATERIALS;
    
    static {
        ORIGIN_MATERIALS = new HashMap<>();
        ORIGIN_MATERIALS.put("human", Material.PLAYER_HEAD);
        ORIGIN_MATERIALS.put("enderman", Material.ENDER_PEARL);
        ORIGIN_MATERIALS.put("blaze", Material.BLAZE_ROD);
        ORIGIN_MATERIALS.put("spider", Material.SPIDER_EYE);
        ORIGIN_MATERIALS.put("phantom", Material.PHANTOM_MEMBRANE);
        ORIGIN_MATERIALS.put("wolf", Material.BONE);
        ORIGIN_MATERIALS.put("cat", Material.TROPICAL_FISH);
        ORIGIN_MATERIALS.put("bee", Material.HONEYCOMB);
        ORIGIN_MATERIALS.put("villager", Material.EMERALD);
        ORIGIN_MATERIALS.put("slime", Material.SLIME_BALL);
        ORIGIN_MATERIALS.put("drowned", Material.TRIDENT);
        ORIGIN_MATERIALS.put("irongolem", Material.IRON_INGOT);
        ORIGIN_MATERIALS.put("witch", Material.BREWING_STAND);
        ORIGIN_MATERIALS.put("piglin", Material.GOLD_INGOT);
    }
    
    public OriginSelectionGUI(ZenOrigins plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.inventory = Bukkit.createInventory(this, 54, Component.text("Choose Your Origin", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD));
        setupGUI();
    }
    
    private void setupGUI() {
        // Clear inventory
        inventory.clear();
        
        // Add origin items
        int slot = 10; // Start from slot 10 for better layout
        for (Origin origin : plugin.getOriginManager().getAllOrigins().values()) {
            if (origin.hasPermission(player)) {
                ItemStack item = createOriginItem(origin);
                inventory.setItem(slot, item);
                slot++;
                
                // Skip to next row if we reach the end
                if (slot % 9 == 8) {
                    slot += 2;
                }
                
                // Don't go beyond slot 43 to keep space for other items
                if (slot > 43) break;
            }
        }
        
        // Add current origin info item
        addCurrentOriginInfo();
        
        // Add decorative items and borders
        addDecorations();
    }
    
    private ItemStack createOriginItem(Origin origin) {
        Material material = ORIGIN_MATERIALS.getOrDefault(origin.getId(), Material.BARRIER);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        // Set display name
        meta.displayName(Component.text(origin.getDisplayName(), NamedTextColor.GOLD, TextDecoration.BOLD));
        
        // Create lore
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(""));
        lore.add(Component.text(origin.getDescription(), NamedTextColor.GRAY));
        lore.add(Component.text(""));
        
        // Add abilities
        if (!origin.getAbilities().isEmpty()) {
            lore.add(Component.text("✦ Abilities:", NamedTextColor.GREEN, TextDecoration.BOLD));
            for (String ability : origin.getAbilities()) {
                lore.add(Component.text("  • " + ability, NamedTextColor.AQUA));
            }
            lore.add(Component.text(""));
        }
        
        // Add weaknesses
        if (!origin.getWeaknesses().isEmpty()) {
            lore.add(Component.text("⚠ Weaknesses:", NamedTextColor.RED, TextDecoration.BOLD));
            for (String weakness : origin.getWeaknesses()) {
                lore.add(Component.text("  • " + weakness, NamedTextColor.LIGHT_PURPLE));
            }
            lore.add(Component.text(""));
        }
        
        // Check if player can change origin
        long currentDay = player.getWorld().getFullTime() / 24000L;
        if (plugin.getPlayerDataManager().canChangeOrigin(player.getUniqueId(), currentDay)) {
            lore.add(Component.text("Click to select this origin!", NamedTextColor.YELLOW, TextDecoration.ITALIC));
        } else {
            lore.add(Component.text("You can only change origins once per in-game day!", NamedTextColor.RED, TextDecoration.ITALIC));
        }
        
        meta.lore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    private void addCurrentOriginInfo() {
        String currentOriginId = plugin.getPlayerDataManager().getPlayerOrigin(player.getUniqueId());
        Origin currentOrigin = currentOriginId != null ? plugin.getOriginManager().getOrigin(currentOriginId) : null;
        
        ItemStack infoItem = new ItemStack(Material.BOOK);
        ItemMeta meta = infoItem.getItemMeta();
        
        meta.displayName(Component.text("Current Origin", NamedTextColor.BLUE, TextDecoration.BOLD));
        
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(""));
        
        if (currentOrigin != null) {
            lore.add(Component.text("You are currently: " + currentOrigin.getDisplayName(), NamedTextColor.GREEN));
        } else {
            lore.add(Component.text("You are currently: Human", NamedTextColor.GREEN));
        }
        
        lore.add(Component.text(""));
        
        // Show day restriction info
        long currentDay = player.getWorld().getFullTime() / 24000L;
        long lastChangeDay = plugin.getPlayerDataManager().getLastChangeDay(player.getUniqueId());
        
        if (lastChangeDay >= 0) {
            lore.add(Component.text("Last changed on day: " + lastChangeDay, NamedTextColor.GRAY));
            lore.add(Component.text("Current day: " + currentDay, NamedTextColor.GRAY));
            
            if (plugin.getPlayerDataManager().canChangeOrigin(player.getUniqueId(), currentDay)) {
                lore.add(Component.text("You can change your origin today!", NamedTextColor.GREEN));
            } else {
                lore.add(Component.text("You must wait until the next in-game day to change.", NamedTextColor.RED));
            }
        } else {
            lore.add(Component.text("You haven't changed your origin yet!", NamedTextColor.YELLOW));
        }
        
        meta.lore(lore);
        infoItem.setItemMeta(meta);
        
        inventory.setItem(49, infoItem); // Bottom middle slot
    }
    
    private void addDecorations() {
        // Add border items
        ItemStack borderItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta borderMeta = borderItem.getItemMeta();
        borderMeta.displayName(Component.text(""));
        borderItem.setItemMeta(borderMeta);
        
        // Top and bottom rows
        for (int i = 0; i < 9; i++) {
            if (inventory.getItem(i) == null) inventory.setItem(i, borderItem);
            if (inventory.getItem(45 + i) == null) inventory.setItem(45 + i, borderItem);
        }
        
        // Side columns
        for (int row = 1; row < 5; row++) {
            if (inventory.getItem(row * 9) == null) inventory.setItem(row * 9, borderItem);
            if (inventory.getItem(row * 9 + 8) == null) inventory.setItem(row * 9 + 8, borderItem);
        }
        
        // Add close button
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.displayName(Component.text("Close", NamedTextColor.RED, TextDecoration.BOLD));
        closeMeta.lore(List.of(Component.text("Click to close this menu", NamedTextColor.GRAY)));
        closeItem.setItemMeta(closeMeta);
        inventory.setItem(53, closeItem);
    }
    
    public void open() {
        player.openInventory(inventory);
    }
    
    public Origin getOriginFromSlot(int slot) {
        ItemStack item = inventory.getItem(slot);
        if (item == null || item.getType() == Material.AIR) return null;
        
        // Find origin by checking the material mapping
        for (Map.Entry<String, Material> entry : ORIGIN_MATERIALS.entrySet()) {
            if (entry.getValue() == item.getType()) {
                return plugin.getOriginManager().getOrigin(entry.getKey());
            }
        }
        
        return null;
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
