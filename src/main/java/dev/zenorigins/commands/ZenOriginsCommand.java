package dev.zenorigins.commands;

import dev.zenorigins.ZenOrigins;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Admin commands for ZenOrigins
 */
public class ZenOriginsCommand implements CommandExecutor, TabCompleter {
    
    private final ZenOrigins plugin;
    
    public ZenOriginsCommand(ZenOrigins plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("zenorigins.admin")) {
            sender.sendMessage(Component.text("You don't have permission to use this command!", NamedTextColor.RED));
            return true;
        }
        
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "reload" -> reloadPlugin(sender);
            case "reset" -> {
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /zenorigins reset <player>", NamedTextColor.RED));
                    return true;
                }
                resetPlayer(sender, args[1]);
            }
            case "info" -> showPluginInfo(sender);
            default -> showHelp(sender);
        }
        
        return true;
    }
    
    private void showHelp(CommandSender sender) {
        sender.sendMessage(Component.text("=== ZenOrigins Admin Commands ===", NamedTextColor.GOLD));
        sender.sendMessage(Component.text("/zenorigins reload", NamedTextColor.YELLOW).append(Component.text(" - Reload the plugin configuration", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("/zenorigins reset <player>", NamedTextColor.YELLOW).append(Component.text(" - Reset a player's origin", NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("/zenorigins info", NamedTextColor.YELLOW).append(Component.text(" - Show plugin information", NamedTextColor.WHITE)));
    }
    
    private void reloadPlugin(CommandSender sender) {
        plugin.reloadConfig();
        sender.sendMessage(Component.text("ZenOrigins configuration reloaded!", NamedTextColor.GREEN));
    }
    
    private void resetPlayer(CommandSender sender, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        
        if (target == null) {
            sender.sendMessage(Component.text("Player not found: " + playerName, NamedTextColor.RED));
            return;
        }
        
        if (plugin.getOriginManager().applyOrigin(target, "human")) {
            sender.sendMessage(Component.text("Reset " + target.getName() + "'s origin to Human.", NamedTextColor.GREEN));
            target.sendMessage(Component.text("Your origin has been reset to Human by an administrator.", NamedTextColor.YELLOW));
        } else {
            sender.sendMessage(Component.text("Failed to reset " + target.getName() + "'s origin.", NamedTextColor.RED));
        }
    }
    
    private void showPluginInfo(CommandSender sender) {
        sender.sendMessage(Component.text("=== ZenOrigins Plugin Info ===", NamedTextColor.GOLD));
        sender.sendMessage(Component.text("Version: ", NamedTextColor.YELLOW).append(Component.text(plugin.getPluginMeta().getVersion(), NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("Registered Origins: ", NamedTextColor.YELLOW).append(Component.text(plugin.getOriginManager().getOriginIds().size(), NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("Online Players with Origins: ", NamedTextColor.YELLOW)
            .append(Component.text(Bukkit.getOnlinePlayers().stream()
                .mapToInt(p -> plugin.getOriginManager().getPlayerOrigin(p) != null ? 1 : 0)
                .sum(), NamedTextColor.WHITE)));
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.addAll(List.of("reload", "reset", "info"));
            return completions.stream()
                .filter(completion -> completion.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (args.length == 2 && args[0].equalsIgnoreCase("reset")) {
            completions.addAll(Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList()));
            return completions.stream()
                .filter(completion -> completion.toLowerCase().startsWith(args[1].toLowerCase()))
                .collect(Collectors.toList());
        }
        
        return completions;
    }
}
