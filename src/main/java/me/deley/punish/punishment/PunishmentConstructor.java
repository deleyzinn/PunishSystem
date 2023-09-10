package me.deley.punish.punishment;

import lombok.Getter;
import me.deley.punish.PunishMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Level;

/**
 * Utilizada para gerenciar a categories.yml
 */

@Getter public class PunishmentConstructor {
    private String name;
    private String time;
    private String reason;
    private PunishmentType type;

    public PunishmentConstructor(String name, String time, String reason, String type) {
        this.name = name;
        this.time = time;
        this.reason = reason;
        try {
            this.type = PunishmentType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, ChatColor.DARK_RED + "[PunishSystem] Ocorreu um erro ao carregar o Punishment: " + name);
            Bukkit.getLogger().log(Level.SEVERE, ChatColor.DARK_RED +"[PunishSystem] O type: " + type + " nao existe. (Utilize apenas BAN/MUTE)!");
            PunishMain.getInstance().getPluginLoader().disablePlugin(PunishMain.getInstance());
        }
    }
}
