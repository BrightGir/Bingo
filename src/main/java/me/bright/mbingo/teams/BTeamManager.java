package me.bright.mbingo.teams;

import me.bright.mbingo.BingoGame;
import me.bright.skylib.teams.TeamColor;
import me.bright.skylib.teams.TeamManager;

public class BTeamManager extends TeamManager {

    private BingoGame game;

    public BTeamManager(BingoGame game) {
        super(game);
        this.game = game;
    }

    @Override
    public void registerTeams(int teamCount) {
        int counter = 0;
        for (TeamColor teamColor : TeamColor.values()) {
            if(counter == teamCount) {
                break;
            }
            getTeamsMap().put(teamColor,new BTeam(teamColor,game.getTeamSize()));
            counter++;
        }
    }

}
