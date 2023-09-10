package me.deley.punish;

import co.aikar.commands.BukkitCommandManager;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lombok.Getter;
import me.deley.punish.commands.CheckCommand;
import me.deley.punish.commands.PardonCommand;
import me.deley.punish.commands.PunishCommand;
import me.deley.punish.data.DataManager;
import me.deley.punish.data.connection.MySQLCon;
import me.deley.punish.data.mysql.MySQLData;
import me.deley.punish.data.yaml.YAMLData;
import me.deley.punish.listener.ChatListener;
import me.deley.punish.listener.PlayerJoinListener;
import me.deley.punish.punishment.PunishLoader;
import me.deley.punish.punishment.PunishManager;
import me.deley.punish.punishment.PunishmentConstructor;
import me.deley.punish.punishment.PunishmentType;
import me.deley.punish.utils.DiscordHook;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Getter
public class PunishMain extends JavaPlugin {
    @Getter
    private static PunishMain instance;
    @Getter
    private MySQLCon sqlData;
    private String mysqlAddress;
    private String mysqlDatabase;
    private String mysqlUsername;
    private String mysqlPassword;
    private String discordWebhook;
    private int mysqlPort;
    private PunishManager punishManager;
    private PunishLoader loader;
    @Getter
    private DiscordHook discordHook;
    private DataManager dataManager = null;
    private Gson gson;
    private JsonParser parser;

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
        loadConfigurations();
        //Isso seria caso o YAMLData estivesse pronto....
//        if (getConfig().getBoolean("use-mysql"))
//            dataManager = new MySQLData();
//        else
//            dataManager = new YAMLData();
        dataManager = new MySQLData();
    }

    @Override
    public void onEnable() {
        instance = this;
        try {
            dataManager.init();
            getLogger().log(Level.INFO, "DataManager init.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        discordHook = new DiscordHook(discordWebhook);
        sqlData = new MySQLCon();
        try {
            sqlData.initializeConnection();
        } catch (SQLException e) {
            getServer().shutdown();
            throw new RuntimeException(e);

        }
        try {
            punishManager = new PunishManager();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(new PunishCommand());
        manager.registerCommand(new PardonCommand());
        manager.registerCommand(new CheckCommand());

        registerTabCompletes(manager);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {
        sqlData.disconnect();
    }

    private void loadConfigurations() {
        this.mysqlAddress = this.getConfig().getString("mysql.host");
        this.mysqlDatabase = this.getConfig().getString("mysql.database");
        this.mysqlUsername = this.getConfig().getString("mysql.user");
        this.mysqlPassword = this.getConfig().getString("mysql.password");
        this.mysqlPort = this.getConfig().getInt("mysql.port");
        if (getConfig().getBoolean("discordhook"))
            this.discordWebhook = this.getConfig().getString("discordurl");
    }

    private void registerTabCompletes(BukkitCommandManager manager) {
        //Mute categories complete;
        manager.getCommandCompletions().registerCompletion("mutetypes", context -> {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < PunishLoader.size(); i++) {
                PunishmentConstructor constructor = PunishLoader.get(i);
                if (constructor.getType() != PunishmentType.MUTE) continue;
                list.add(constructor.getName());
            }
            return list;

        });

        //Ban categories complete;
        manager.getCommandCompletions().registerCompletion("bantypes", context -> {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < PunishLoader.size(); i++) {
                PunishmentConstructor constructor = PunishLoader.get(i);
                if (constructor.getType() != PunishmentType.BAN) continue;
                list.add(constructor.getName());
            }
            return list;
        });

        //Punished players complete;
        manager.getCommandCompletions().registerCompletion("bannedplayers", context -> {
            getPunishManager().checkBannedPlayers();
            return getPunishManager().getBannedPlayers();
        });
        manager.getCommandCompletions().registerCompletion("mutedplayers", context -> {
            getPunishManager().checkMutedPlayers();
            return getPunishManager().getMutedPlayers();
        });
    }
}