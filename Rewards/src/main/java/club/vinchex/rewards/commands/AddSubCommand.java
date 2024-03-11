package club.vinchex.rewards.commands;

import club.vinchex.rewards.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AddSubCommand implements CommandExecutor {
    private final Main plugin;
    public AddSubCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getNotConsole()));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("adminrewards.command.add") || !player.hasPermission("adminrewards.*")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getNotHavePermission()));
            return false;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7Use: /adminrewards add <reward> <command>"));
            return true;
        }

        String rewardName = args[1];
        StringBuilder commandBuilder = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            commandBuilder.append(args[i]).append(" ");
        }
        String commandToAdd = commandBuilder.toString().trim();

        FileConfiguration config = plugin.getConfig();
        List<String> commands = config.getStringList("rewards." + rewardName);

        if (commands == null) {
            commands = new ArrayList<>();
        }

        commands.add(commandToAdd);
        config.set("rewards." + rewardName, commands);
        plugin.saveConfig();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7Command added to &6" + rewardName + "&7."));
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