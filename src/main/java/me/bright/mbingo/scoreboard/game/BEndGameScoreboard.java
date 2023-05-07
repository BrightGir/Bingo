package me.bright.mbingo.scoreboard.game;

import me.bright.mbingo.BingoGame;
import me.bright.skylib.SPlayer;
import me.bright.skylib.game.Game;
import me.bright.skylib.scoreboard.game.EndGameSkelet;

public class BEndGameScoreboard extends EndGameSkelet {
    public BEndGameScoreboard(Game game, SPlayer player) {
        super(game, player,"&b&lBingo");
    }


    @Override
    public void updateLines() {
        setLine("      ",8);
        setLine(" §fРежим: §a" + (((BingoGame)getGame()).getMode()),7);
        String team = (getSPlayer().getTeam() == null) ? "§cНет" : getSPlayer().getTeam().getColor().getColoredName();
        setLine(" §fКоманда: " + team,6);
        setLine("   ",5);
        setLine(" §6Победила команда",4);
        String winner = (getGame().getWinner() == null) ? "§cНет" : getGame().getWinner().getColor().getColoredName();
        setLine(" " + winner,3);
        setLine("  ",2);
        setLine("   §ewww.SkyStorm.pro  ",1);
    }
}
