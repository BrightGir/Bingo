package me.bright.mbingo.database;

import me.bright.mbingo.Bingo;
import me.bright.mbingo.BingoGame;
import me.bright.mbingo.MBingo;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DbGameInformationUpdater {


    private BingoGame game;
    private BukkitTask updater;
    public DbGameInformationUpdater(BingoGame game) {
        this.game = game;
    }

    public void start() {
        if(updater == null || (updater != null && updater.isCancelled())) {
            updater = new BukkitRunnable() {
                @Override
                public void run() {
                    ((MBingo)game.getArena().getPlugin()).getGameMySQL().updateInformation(game);
                }
            }.runTaskTimerAsynchronously(game.getArena().getPlugin(),0,30L);
        }
    }

    public void stop() {
        if(updater != null && !updater.isCancelled()) {
            updater.cancel();
        }
    }
}