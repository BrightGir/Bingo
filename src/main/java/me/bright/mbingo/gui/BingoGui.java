package me.bright.mbingo.gui;

import me.bright.mbingo.BPlayer;
import me.bright.mbingo.BingoGame;
import me.bright.mbingo.teams.BTeam;
import me.bright.skylib.SPlayer;
import org.bukkit.entity.Player;

public class BingoGui extends GUI {

    private BingoGame game;
    private BPlayer player;

    public BingoGui(BingoGame game, Player holder) {
        super(holder, "Предметы", 5);
        this.game = game;
        this.player = (BPlayer) SPlayer.getPlayer(holder);
        init();
    }

    public void init() {
        int[] idxs = {12,13,14,21,22,23,30,31,32};
        for(int c = 1; c<=9;c++) {
            this.setItem(((BTeam)player.getTeam()).getBingo().get(c),idxs[c-1]);
        }
    }

}
