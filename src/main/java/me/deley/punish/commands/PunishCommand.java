package me.deley.punish.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.deley.punish.PunishMain;
import me.deley.punish.punishment.*;
import me.deley.punish.utils.BanIDGen;
import me.deley.punish.utils.DateUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("p|punish|punir")
@CommandPermission("deley.punish.use")
public class PunishCommand extends BaseCommand {

    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "/punish [ban:mute]");
    }

    @Subcommand("mute")
    @CommandAlias("mutar|silenciar")
    @CommandCompletion("@mutetypes @players -s")
    @CommandPermission("deley.punish.mute")
    public static void onMute(CommandSender sender, Player player, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + "Sintaxe correta: /punish mute (categoria) (player) [-s] [motivo]");
            return;
        }
        PunishmentConstructor constructor = PunishLoader.getByName(args[0]);
        if (constructor == null) {
            sender.sendMessage(ChatColor.RED + "A categoria especificada não foi encontrada. '(" + args[0] + ")'.");
            return;
        }
        if (constructor.getType() != PunishmentType.MUTE) {
            sender.sendMessage(ChatColor.RED + "A categoria " + constructor.toString().toUpperCase() + " não pertence a punição do tipo MUTE");
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Invalid target.");
            return;
        }
        Punishment mute = PunishMain.getInstance().getDataManager().getMute(target.getUniqueId());
        if (mute != null) {
            sender.sendMessage(ChatColor.RED + "O alvo já possui um mute ativo.");
            return;
        }
        if (args.length == 2) {
            mute = new Punishment(constructor, target.getName(), target.getUniqueId(), sender.getName(), (sender instanceof Player) ? player.getUniqueId() : null, -1L, System.currentTimeMillis(), null, null, null);
            if (PunishMain.getInstance().getDataManager().punishMute(mute)) {
                Bukkit.broadcastMessage(ChatColor.RED + "§c§lPUNISH §fO jogador §c" + target.getName() + " §ffoi mutado permanentemente");
                Bukkit.broadcastMessage(ChatColor.RED + "§c§lPUNISH §fMotivo: §c" + constructor.getReason() + " §f.");
                if (target.isOnline()) {
                    target.sendMessage(ChatColor.RED + "Você foi mutado via requisição de um staffer.");
                    target.playSound(target.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                }

            }
        }
        if (args.length >= 3) {
            if (isInArgument("-s", args, 2)) {
                mute = new Punishment(constructor, target.getName(), target.getUniqueId(), sender.getName(), (sender instanceof Player) ? player.getUniqueId() : null, Long.parseLong(constructor.getTime()), System.currentTimeMillis(), null, null, null);
            } else {
                StringBuilder builder = new StringBuilder();
                for (int i = 2; i < args.length; i++)
                    builder.append(args[i]).append(i + 1 >= args.length ? "" : " ");
                mute = new Punishment(constructor, target.getName(), target.getUniqueId(), sender.getName(), (sender instanceof Player) ? player.getUniqueId() : null, Long.parseLong(constructor.getTime()), System.currentTimeMillis(), builder.toString(), null, null);
            }
            if (PunishMain.getInstance().getDataManager().punishMute(mute)) {
                if (target.isOnline()) {
                    target.sendMessage(ChatColor.RED + "Você foi mutado via requisição de um staffer.");
                    target.playSound(target.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                }
            }
        }
    }


    @Subcommand("ban")
    @CommandAlias("ban|banir")
    @CommandCompletion("@bantypes @players -s")
    @CommandPermission("deley.punish.ban")
    public static void onBan(CommandSender sender, Player player, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cSintaxe correta: /punish ban (categoria) (player) [-s, motivo]");
            return;
        }
        PunishmentConstructor constructor = PunishLoader.getByName(args[0]);
        if (constructor == null) {
            sender.sendMessage(ChatColor.RED + "A categoria especificada não foi encontrada. '(" + args[0] + ")'.");
            return;
        }
        if (constructor.getType() != PunishmentType.BAN) {
            sender.sendMessage(ChatColor.RED + "A categoria " + constructor.toString().toUpperCase() + " não pertence a punição do tipo BAN.");
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Invalid target.");
            return;
        }
        Punishment ban = PunishMain.getInstance().getDataManager().getBan(target.getUniqueId());
        if (ban != null) {
            sender.sendMessage(ChatColor.RED + "O alvo já possui um BAN ativo.");
            return;
        }
        String banID = BanIDGen.createBanID();
        if (args.length == 2) { //(categoria) (player)
            ban = PunishMain.getInstance().getDataManager().punish(constructor, target.getUniqueId(), target.getName(), sender.getName(), (sender instanceof Player) ? player.getUniqueId() : null, null, null, null, banID);
            PunishManager.announceBan(ban);
            if (target.isOnline())
                PunishManager.kickBan(ban, false, target);
        } else if (args.length >= 3) { //(categoria) (player) [-s, motivo]
            if (isInArgument("-s", args, 2)) {
                ban = PunishMain.getInstance().getDataManager().punish(constructor, target.getUniqueId(), target.getName(), sender.getName(), (sender instanceof Player) ? player.getUniqueId() : null, null, null, null, banID);
            } else {
                StringBuilder builder = new StringBuilder();
                for (int i = 2; i < args.length; i++)
                    builder.append(args[i]).append(i + 1 >= args.length ? "" : " ");
                ban = PunishMain.getInstance().getDataManager().punish(constructor, target.getUniqueId(), target.getName(), sender.getName(), (sender instanceof Player) ? player.getUniqueId() : null, null, builder.toString(), null, banID);
                PunishManager.announceBan(ban);
            }
            if (target.isOnline())
                PunishManager.kickBan(ban, false, target);
        } else if (args.length >= 3) {  //(categoria) (player) [-s] [motivo]
            StringBuilder builder = new StringBuilder();
            if (isInArgument("-s", args, 2)) {
                for (int i = 4; i < args.length; i++)
                    builder.append(args[i]).append(i + 1 >= args.length ? "" : " ");
                ban = PunishMain.getInstance().getDataManager().punish(constructor, target.getUniqueId(), target.getName(), sender.getName(), (sender instanceof Player) ? player.getUniqueId() : null, null, builder.toString(), null, banID);
                if (target.isOnline())
                    PunishManager.kickBan(ban, false, target);
            }
        }
    }

    public static boolean isInArgument(String s, String[] args, Integer... i) {
        for (Integer x : i) {
            if (!args[x].equalsIgnoreCase(s))
                continue;
            return true;
        }
        return false;
    }
}
