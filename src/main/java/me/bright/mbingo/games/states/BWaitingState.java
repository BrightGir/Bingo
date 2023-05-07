package me.bright.mbingo.games.states;

import me.bright.mbingo.BPlayer;
import me.bright.mbingo.BingoGame;
import me.bright.mbingo.utils.ItemBuilder;
import me.bright.skylib.SPlayer;
import me.bright.skylib.game.states.WaitingState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class BWaitingState extends WaitingState {

    private int secondsLeft;
    private BukkitTask counterTask;
    private boolean counting;
  //  private WaitingListener listener;

    public BWaitingState(BingoGame game) {
        super(game);
        //listener = new WaitingListener(this.getGame());
    }

    @Override
    public int getCounterSeconds() {
        return 40;
    }

    @Override
    public void startState() {

    }

    @Override
    public void endAction() {

    }

    @Override
    public void setDefaultStateOfPlayer(Player player) {
        setDefaultItems(player);
        int pc = getGame().getPlayersSize();
        if(!counting && pc >= this.getGame().getTeamSize()*5) {
            startCounting();
            return;
        }
        if(counting && pc >= this.getGame().getTeamSize()*6 && secondsLeft > 11) {
            this.secondsLeft = 10;
            return;
        }
        BPlayer bp = (BPlayer) SPlayer.getPlayer(player);
      //  Bukkit.getLogger().info("in setdef state waiting");
        getGame().getScoreboardManager().setBoard(bp);
    }

    @Override
    public int getUpdateScoreboardDelay() {
        return 0;
    }

    private void setDefaultItems(Player player) {
        player.getInventory().setItem(0,new ItemBuilder(Material.NETHER_STAR)
                //   .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .setColoredName("&aВыбрать команду")
                .create());
        player.getInventory().setItem(8,new ItemBuilder(Material.MAGMA_CREAM)
                // .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .setColoredName("&cВыйти из игры")
                .create());
    }

    //  @Override
  //  public void startState() {
  //      secondsLeft = getDefaultSecondsLeft();
  //  }
//
  //  @Override
  //  public void end() {
//
  //  }
  //
  //  @Override
  //  public void addPlayer(Player player) {
  //      setDefaultItems(player);
//
  //      int pc = getGame().getPlayersSize();
  //      if(!counting && pc >= this.getGame().getTeamSize()*5) {
  //          startCounting();
  //          return;
  //      }
  //      if(counting && pc >= this.getGame().getTeamSize()*6 && secondsLeft > 11) {
  //          this.secondsLeft = 10;
  //          return;
  //      }
  //      BPlayer bp = (BPlayer) SPlayer.getPlayer(player);
  //      ScoreboardManager.setBoard(bp,getGame(),getEnum());
  //  }
//
  //  @Override
  //  public void removePlayer(Player player) {
  //      getGame().removePlayer(player);
  //      if(getGame().getPlayersSize() < getGame().getTeamSize()*5) {
  //          stopCounting();
  //      }
  //  }
//
  //  public int getDefaultSecondsLeft() {
  //      return 40;
  //  }
//
  //  private void setDefaultItems(Player player) {
  //      player.getInventory().setItem(0,new ItemBuilder(Material.NETHER_STAR)
  //              //   .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
  //              .setColoredName("&aВыбрать команду")
  //              .create());
//
  //      player.getInventory().setItem(8,new ItemBuilder(Material.MAGMA_CREAM)
  //              // .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
  //              .setColoredName("&cВыйти из игры")
  //              .create());
  //  }
/*





    public int getSecondsLeft() {
        return secondsLeft;
    }

    public boolean isCounting() {
        return counting;
    }


    public void startCounting() {
        this.counting = true;
        this.secondsLeft = getDefaultSecondsLeft();
        counterTask = new BukkitRunnable() {
            @Override
            public void run() {
                secondsLeft--;
                if(secondsLeft == 15 || secondsLeft == 10 || (secondsLeft <= 5 && secondsLeft >= 1)) {
                    sendPlayersColorMessage("&FИгра начинается через &e" + secondsLeft + " &f" + Messenger.correct(secondsLeft,"секунду",
                            "секунды","секунд"));
                }
                if(secondsLeft <= 0) {
                    WaitingState.this.getGame().setState(GameState.ACTIVEGAME);
                    this.cancel();
                }
            }
        }.runTaskTimer(this.getGame().getArena().getPlugin(),20,0);
    }

    private void sendPlayersColorMessage(String message) {
        this.getGame().getPlayers().forEach(player -> {
            Messenger.send(player,Messenger.color(message));
        });
    }

    public void stopCounting() {
        counting = false;
        if(counterTask != null && !counterTask.isCancelled()) {
            counterTask.cancel();
        }
    }
    */

}
