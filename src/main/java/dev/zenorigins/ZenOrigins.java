package dev.zenorigins;

import dev.zenorigins.commands.OriginCommand;
import dev.zenorigins.commands.ZenOriginsCommand;
import dev.zenorigins.gui.GUIListener;
import dev.zenorigins.listeners.PlayerListener;
import dev.zenorigins.managers.OriginManager;
import dev.zenorigins.managers.PlayerDataManager;
import dev.zenorigins.origins.OriginRegistry;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ZenOrigins extends JavaPlugin {
    
    private static ZenOrigins instance;
    private OriginManager originManager;
    private PlayerDataManager playerDataManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Save default config
        saveDefaultConfig();
        
        // Initialize managers
        playerDataManager = new PlayerDataManager(this);
        originManager = new OriginManager(this);
        
        // Register origins
        OriginRegistry.registerDefaults(originManager);
        
        // Register commands
        Objects.requireNonNull(getCommand("origin")).setExecutor(new OriginCommand(this));
        Objects.requireNonNull(getCommand("zenorigins")).setExecutor(new ZenOriginsCommand(this));
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        
        getLogger().info("ZenOrigins has been enabled!");
    }
    
    @Override
    public void onDisable() {
        if (playerDataManager != null) {
            playerDataManager.saveAllData();
        }
        getLogger().info("ZenOrigins has been disabled!");
    }
    
    public static ZenOrigins getInstance() {
        return instance;
    }
    
    public OriginManager getOriginManager() {
        return originManager;
    }
    
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}
