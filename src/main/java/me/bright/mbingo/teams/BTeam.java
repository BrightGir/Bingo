package me.bright.mbingo.teams;

import me.bright.mbingo.Bingo;
import me.bright.skylib.teams.Team;
import me.bright.skylib.teams.TeamColor;
import org.bukkit.entity.Player;

import java.util.*;

public class BTeam extends Team {

   // private int players;
    private Bingo bingo;

    public BTeam(me.bright.skylib.teams.TeamColor color, int maxPlayers) {
        super(color, maxPlayers);
    }

    public Bingo getBingo() {
        return bingo;
    }

    public void setBingo(Bingo bingo) {
        this.bingo = bingo;
    }






}
