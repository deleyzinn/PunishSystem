package me.deley.punish.listener;

import me.deley.punish.PunishMain;
import me.deley.punish.punishment.PunishManager;
import me.deley.punish.punishment.Punishment;
import me.deley.punish.utils.DateUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Calendar;
import java.util.Date;


/**
 * Listener para o funcionamento do ban
 */
public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        Punishment ban = PunishMain.getInstance().getDataManager().getBan(event.getName());
        boolean join = false;
        if (ban != null) {
            if (ban.hasExpiration()) {
                if (ban.getExpirationTime() < System.currentTimeMillis()) {
                    join = true;
                    PunishMain.getInstance().getDataManager().pardonBan(event.getName());
                }
            }
        } else {
            join = true;
        }
        if (!join) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            String reason = (ban.hasCustomReason() ? ban.getCustomReason() :
                    ban.getCategory().getReason());
            if (!ban.hasExpiration())

                event.setKickMessage("§cVocê está permanentemente banido do servidor.\n"
                        + "\n"
                        + "Motivo: " + reason
                        + "\nData: " + ban.toDateApplied()
                        + "\nID do banimento: " + ban.getBanID()
                        + "\n"
                        + "\n§eIsso foi um engano? Solicite seu §b§lAPPEAL§e em §6discord.gg/servidor");
            else
                event.setKickMessage("§cVocê está temporariamente banido do servidor.\n"
                        + "\n"
                        + "Motivo: " + reason
                        + "\nData: " + ban.toDateApplied()
                        + "\nExpira em: " + DateUtil.getDifferenceFormat(ban.getExpirationTime())
                        + "\nID do banimento: " + ban.getBanID()
                        + "\n"
                        + "\n§eIsso foi um engano? Solicite seu §b§lAPPEAL§e em §6discord.gg/servidor");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        for (int i = 0; i < 60; i++)
            Bukkit.broadcastMessage("");
    }
}
