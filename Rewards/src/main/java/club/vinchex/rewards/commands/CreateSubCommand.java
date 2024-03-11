package club.vinchex.rewards.commands;

import club.vinchex.rewards.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CreateSubCommand implements CommandExecutor {
    private final Main plugin;

    public CreateSubCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getNotConsole()));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("adminrewards.command.create") || !player.hasPermission("rewards.*")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getNotHavePermission()));
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7Use: /adminrewards create <reward>"));
            return true;
        }

        String rewardName = args[1];
        FileConfiguration config = plugin.getConfig();

        if (config.contains("rewards." + rewardName)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7The reward &6" + rewardName + "&7 already exist."));
            return true;
        }

        config.set("rewards." + rewardName, new ArrayList<String>());
        plugin.saveConfig();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7Reward &6" + rewardName + "&7 made successfully."));
        return true;
    }
    private String getNotConsole() {
        StringBuilder sb = new StringBuilder();
        for (String line : plugin.getMessagesConfig().getStringList("not-console")) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
    private String getNotHavePermission() {
        StringBuilder sb = new StringBuilder();
        for (String line : plugin.getMessagesConfig().getStringList("not-permission")) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}
