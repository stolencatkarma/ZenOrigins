package dev.zenorigins.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event fired when a player's origin changes
 */
public class OriginChangeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    
    private final Player player;
    private final String previousOrigin;
    private final String newOrigin;
    private boolean cancelled = false;
    
    public OriginChangeEvent(Player player, String previousOrigin, String newOrigin) {
        this.player = player;
        this.previousOrigin = previousOrigin;
        this.newOrigin = newOrigin;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public String getPreviousOrigin() {
        return previousOrigin;
    }
    
    public String getNewOrigin() {
        return newOrigin;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
