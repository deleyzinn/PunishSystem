package me.deley.punish.data.connection;

import lombok.Getter;
import lombok.Setter;
import me.deley.punish.PunishMain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Iniciar a conexão com mysql...
 */
public class MySQLCon {
    @Getter
    @Setter
    private Connection connection;
    private String address = PunishMain.getInstance().getMysqlAddress();
    private String user = PunishMain.getInstance().getMysqlUsername();
    private String password = PunishMain.getInstance().getMysqlPassword();
    private String database = PunishMain.getInstance().getMysqlDatabase();
    private int port = PunishMain.getInstance().getMysqlPort();

    public void initializeConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + database + "?useSSL=false", user, password);
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void disconnect()  {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
