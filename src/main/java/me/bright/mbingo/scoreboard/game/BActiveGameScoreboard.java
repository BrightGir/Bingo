package me.bright.mbingo.scoreboard.game;

import me.bright.mbingo.BingoGame;
import me.bright.mbingo.games.states.BActiveState;
import me.bright.mbingo.teams.BTeam;
import me.bright.mbingo.utils.Messenger;
import me.bright.skylib.SPlayer;
import me.bright.skylib.game.Game;
import me.bright.skylib.game.states.ActiveState;
import me.bright.skylib.scoreboard.game.ActiveGameSkelet;

public class BActiveGameScoreboard extends ActiveGameSkelet {

    public BActiveGameScoreboard(Game game, SPlayer player) {
        super(game, player,"&b&lBingo");
    }


    @Override
    public void updateLines() {
        setLine("      ",10);
        setLine(" §fРежим: §a" + (((BingoGame)getGame()).getMode()).getName(),9);
        String team = (getSPlayer().getTeam() == null) ? "§cНет" : getSPlayer().getTeam().getColor().getColoredName();
        setLine(" §fКоманда: " + team,8);
        setLine("    ",7);
        setLine(" §6Окончание игры",6);
        int minutes = (((ActiveState)getGame().getState()).getSecondsToEnd())/60;
        //     game.getArena().getPlugin().getLogger().info("seconds == " + (((ActiveState)game.getState()).getSecondsToEnd()));
        setLine(" §f" + minutes + " " + Messenger.correct(minutes,"минута","минуты","минут"),5);
        setLine(" ",4);
        setLine(" §fПредметов: §a" + ((BTeam)getSPlayer().getTeam()).getBingo().getFinded() + "/" +
                ((BTeam)getSPlayer().getTeam()).getBingo().getMaxItems(),3);
        setLine("  ",2);
        setLine("   §ewww.SkyStorm.pro  ",1);
    }
}
