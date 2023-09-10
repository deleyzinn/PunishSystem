package me.deley.punish.data.yaml;

import me.deley.punish.PunishMain;
import me.deley.punish.data.DataManager;
import me.deley.punish.punishment.Punishment;
import me.deley.punish.utils.Configuration;
import me.deley.punish.utils.ConfigurationUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Classe criada para fazer o controle dos dados em YAML (Arquivo local)
 * Utilizada para armazenar punições no banco de dados YAML (Arquivo local)
 */

// ---> INCOMPLETA INCOMPLETA INCOMPLETA INCOMPLETA INCOMPLETA INCOMPLETA <---
public class YAMLData implements DataManager {
    private Configuration config;

    @Override
    public void init() throws Exception {
        this.config = new ConfigurationUtils(new File(PunishMain.getInstance().getDataFolder(), "datacenter"), "playerdata.yml");
    }

    @Override
    public void close() throws Exception {
        config = null;
    }

    @Override
    public void wipeAll() throws Exception {
        config.getConfigFile().delete();
        init();
    }

    @Override
    public void pardonBan(String name) {

    }

    @Override
    public void pardonMute(String name) {

    }

    @Override
    public Punishment getBan(String name) {
        return null;
    }

    @Override
    public Punishment getMute(String name) {
        return null;
    }

    @Override
    public Punishment checkByID(String id) {
        return null;
    }

    @Override
    public boolean punishMute(Punishment mute) {
        return false;
    }

    @Override
    public boolean punishBan(Punishment ban) {
        return false;
    }

    @Override
    public void checkBannedPlayers() {

    }

    @Override
    public void checkMutedPlayers() {

    }

    @Override
    public boolean existID(String id) {
        return false;
    }
}
