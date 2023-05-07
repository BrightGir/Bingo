package me.bright.mbingo;



import me.bright.skylib.database.DataType;
import me.bright.skylib.database.MySQL;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.UUID;

public class DatedPlayer {

    private static MySQL database;
    private static HashMap<UUID,DatedPlayer> players = new HashMap<>();
    private int wins;
    private int games;
    private OfflinePlayer player;

    public DatedPlayer(OfflinePlayer player) {
        this.player = player;
        wins = -1;
        games = -1;
        players.put(player.getUniqueId(),this);
    }

 //   public int getWins() {
 //       if(wins == -1 && database != null) {
 //           wins = (int) database.getData(player, DataType.WINS);
 //       }
 //       return wins;
 //   }
//
 //   public int getGames() {
 //       if(games == -1 && database != null) {
 //           games = (int) database.getData(player, DataType.GAMES);
 //       }
 //       return games;
 //   }
//
 //   public void addWin() {
 //       if(database != null && database.getConnection() != null) {
 //           database.addGame(true, player);
 //       }
 //       games++;
 //       wins++;
 //   }
//
 //   public void addGame() {
 //       if(database != null && database.getConnection() != null) {
 //           database.addGame(false, player);
 //       }
 //       games++;
 //   }
//
 //   public static DatedPlayer getPlayer(OfflinePlayer player) {
 //       return (players.get(player.getUniqueId()) == null) ? new DatedPlayer(player) : players.get(player.getUniqueId());
 //   }
//
 //   public static void setDataBase(Database db) {
 //       database = db;
 //   }
//
 //   public static Database getDb() { return database;}


}
