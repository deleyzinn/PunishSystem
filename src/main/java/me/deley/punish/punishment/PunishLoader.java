package me.deley.punish.punishment;

import me.deley.punish.PunishMain;
import me.deley.punish.utils.ConfigurationUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe responsável por fazer o carregamento da categories.yml
 */

public class PunishLoader {
    private static final List<PunishmentConstructor> LIST = new LinkedList<>();

    public static int size() {
        List<PunishmentConstructor> list = LIST;
        synchronized (list) {
            return LIST.size();
        }
    }

    public static PunishmentConstructor get(int index) {
        List<PunishmentConstructor> list = LIST;
        synchronized (list) {
            return LIST.get(index);
        }
    }
    public static synchronized PunishmentConstructor getByName(String name) {
        List<PunishmentConstructor> list = LIST;
        synchronized (list) {
            for (PunishmentConstructor next : LIST) {
                if (!next.getName().equalsIgnoreCase(name)) continue;
                return next;
            }
            return null;
        }
    }

    static {
        ConfigurationUtils config = PunishMain.getInstance().getPunishManager().getConfig();
        for (String punishments : config.getConfig().getConfigurationSection("punishments").getKeys(false)) {
            LIST.add((new PunishmentConstructor(punishments,config.getConfig().getString("punishments." + punishments + ".time"), config.getConfig().getString("punishments." + punishments + ".description"), config.getConfig().getString("punishments." + punishments + ".type"))));
        }
        if (!LIST.isEmpty()) {
            Collections.sort(LIST, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        }
    }
}
