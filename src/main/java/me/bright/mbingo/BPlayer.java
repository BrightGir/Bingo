package me.bright.mbingo;

import me.bright.mbingo.teams.BTeam;
import me.bright.skylib.SPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class BPlayer extends SPlayer {


    private static HashMap<UUID,BPlayer> players = new HashMap<>();
  //  private BTeam team;
    private int finded;
    private Location gameSpawnLocation;
    private int respawnSeconds;

    public BPlayer(Player player) {
        super(player);
        Bukkit.getLogger().info("WI PUT");
        finded = 0;
        setRespawn(60);
        if(player.hasPermission("bingo.respawn55")) {
            setRespawn(55);
            return;
        }
        if(player.hasPermission("bingo.respawn50")) {
            setRespawn(50);
            return;
        }
        if(player.hasPermission("bingo.respawn45")) {
            setRespawn(45);
            return;
        }
        if(player.hasPermission("bingo.respawn40")) {
            setRespawn(40);
            return;
        }
        if(player.hasPermission("bingo.respawn35")) {
            setRespawn(35);
            return;
        }
        if(player.hasPermission("bingo.respawn30")) {
            setRespawn(30);
            return;
        }
    }

    public static void create(Player player) {
        getPlayersMap().put(player.getUniqueId(),new BPlayer(player));
    }

    public void setRespawn(int respawnSeconds) {
        this.respawnSeconds = respawnSeconds;
    }

    public int getRespawn() {
        return respawnSeconds;
    }

    public Location getGameSpawnLocation() {
        return gameSpawnLocation;
    }

    public void setGameSpawnLocation(Location gameSpawnLocation) {
        this.gameSpawnLocation = gameSpawnLocation;
    }


    public void setFinded(int finded) {
        this.finded = finded;
    }


    public void addFinded() {
        finded++;
    }

    public int getFinded() {
        return finded;
    }




}
