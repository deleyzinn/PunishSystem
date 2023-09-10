package me.deley.punish.punishment;

import lombok.Getter;
import me.deley.punish.PunishMain;
import me.deley.punish.data.DataManager;
import me.deley.punish.data.connection.MySQLCon;
import me.deley.punish.utils.Configuration;
import me.deley.punish.utils.ConfigurationUtils;
import me.deley.punish.utils.DateUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Classe utilizada para gerenciar os dados e salvá-los (ALTERADA PARA A DataManager.java)
 * author: @deley
 */

@Getter
public class PunishManager {
    @Getter
    private List<String> bannedPlayers = new ArrayList<>();
    @Getter
    private List<String> mutedPlayers = new ArrayList<>();
    private ConfigurationUtils config;
    private DataManager manager = PunishMain.getInstance().getDataManager();

    public PunishManager() throws SQLException {
        config = new ConfigurationUtils(PunishMain.getInstance().getDataFolder(), "categories.yml");
    }

    public void checkMutedPlayers() {
        manager.checkMutedPlayers();
    }

    public void checkBannedPlayers() {
        manager.checkBannedPlayers();
    }

    public static void kickBan(Punishment ban, boolean time, Player p) {
        String reason = (ban.hasCustomReason() ? ban.getCustomReason() : ban.getCategory().getReason());
        if (!time)
            p.kickPlayer("§cVocê está permanentemente banido do servidor.\n" + "\n" + "Motivo: " + reason + "\nData: " + ban.toDateApplied() + "\nID do banimento: " + ban.getBanID() + "\n" + "\n§eIsso foi um engano? Solicite seu §b§lAPPEAL§e em §6discord.gg/servidor");
        else
            p.kickPlayer("§cVocê está temporariamente banido do servidor.\n" + "\n" + "Motivo: " + reason + "\nData: " + ban.toDateApplied() + "\nExpira em: " + DateUtil.getDifferenceFormat(ban.getExpirationTime()) + "\nID do banimento: " + ban.getBanID() + "\n" + "\n§eIsso foi um engano? Solicite seu §b§lAPPEAL§e em §6discord.gg/servidor");
    }

    public static void announceBan(Punishment punishment) {
        Bukkit.broadcastMessage("§c§lPUNISH §fO jogador §b" + punishment.getPlayerName() + "§f foi banido.");
        Bukkit.broadcastMessage("§c§lPUNISH §fMotivo: §b" + punishment.getCategory().getReason() + "§f.");
    }

}
