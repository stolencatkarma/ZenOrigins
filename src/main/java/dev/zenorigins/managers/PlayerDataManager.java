package dev.zenorigins.managers;

import dev.zenorigins.ZenOrigins;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages player data persistence
 */
public class PlayerDataManager {
    
    private final ZenOrigins plugin;
    private final File dataFile;
    private FileConfiguration dataConfig;
    private final Map<UUID, String> playerOrigins;
    private final Map<UUID, Long> lastChangeDay = new HashMap<>();
    
    public PlayerDataManager(ZenOrigins plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        this.playerOrigins = new HashMap<>();
        loadData();
    }
    
    /**
     * Load player data from file
     */
    private void loadData() {
        if (!dataFile.exists()) {
            try {
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create player data file!");
                e.printStackTrace();
                return;
            }
        }
        
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        
        // Load all player origins
        if (dataConfig.contains("players")) {
            for (String uuidString : dataConfig.getConfigurationSection("players").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    String origin = dataConfig.getString("players." + uuidString + ".origin");
                    if (origin != null) {
                        playerOrigins.put(uuid, origin);
                    }
                    long day = dataConfig.getLong("players." + uuidString + ".lastChangeDay", -1);
                    if (day >= 0) {
                        lastChangeDay.put(uuid, day);
                    }
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in player data: " + uuidString);
                }
            }
        }
        
        plugin.getLogger().info("Loaded data for " + playerOrigins.size() + " players");
    }
    
    /**
     * Save all player data to file
     */
    public void saveAllData() {
        try {
            // Clear existing player data
            dataConfig.set("players", null);
            // Save all player origins and last change day
            for (Map.Entry<UUID, String> entry : playerOrigins.entrySet()) {
                dataConfig.set("players." + entry.getKey().toString() + ".origin", entry.getValue());
                Long day = lastChangeDay.get(entry.getKey());
                if (day != null) {
                    dataConfig.set("players." + entry.getKey().toString() + ".lastChangeDay", day);
                }
            }
            dataConfig.save(dataFile);
            plugin.getLogger().info("Saved data for " + playerOrigins.size() + " players");
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save player data!");
            e.printStackTrace();
        }
    }
    
    /**
     * Set a player's origin
     */
    public void setPlayerOrigin(UUID playerId, String origin) {
        if (origin == null) {
            playerOrigins.remove(playerId);
        } else {
            playerOrigins.put(playerId, origin);
        }
        dataConfig.set("players." + playerId.toString() + ".origin", origin);
        saveAsync();
    }

    /**
     * Get a player's origin
     */
    public String getPlayerOrigin(UUID playerId) {
        return playerOrigins.get(playerId);
    }

    /**
     * Set the last day a player changed their origin
     */
    public void setLastChangeDay(UUID playerId, long day) {
        lastChangeDay.put(playerId, day);
        dataConfig.set("players." + playerId.toString() + ".lastChangeDay", day);
        saveAsync();
    }

    /**
     * Get the last day a player changed their origin
     */
    public long getLastChangeDay(UUID playerId) {
        return lastChangeDay.getOrDefault(playerId, -1L);
    }

    /**
     * Check if a player can change their origin
     */
    public boolean canChangeOrigin(UUID playerId, long currentDay) {
        long lastDay = getLastChangeDay(playerId);
        return currentDay > lastDay;
    }

    /**
     * Save data asynchronously
     */
    private void saveAsync() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::saveAllData);
    }

    /**
     * Remove all data for a player
     */
    public void removePlayerData(UUID playerId) {
        playerOrigins.remove(playerId);
        dataConfig.set("players." + playerId.toString(), null);
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not remove data for player " + playerId);
            e.printStackTrace();
        }
    }
}
