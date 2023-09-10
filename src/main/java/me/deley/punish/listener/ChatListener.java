package me.deley.punish.listener;

import me.deley.punish.PunishMain;
import me.deley.punish.punishment.Punishment;
import me.deley.punish.utils.DateUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


/**
 * Listener para o funcionamento do mute.
 */
public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled())
            return;
        Player player = event.getPlayer();
        Punishment mute = PunishMain.getInstance().getDataManager().getMute(player.getName());
        if (mute != null) {
            if (!mute.isExpired()) {
                event.setCancelled(true);
                if (mute.hasExpiration()) {
                    player.sendMessage(ChatColor.RED + "Você está mutado temporariamente!");
                    player.sendMessage(ChatColor.RED + "Motivo: " + mute.getCategory().getReason());
                    player.sendMessage(ChatColor.RED + "Expira em: "+ DateUtil.getDifferenceFormat(mute.getExpirationTime()) + ".");
                } else {
                    player.sendMessage(ChatColor.RED + "Você está mutado permanentemente!");
                    player.sendMessage(ChatColor.RED + "Motivo: " + mute.getCategory().getReason());
                }
            } else {

            }
        }
    }
}
