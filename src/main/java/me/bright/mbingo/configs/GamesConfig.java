package me.bright.mbingo.configs;

import me.bright.mbingo.games.GameMode;
import me.bright.skylib.configs.SConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

public class GamesConfig extends SConfig {

    public GamesConfig(JavaPlugin plugin) {
        super(plugin.getDataFolder(),"games");
    }

    @Override
    protected void values() {
        dataConfig("blockname","&aЛаки блок");
        dataConfig("games.bingo-1.worldname","game1");
        dataConfig("games.bingo-1.mode","SOLO");
        dataConfig("games.bingo-1.max_players",6);
        dataConfig("games.bingo-1.team_size",1);
        dataConfig("games.bingo-1.min_start",2);
        dataConfig("games.bingo-1.min_forcestart",5);
        dataConfig("games.bingo-1.map_name","Спанчбоб");
        dataConfig("games.bingo-1.lobby_spawn_location","-240, 48, 284, 0, 0");
        dataConfig("games.bingo-1.border_size",1000);
        dataConfig("games.bingo-1.border_center_x",-280);
        dataConfig("games.bingo-1.border_center_z",295);
        dataConfig("games.bingo-1.activeGameDurationMinutes",1);

      //  dataConfig("games.bingo-1.islands_locations", Arrays.asList(
      //          "-280, 44, 295, 0, 0",
      //          "-282, 44, 274, 0, 0",
      //          "-267, 44, 257, 0, 0",
      //          "-267, 44, 257, 0, 0",
      //          "-267, 44, 257, 0, 0",
      //          "-267, 44, 257, 0, 0",
      //          "-267, 44, 257, 0, 0",
      //          "-267, 44, 257, 0, 0"));
    }
}
