package dev.zenorigins.commands;

import dev.zenorigins.ZenOrigins;
import dev.zenorigins.gui.OriginSelectionGUI;
import dev.zenorigins.origins.Origin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main command for players to interact with origins
 */
public class OriginCommand implements CommandExecutor, TabCompleter {
    
    private final ZenOrigins plugin;
    
    public OriginCommand(ZenOrigins plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command can only be used by players!", NamedTextColor.RED));
            return true;
        }
        
        if (!player.hasPermission("zenorigins.use")) {
            player.sendMessage(Component.text("You don't have permission to use this command!", NamedTextColor.RED));
            return true;
        }
        
        if (args.length == 0) {
            // Open GUI by default
            openOriginGUI(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "gui" -> openOriginGUI(player);
            case "list" -> showOriginList(player);
            case "choose" -> {
                if (args.length < 2) {
                    player.sendMessage(Component.text("Usage: /origin choose <origin>", NamedTextColor.RED));
                    return true;
                }
                chooseOrigin(player, args[1]);
            }
            case "reset" -> resetOrigin(player);
            case "info" -> showCurrentOrigin(player);
            default -> showHelp(player);
        }
        
        return true;
    }
    
    private void showHelp(Player player) {
        player.sendMessage(Component.text("=== ZenOrigins Commands ===", NamedTextColor.GOLD, TextDecoration.BOLD));
        player.sendMessage(Component.text("/origin", NamedTextColor.YELLOW).append(Component.text(" - Open the origin selection GUI", NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/origin gui", NamedTextColor.YELLOW).append(Component.text(" - Open the origin selection GUI", NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/origin list", NamedTextColor.YELLOW).append(Component.text(" - Show all available origins", NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/origin choose <origin>", NamedTextColor.YELLOW).append(Component.text(" - Choose an origin", NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/origin reset", NamedTextColor.YELLOW).append(Component.text(" - Reset to human origin", NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/origin info", NamedTextColor.YELLOW).append(Component.text(" - Show your current origin", NamedTextColor.WHITE)));
    }

    private void openOriginGUI(Player player) {
        OriginSelectionGUI gui = new OriginSelectionGUI(plugin, player);
        gui.open();
    }
    
    private void showOriginList(Player player) {
        player.sendMessage(Component.text("=== Available Origins ===", NamedTextColor.GOLD, TextDecoration.BOLD));
        
        for (Origin origin : plugin.getOriginManager().getAllOrigins().values()) {
            if (origin.hasPermission(player)) {
                Component originInfo = Component.text("• ", NamedTextColor.GRAY)
                    .append(Component.text(origin.getDisplayName(), NamedTextColor.GREEN, TextDecoration.BOLD))
                    .append(Component.text(" - " + origin.getDescription(), NamedTextColor.WHITE));
                
                player.sendMessage(originInfo);
                
                // Show abilities
                if (!origin.getAbilities().isEmpty()) {
                    Component abilities = Component.text("  Abilities: ", NamedTextColor.BLUE)
                        .append(Component.text(String.join(", ", origin.getAbilities()), NamedTextColor.AQUA));
                    player.sendMessage(abilities);
                }
                
                // Show weaknesses
                if (!origin.getWeaknesses().isEmpty()) {
                    Component weaknesses = Component.text("  Weaknesses: ", NamedTextColor.RED)
                        .append(Component.text(String.join(", ", origin.getWeaknesses()), NamedTextColor.DARK_RED));
                    player.sendMessage(weaknesses);
                }
                
                player.sendMessage(Component.text(""));
            }
        }
    }
    
    private void chooseOrigin(Player player, String originName) {
        Origin origin = plugin.getOriginManager().getOrigin(originName);

        if (origin == null) {
            player.sendMessage(Component.text("That origin does not exist!", NamedTextColor.RED));
            return;
        }

        if (!origin.hasPermission(player)) {
            player.sendMessage(Component.text("You don't have permission to choose this origin!", NamedTextColor.RED));
            return;
        }

        long currentDay = player.getWorld().getFullTime() / 24000L;

        if (!plugin.getPlayerDataManager().canChangeOrigin(player.getUniqueId(), currentDay)) {
            player.sendMessage(Component.text("You can only change your origin once per in-game day!", NamedTextColor.RED));
            return;
        }

        plugin.getOriginManager().setPlayerOrigin(player, origin, false); // Don't give items on command change
        plugin.getPlayerDataManager().setLastChangeDay(player.getUniqueId(), currentDay);

        player.sendMessage(Component.text("You are now a ", NamedTextColor.GREEN)
            .append(Component.text(origin.getDisplayName(), NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text("!", NamedTextColor.GREEN)));
    }

    private void resetOrigin(Player player) {
        long currentDay = player.getWorld().getFullTime() / 24000L;

        if (!plugin.getPlayerDataManager().canChangeOrigin(player.getUniqueId(), currentDay)) {
            player.sendMessage(Component.text("You can only change your origin once per in-game day!", NamedTextColor.RED));
            return;
        }

        plugin.getOriginManager().resetPlayerOrigin(player, false); // Don't give items on command change
        plugin.getPlayerDataManager().setLastChangeDay(player.getUniqueId(), currentDay);

        player.sendMessage(Component.text("You have reset your origin to Human.", NamedTextColor.GREEN));
    }
    
    private void showCurrentOrigin(Player player) {
        Origin currentOrigin = plugin.getOriginManager().getPlayerOrigin(player);
        
        if (currentOrigin == null) {
            player.sendMessage(Component.text("You don't have an origin set. Use /origin choose <origin> to select one.", NamedTextColor.YELLOW));
            return;
        }
        
        player.sendMessage(Component.text("=== Your Current Origin ===", NamedTextColor.GOLD, TextDecoration.BOLD));
        player.sendMessage(Component.text(currentOrigin.getDisplayName(), NamedTextColor.GREEN, TextDecoration.BOLD));
        player.sendMessage(Component.text(currentOrigin.getDescription(), NamedTextColor.WHITE));
        
        if (!currentOrigin.getAbilities().isEmpty()) {
            player.sendMessage(Component.text("Abilities: ", NamedTextColor.BLUE)
                .append(Component.text(String.join(", ", currentOrigin.getAbilities()), NamedTextColor.AQUA)));
        }
        
        if (!currentOrigin.getWeaknesses().isEmpty()) {
            player.sendMessage(Component.text("Weaknesses: ", NamedTextColor.RED)
                .append(Component.text(String.join(", ", currentOrigin.getWeaknesses()), NamedTextColor.DARK_RED)));
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.addAll(List.of("gui", "list", "choose", "reset", "info"));
            return completions.stream()
                .filter(completion -> completion.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (args.length == 2 && args[0].equalsIgnoreCase("choose")) {
            if (sender instanceof Player player) {
                completions.addAll(plugin.getOriginManager().getAllOrigins().values().stream()
                    .filter(origin -> origin.hasPermission(player))
                    .map(Origin::getId)
                    .collect(Collectors.toList()));
            }
            return completions.stream()
                .filter(completion -> completion.toLowerCase().startsWith(args[1].toLowerCase()))
                .collect(Collectors.toList());
        }
        
        return completions;
    }
}
