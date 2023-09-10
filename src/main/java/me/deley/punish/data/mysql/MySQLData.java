package me.deley.punish.data.mysql;

import me.deley.punish.PunishMain;
import me.deley.punish.data.DataManager;
import me.deley.punish.data.connection.MySQLCon;
import me.deley.punish.punishment.PunishLoader;
import me.deley.punish.punishment.Punishment;
import me.deley.punish.punishment.PunishmentConstructor;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;


/**
 * Classe criada para fazer o controle dos dados em MYSQL
 * Utilizada para armazenar punições no banco de dados MYSQL
 */


public class MySQLData implements DataManager {
    private final MySQLCon con = PunishMain.getInstance().getSqlData();

    @Override
    public void init() throws Exception {
        PreparedStatement statementBan = con.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `punish_bans`(`bannedName` VARCHAR(20), `bannedId` VARCHAR(100), `authorName` VARCHAR(20), `authorId` VARCHAR(100), `category` VARCHAR(30),`expirationTime` VARCHAR(50),`appliedDate` VARCHAR(255), `customReason` VARCHAR(255), `ipAddress` VARCHAR(100), `banID` VARCHAR(10))");
        statementBan.execute();
        statementBan.close();
        PreparedStatement statementMute = con.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `punish_mutes`(`mutedName` VARCHAR(20), `mutedId` VARCHAR(100), `authorName` VARCHAR(20), `authorId` VARCHAR(100), `category` VARCHAR(30),`expirationTime` VARCHAR(50),`appliedDate` VARCHAR(255), `customReason` VARCHAR(255), `ipAddress` VARCHAR(100))");
        statementMute.execute();
        statementMute.close();
    }

    @Override
    public void close() {
        con.disconnect();
        Bukkit.getLogger().log(Level.WARNING, "A conexão com MYSQL foi finalizada.");
    }

    @Override
    public void wipeAll() {
        try {
            PreparedStatement statement = con.getConnection().prepareStatement("TRUNCATE TABLE `punish_bans`");
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            PunishMain.getInstance().getLogger().log(Level.SEVERE, "Não foi possível dar WIPE na tabela 'punish_bans'");
            e.printStackTrace();
        }
        try {
            PreparedStatement statement = con.getConnection().prepareStatement("TRUNCATE TABLE `punish_mutes`");
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            PunishMain.getInstance().getLogger().log(Level.SEVERE, "Não foi possível dar WIPE na tabela 'punish_mutes'");
            e.printStackTrace();
        }
    }

