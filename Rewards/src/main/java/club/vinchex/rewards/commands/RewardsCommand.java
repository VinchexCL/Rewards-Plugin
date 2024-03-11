package club.vinchex.rewards.commands;

import club.vinchex.rewards.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class RewardsCommand implements CommandExecutor {
    private final Main plugin;

    public RewardsCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getNotConsole()));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7Use: /redeem <reward>"));
            return true;
        }

        String rewardName = args[0];
        FileConfiguration config = plugin.getConfig();
        List<String> commands = config.getStringList("rewards." + rewardName);

        if (commands == null || commands.isEmpty()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7The reward &6" + rewardName + "&7 doesn''t exist or doesn''t have commands added."));
            return true;
        }

        FileConfiguration playersConfig = plugin.getPlayersConfig();
        List<String> claimedPlayers = playersConfig.getStringList("players." + rewardName);

        Player player = (Player) sender;
        if (claimedPlayers.contains(player.getUniqueId().toString())) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7You have already claimed the reward &6" + rewardName + "&7."));
            return true;
        }

        for (String cmd : commands) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd.replace("%player%", sender.getName()));
        }

        claimedPlayers.add(player.getUniqueId().toString());
        playersConfig.set("players." + rewardName, claimedPlayers);
        plugin.savePlayersConfig();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7Reward &6" + rewardName + "&7 was claimed succesfully."));
        return true;
    }

    private String getNotConsole() {
        StringBuilder sb = new StringBuilder();
        for (String line : plugin.getMessagesConfig().getStringList("not-console")) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}
