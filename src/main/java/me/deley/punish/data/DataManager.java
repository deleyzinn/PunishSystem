package me.deley.punish.data;

import me.deley.punish.punishment.Punishment;


/**
 * Interface para facilitar o uso dos bancos de dados.
 * autor: @deley
 */
public interface DataManager {

    void init() throws Exception;

    void close() throws Exception;

    void wipeAll() throws Exception;

    void pardonBan(String name);

    void pardonMute(String name);

    Punishment getBan(String name);

    Punishment getMute(String name);

    Punishment checkByID(String id);

    boolean punishMute(Punishment mute);

    boolean punishBan(Punishment ban);

    void checkBannedPlayers();

    void checkMutedPlayers();

    boolean existID(String id);

}