    @Override
    public void pardonBan(String name) {
        try {
            PreparedStatement statement = con.getConnection().prepareStatement("DELETE FROM `punish_bans` WHERE `bannedName`='" + name + "';");
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pardonMute(String name) {
        try {
            PreparedStatement statement = con.getConnection().prepareStatement("DELETE FROM `punish_mutes` WHERE `mutedName`='" + name + "';");
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Punishment getBan(String name) {
        Punishment punishment = null;
        try {
            PreparedStatement statement = con.getConnection().prepareStatement("SELECT * FROM `punish_bans` WHERE `bannedName`='" + name + "';");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                PunishmentConstructor category = PunishLoader.getByName(resultSet.getString("category"));
                String authorName = resultSet.getString("authorName");
                String authorId = resultSet.getString("authorId");
                String playerName = resultSet.getString("bannedName");
                String playerId = resultSet.getString("bannedId");
                String reason = resultSet.getString("customReason");
                long time = resultSet.getLong("expirationTime");
                String address = resultSet.getString("ipAddress");
                String banID = resultSet.getString("banID");
                long appliedDate = resultSet.getLong("appliedDate");
                punishment = new Punishment(category, playerName, UUID.fromString(playerId), authorName, UUID.fromString(authorId), time, appliedDate, reason, address, banID);
            }
            return punishment;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Punishment getMute(String name) {
        Punishment punishment = null;
        try {
            PreparedStatement muteStatement = con.getConnection().prepareStatement("SELECT * FROM `punish_mutes` WHERE `mutedName`='" + name + "';");
            ResultSet muteQuery = muteStatement.executeQuery();

            if (muteQuery.next()) {
                PunishmentConstructor category = PunishLoader.getByName(muteQuery.getString("category"));
                String authorName = muteQuery.getString("authorName");
                String authorId = muteQuery.getString("authorId");
                String playerName = muteQuery.getString("mutedName");
                String playerId = muteQuery.getString("mutedId");
                String reason = muteQuery.getString("customReason");
                long time = muteQuery.getLong("expirationTime");
                String address = muteQuery.getString("ipAddress");
                long appliedDate = muteQuery.getLong("appliedDate");
                punishment = new Punishment(category, playerName, UUID.fromString(playerId), authorName, UUID.fromString(authorId), time, appliedDate, reason, address, null);
            }
            muteQuery.close();
            muteStatement.close();
            return punishment;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Punishment checkByID(String id) {
        Punishment punishment = null;
        try {
            PreparedStatement statement = con.getConnection().prepareStatement("SELECT * FROM `punish_bans` WHERE `banID`='" + id + "';");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                PunishmentConstructor category = PunishLoader.getByName(resultSet.getString("category"));
                String authorName = resultSet.getString("authorName");
                String authorId = resultSet.getString("authorId");
                String playerName = resultSet.getString("bannedName");
                String playerId = resultSet.getString("bannedId");
                String reason;
                if (Objects.equals(resultSet.getString("customReason"), "null")) reason = null;
                else reason = resultSet.getString("customReason");
                long time = resultSet.getLong("expirationTime");
                String address = resultSet.getString("ipAddress");
                String banID = resultSet.getString("banID");
                long appliedDate = resultSet.getLong("appliedDate");
                punishment = new Punishment(category, playerName, UUID.fromString(playerId), authorName, UUID.fromString(authorId), time, appliedDate, reason, address, banID);
            }
            resultSet.close();
            statement.close();
            return punishment;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean punishMute(Punishment mute) {
        try {
            PreparedStatement createMute = con.getConnection().prepareStatement("INSERT INTO `punish_mutes` (`mutedName`, `mutedId`, `authorName`, `authorId`, `category`, `expirationTime`, `customReason`, `ipAddress`) VALUES (?,?,?,?,?,?,?,?)");
            createMute.setString(1, mute.getPlayerName());
            createMute.setString(2, mute.getPlayerUniqueId().toString());
            createMute.setString(3, mute.getSenderName());
            createMute.setString(4, mute.getSenderUniqueId().toString());
            createMute.setString(5, mute.getCategory().getName());
            createMute.setLong(6, mute.getExpirationTime());
            createMute.setString(7, mute.getCustomReason());
            createMute.setString(8, mute.getIpAddress());
            createMute.executeUpdate();
            createMute.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean punishBan(Punishment ban) {
        try {
            PreparedStatement createBan = con.getConnection().prepareStatement("INSERT INTO `punish_bans` (`bannedName`, `bannedId`, `authorName`, `authorId`, `category`, `expirationTime`, `customReason`, `ipAddress`, `banID`) VALUES (?,?,?,?,?,?,?,?,?)");
            createBan.setString(1, ban.getPlayerName());
            createBan.setString(2, ban.getPlayerUniqueId().toString());
            createBan.setString(3, ban.getSenderName());
            createBan.setString(4, ban.getSenderUniqueId().toString());
            createBan.setString(5, ban.getCategory().getName());
            createBan.setLong(6, ban.getExpirationTime());
            createBan.setString(7, ban.getCustomReason());
            createBan.setString(8, ban.getIpAddress());
            createBan.setString(9, ban.getBanID());
            createBan.executeUpdate();
            createBan.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void checkMutedPlayers() {
        List<String> mutedPlayers = PunishMain.getInstance().getPunishManager().getMutedPlayers();
        mutedPlayers.clear();
        try {
            PreparedStatement statement = con.getConnection().prepareStatement("SELECT mutedName FROM punish_mutes");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                mutedPlayers.add(resultSet.getString("mutedName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existID(String id) {
        try {
            PreparedStatement banID = PunishMain.getInstance().getSqlData().getConnection().prepareStatement("SELECT * FROM `punish_bans` WHERE `banID`='" + id + "';");
            ResultSet accQuery = banID.executeQuery();
            if (accQuery.next()) {
                return true;
            }
            banID.close();
            accQuery.close();
        } catch (SQLException e) {
            // TODO: handle exception

        }
        return false;
    }

    @Override
    public void checkBannedPlayers() {
        List<String> bannedPlayers = PunishMain.getInstance().getPunishManager().getBannedPlayers();
        bannedPlayers.clear();
        try {
            PreparedStatement statement = con.getConnection().prepareStatement("SELECT bannedName FROM punish_bans");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bannedPlayers.add(resultSet.getString("bannedName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
