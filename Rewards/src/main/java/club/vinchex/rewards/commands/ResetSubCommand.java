package club.vinchex.rewards.commands;

import club.vinchex.rewards.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class ResetSubCommand implements CommandExecutor {
    private final Main plugin;

    public ResetSubCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getNotConsole()));
            return true;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("adminrewards.command.reset") || !p.hasPermission("rewards.*")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getNotHavePermission()));
            return false;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7Use: /adminrewards reset <player|all> <reward>"));
            return true;
        }

        String target = args[1];
        String rewardName = args[2];
        FileConfiguration playersConfig = plugin.getPlayersConfig();

        if (!playersConfig.contains("players." + rewardName)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7The reward &6" + rewardName + " &7doesn't exist."));
            return true;
        }

        if (target.equalsIgnoreCase("all")) {
            List<String> players = playersConfig.getStringList("players." + rewardName);
            players.clear();
            playersConfig.set("players." + rewardName, players);
            plugin.savePlayersConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7The reward &6" + rewardName + " &7can be redeemed again by all players."));
        } else {
            Player player = plugin.getServer().getPlayer(target);
            if (player == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7Player &6" + target + "&7 not found."));
                return true;
            }

            List<String> players = playersConfig.getStringList("players." + rewardName);
            players.remove(player.getUniqueId().toString());
            playersConfig.set("players." + rewardName, players);
            plugin.savePlayersConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7The reward &6" + rewardName + " &7can be redeemed again by player '" + player.getName() + "'."));
        }

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