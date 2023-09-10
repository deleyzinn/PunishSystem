package me.deley.punish.utils;

import me.deley.punish.PunishMain;
import me.deley.punish.punishment.PunishManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * Classe utilizada para a criação de BAN ID aleatorio.
 */

public class BanIDGen {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    public static String createBanID() {
        Random nextRandom = new Random();
        int randomNumber = nextRandom.nextInt(6 + 1 - 4) + 4;
        Random random;
        String code = "";
        for (int i = 0; i < randomNumber; i++) {
            random = new Random();
            code += ALPHA_NUMERIC_STRING.charAt(random.nextInt(ALPHA_NUMERIC_STRING.length()));

        }

        try {
            PreparedStatement banID = PunishMain.getInstance().getSqlData().getConnection().prepareStatement("SELECT * FROM `punish_bans` WHERE `banID`='" + code + "';");
            ResultSet accQuery = banID.executeQuery();

            if (accQuery.next()) {

                for (int i = 0; i < randomNumber; i++) {
                    random = new Random();
                    code += ALPHA_NUMERIC_STRING.charAt(random.nextInt(ALPHA_NUMERIC_STRING.length() + 3));

                }
            }

            banID.close();
            accQuery.close();
            return code;
        } catch (SQLException e) {
            // TODO: handle exception
        }

        return code;
    }


}

