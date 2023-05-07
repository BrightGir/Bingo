package me.bright.mbingo.scoreboard.game;


import me.bright.mbingo.BingoGame;
import me.bright.mbingo.games.states.BWaitingState;
import me.bright.mbingo.utils.Messenger;
import me.bright.skylib.SPlayer;
import me.bright.skylib.game.Game;
import me.bright.skylib.game.GameState;
import me.bright.skylib.game.states.WaitingState;
import me.bright.skylib.scoreboard.game.WaitingSkelet;

public class BWaitingScoreboard extends WaitingSkelet {


    public BWaitingScoreboard(Game game, SPlayer player) {
        super(game, player,"&b&lBingo");
    }


    @Override
    public void updateLines() {
        setLine("        ",10);
        setLine(" §fРежим: §a" + ((BingoGame)getGame()).getMode().getName(),9);
        String team = (getSPlayer().getTeam() == null) ? "§cНет" : getSPlayer().getTeam().getColor().getColoredName();
        setLine(" §fКоманда: " + team,8);
        setLine("    ",7);
        boolean counting = ((WaitingState)getGame().getState()).isCounting();
        String s6 = (counting) ? "Начало через" : "Статус игры";
        String s5 = (counting) ? ((WaitingState)getGame().getState()).getSecondsLeft() + " " + Messenger.correct(((WaitingState)getGame().getState()).getSecondsLeft(),"секунда","секунды","секунд") : "Ожидание игроков";
        setLine(" §6" + s6,6);
        setLine(" §f" + s5,5);
        setLine(" ",4);
        setLine(" §fИгроков: §a" + getGame().getPlayersSize() + "/" + getGame().getMaxPlayers(),3);
        setLine("  ",2);
        //  scoreboard.add(" " + PlaceholderAPI.setPlaceholders(player.getPlayer(),"%bingo_teamcolor%") + player.getPlayer().getName(),2);
        setLine("   §ewww.SkyStorm.pro  ",1);
    }


}