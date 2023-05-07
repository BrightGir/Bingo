package me.bright.mbingo.database;

import me.bright.skylib.database.Creatable;
import me.bright.skylib.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class GameInfoMySQL extends MySQL implements Creatable {

    private String table2;
    private FileConfiguration config;

    public GameInfoMySQL(FileConfiguration conf) {
        table2 = conf.getString("settings.db.info_table");
        config = conf;
    }

    @Override
    public String getCreateTableString() {
        String query2 =  "CREATE TABLE IF NOT EXISTS " + table2 + "(" +
                LDbType.GAMES.getDbStringName() + " int(11) NOT NULL," +
                LDbType.WINS.getDbStringName() + " int(11) NOT NULL," +
                LDbType.KIILS.getDbStringName() + " int(11) NOT NULL," +
                LDbType.UUID.getDbStringName() + " varchar(36) NOT NULL," +
                "PRIMARY KEY (" + LDbType.UUID.getDbStringName() + ")" +
                ");";
      //  Bukkit.getLogger().info(query2);
        return query2;
    }

    public void insertPlayer(Player p) {
        execute("INSERT INTO " + getTableName() +
                        " (" + LDbType.KIILS.getDbStringName() + ", " +
                        LDbType.WINS.getDbStringName() + ", " +
                        LDbType.GAMES.getDbStringName() + ", " +
                        LDbType.UUID + ") VALUES(?, ?, ?, ?);",
                0, 0, 0, p.getUniqueId().toString());
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
        return table2;
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
}
