package me.deley.punish.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.deley.punish.PunishMain;
import me.deley.punish.punishment.PunishManager;
import org.bukkit.command.CommandSender;

@CommandPermission("deley.punish.wipe")
@CommandAlias("wipeban|resetbans|resetall")
public class WipeBansCommand extends BaseCommand {

    @Default
    public void wipeAll(CommandSender sender, String[] args) throws Exception {
        PunishMain.getInstance().getDataManager().wipeAll();
    }

}
