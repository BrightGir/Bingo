package me.bright.mbingo.configs;

import me.bright.mbingo.games.GameMode;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConfig extends BConfig{

    public DatabaseConfig(File dataFolder) {
        super(dataFolder, "database");
    }

    @Override
    protected void values() {
        dataConfig("database.host","localhost");
        dataConfig("database.port",3306);
        dataConfig("database.database","bingo");
        dataConfig("database.user","root");
        dataConfig("database.password","root");
    }
}
