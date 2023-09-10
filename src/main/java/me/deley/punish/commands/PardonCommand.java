package me.deley.punish.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.deley.punish.PunishMain;
import me.deley.punish.punishment.Punishment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("pardon")
@CommandPermission("ps.pardon.use")
public class PardonCommand extends BaseCommand {

    @Subcommand("ban")
    @CommandCompletion("@bannedplayers")
    public static void pardonBan(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cSintaxe correta: /pardon ban (player)");
            return;
        }
        Punishment ban = PunishMain.getInstance().getDataManager().getBan(args[0]);
        if (ban == null) {
            sender.sendMessage("§cO target não está banido.");
            return;
        }
        PunishMain.getInstance().getDataManager().pardonBan(args[0]);
        PunishMain.getInstance().getPunishManager().checkBannedPlayers();
        sender.sendMessage("§aVocê desbaniu o jogador §e" + args[0]);
    }

    @Subcommand("mute")
    @CommandCompletion("@mutedplayers")
    public static void pardonMute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cSintaxe correta: /pardon ban (player)");
            return;
        }
        Punishment ban = PunishMain.getInstance().getDataManager().getMute(args[0]);
        if (ban == null) {
            sender.sendMessage("§cO target não está mutado.");
            return;
        }
        PunishMain.getInstance().getDataManager().pardonMute(args[0]);
        PunishMain.getInstance().getPunishManager().checkMutedPlayers();
        sender.sendMessage("§aVocê desmutou o jogador §e" + args[0]);
    }
}
