package club.vinchex.rewards;

import club.vinchex.rewards.commands.AdminRewardsCommand;
import club.vinchex.rewards.commands.ResetSubCommand;
import club.vinchex.rewards.commands.RewardsCommand;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {

    private FileConfiguration playersConfig;
    private FileConfiguration messagesConfig;
    @Override
    public void onEnable() {
        loadMessagesConfig();
        loadPlayersConfig();
        saveDefaultConfig();
        getCommand("adminrewards").setExecutor(new AdminRewardsCommand(this));
        getCommand("redeem").setExecutor(new RewardsCommand(this));
        getCommand("create").setExecutor(new AdminRewardsCommand(this));
        getCommand("add").setExecutor(new AdminRewardsCommand(this));
        getCommand("reset").setExecutor(new AdminRewardsCommand(this));
    }

    @Override
    public void onDisable() {
    }


    private void loadPlayersConfig() {
        File playersFile = new File(getDataFolder(), "players.yml");
        if (!playersFile.exists()) {
            saveResource("players.yml", false);
        }

        playersConfig = YamlConfiguration.loadConfiguration(playersFile);
    }

    public FileConfiguration getPlayersConfig() {
        return playersConfig;
    }

    public void savePlayersConfig() {
        try {
            playersConfig.save(new File(getDataFolder(), "players.yml"));
        } catch (IOException e) {
            getLogger().warning(ChatColor.translateAlternateColorCodes('&', "&7[&6&lRewards&7] &7Could not save players config to players.yml"));
        }
    }

    private void loadMessagesConfig() {
        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public FileConfiguration getMessagesConfig() {
        return messagesConfig;
    }

}