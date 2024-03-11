package club.vinchex.rewards.commands;

import club.vinchex.rewards.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminRewardsCommand implements CommandExecutor {
    private final Main plugin;
    private final AddSubCommand addSubCommand;
    private final CreateSubCommand createSubCommand;
    private final ResetSubCommand resetSubCommand;

    public AdminRewardsCommand(Main plugin) {
        this.plugin = plugin;
        this.addSubCommand = new AddSubCommand(plugin);
        this.createSubCommand = new CreateSubCommand(plugin);
        this.resetSubCommand = new ResetSubCommand(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getNotConsole()));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("adminrewards.command") || !player.hasPermission("adminrewards.*")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getNotHavePermission()));
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7Use: /adminrewards <create|add|reset> <reward> [command]"));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("create")) {
            return createSubCommand.onCommand(sender, command, label, args);
        } else if (subCommand.equals("add")) {
            return addSubCommand.onCommand(sender, command, label, args);
        } else if (subCommand.equals("reset")) {
            return resetSubCommand.onCommand(sender, command, label, args);
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7Use: /adminrewards <create|add|reset> <reward> [command]"));
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
