package me.bright.mbingo.database;

import me.bright.mbingo.Bingo;
import me.bright.mbingo.BingoGame;
import me.bright.skylib.database.Creatable;
import me.bright.skylib.database.DataType;
import me.bright.skylib.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class GameConnectorMySQL extends MySQL implements Creatable {

    private FileConfiguration config;
    private String table;

    public GameConnectorMySQL(FileConfiguration conf) {
        config = conf;
        table = conf.getString("settings.db.games_table");
    }

    public String getTable() {
        return table;
    }

    @Override
    public String getDbHost() {
        return config.getString("settings.db.host");
    }

    @Override
    public String getDbName() {
        return config.getString("settings.db.dbname");
    }

    @Override
    public String getTableName() {
        return table;
    }

    public void insertGame(BingoGame game) {
        String state = (game.getState() == null) ? null : game.getState().getEnum().toString();
        execute("INSERT INTO " + getTable() +
                        " (" + LDbType.GAMENAME.getDbStringName() + ", " +
                        LDbType.GAME_MODE.getDbStringName() + ", " +
                        LDbType.PLAYERS_COUNT.getDbStringName() + ", " +
                        LDbType.HOSTPORT.getDbStringName() + ", " +
                        LDbType.STATUS + ") VALUES(?, ?, ?, ?, ?);",
                game.getName(), game.getMode().toString(), game.getPlayersSize(), game.getHostport(), state);

    }

    public void updateInformation(BingoGame game) {
        String gamestate = game.getState() == null ? null : game.getState().getEnum().toString();
        String query = "UPDATE " + table + " SET "
                + LDbType.PLAYERS_COUNT.getDbStringName() + " = " + game.getPlayersSize() +
                ", " + LDbType.STATUS.getDbStringName() + " = \"" + gamestate +
                "\" WHERE " + LDbType.GAMENAME.getDbStringName() + " = \"" + game.getName() + "\";";
       // Bukkit.getLogger().info(query);
        execute(query);
    }

    @Override
    public int getDbPort() {
        return config.getInt("settings.db.port");
    }

    @Override
    public String getDbUser() {
        return config.getString("settings.db.user");
    }

    @Override
    public String getDbPassword() {
        return config.getString("settings.db.password");
    }

    @Override
    public String getCreateTableString() {
        String query =  "CREATE TABLE IF NOT EXISTS " + table + "(" +
                LDbType.HOSTPORT.getDbStringName() + " varchar(36) NOT NULL," +
                LDbType.GAMENAME.getDbStringName() + " varchar(36) NOT NULL," +
                LDbType.STATUS.getDbStringName() + " varchar(36)," +
                LDbType.GAME_MODE.getDbStringName() + " varchar(36) NOT NULL," +
                LDbType.PLAYERS_COUNT.getDbStringName() + " int(11) NOT NULL," +
                "PRIMARY KEY (" + LDbType.GAMENAME.getDbStringName() + ")" +
                ");";
        return  query;
    }
}
