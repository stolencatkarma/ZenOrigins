package dev.zenorigins.integrations;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Integration with LibsDisguises for changing player models
 */
public class DisguiseIntegration {
    
    private static boolean libsDisguisesEnabled = false;
    
    public static void initialize(Plugin plugin) {
        Plugin libsDisguises = plugin.getServer().getPluginManager().getPlugin("LibsDisguises");
        if (libsDisguises != null && libsDisguises.isEnabled()) {
            libsDisguisesEnabled = true;
            plugin.getLogger().info("LibsDisguises found! Player model changes enabled.");
        } else {
            plugin.getLogger().info("LibsDisguises not found. Player models will remain default.");
            plugin.getLogger().info("To enable player model changes, install LibsDisguises plugin.");
        }
    }
    
    public static boolean isEnabled() {
        return libsDisguisesEnabled;
    }
    
    public static void disguisePlayer(Player player, String originId) {
        if (!libsDisguisesEnabled) return;
        
        try {
            // Import LibsDisguises classes dynamically to avoid errors when plugin isn't present
            Class<?> disguiseAPIClass = Class.forName("me.libraryaddict.disguise.DisguiseAPI");
            Class<?> mobDisguiseClass = Class.forName("me.libraryaddict.disguise.disguises.MobDisguise");
            Class<?> disguiseTypeClass = Class.forName("me.libraryaddict.disguise.disguises.DisguiseType");
            
            Object disguiseType = getDisguiseType(disguiseTypeClass, originId);
            if (disguiseType != null) {
                Object disguise = mobDisguiseClass.getConstructor(disguiseTypeClass).newInstance(disguiseType);
                disguiseAPIClass.getMethod("disguiseToAll", Player.class, Class.forName("me.libraryaddict.disguise.Disguise"))
                    .invoke(null, player, disguise);
                
                player.sendMessage(net.kyori.adventure.text.Component.text("Your appearance has changed to match your origin!", 
                    net.kyori.adventure.text.format.NamedTextColor.GREEN));
            }
        } catch (Exception e) {
            // Silently handle errors - plugin works fine without disguises
        }
    }
    
    private static Object getDisguiseType(Class<?> disguiseTypeClass, String originId) {
        try {
            return switch (originId.toLowerCase()) {
                case "enderman" -> disguiseTypeClass.getField("ENDERMAN").get(null);
                case "blaze" -> disguiseTypeClass.getField("BLAZE").get(null);
                case "spider" -> disguiseTypeClass.getField("SPIDER").get(null);
                case "phantom" -> disguiseTypeClass.getField("PHANTOM").get(null);
                case "wolf" -> disguiseTypeClass.getField("WOLF").get(null);
                case "cat" -> disguiseTypeClass.getField("CAT").get(null);
                case "villager" -> disguiseTypeClass.getField("VILLAGER").get(null);
                case "slime" -> disguiseTypeClass.getField("SLIME").get(null);
                case "drowned" -> disguiseTypeClass.getField("DROWNED").get(null);
                case "irongolem" -> disguiseTypeClass.getField("IRON_GOLEM").get(null);
                case "witch" -> disguiseTypeClass.getField("WITCH").get(null);
                case "piglin" -> disguiseTypeClass.getField("PIGLIN").get(null);
                case "bee" -> disguiseTypeClass.getField("BEE").get(null);
                default -> null;
            };
        } catch (Exception e) {
            return null;
        }
    }
    
    public static void removeDisguise(Player player) {
        if (!libsDisguisesEnabled) return;
        
        try {
            Class<?> disguiseAPIClass = Class.forName("me.libraryaddict.disguise.DisguiseAPI");
            disguiseAPIClass.getMethod("undisguiseToAll", Player.class).invoke(null, player);
            
            player.sendMessage(net.kyori.adventure.text.Component.text("You return to your human form.", 
                net.kyori.adventure.text.format.NamedTextColor.GRAY));
        } catch (Exception e) {
            // Silently handle errors
        }
    }
}
