package me.deley.punish.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.deley.punish.PunishMain;
import me.deley.punish.punishment.Punishment;
import me.deley.punish.utils.DateUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandAlias("banid|checkid|checkban|getban")
@CommandPermission("ps.punish.checkban")
public class CheckCommand extends BaseCommand {

    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "/checkban (BanID)");
    }

    @Default
    public void checkBan(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Sintaxe correta: /checkban (BAN ID)");
            return;
        }
        Punishment ban = PunishMain.getInstance().getDataManager().checkByID(args[0]);
        if (ban == null) {
            sender.sendMessage(ChatColor.RED + "O ID informado não foi encontrado no banco de dados.");
            return;
        }
        sender.sendMessage(" ");
        sender.sendMessage("§bInformações referentes ao ID: " + ban.getBanID());
        sender.sendMessage(" ");
        sender.sendMessage("§eJogador banido: §b" + ban.getPlayerName());
        sender.sendMessage("§eCausa do ban: §b" + ban.getCategory().getReason() + (ban.hasCustomReason() ? " " : " - " + ban.getCustomReason()));
        sender.sendMessage("§eData: §b" + ban.toDateApplied());
        sender.sendMessage("§eResponsável: §b" + ban.getSenderName());
        sender.sendMessage(" ");

    }
}
