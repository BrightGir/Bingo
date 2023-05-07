package me.bright.mbingo.games.states;

import me.bright.mbingo.BPlayer;
import me.bright.mbingo.Bingo;
import me.bright.mbingo.BingoGame;
import me.bright.mbingo.teams.BTeam;
import me.bright.mbingo.utils.ItemBuilder;
import me.bright.skylib.SPlayer;
import me.bright.skylib.game.GameState;
import me.bright.skylib.teams.Team;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class BActiveState extends me.bright.skylib.game.states.ActiveState {

    private int endSeconds;
    private BukkitTask counterTask;

    public BActiveState(BingoGame game) {
        super(game);
    }


    @Override
    public void actionStartState() {
        distributePlayersToTeams();
        updateInventories();
        setBingoForTeams();
        startEndCounter();
     //   setBoards();
        spawnPlayers();
        getGame().getWorld().setTime(0);
    }

    @Override
    public void endAction() {
        counterTask.cancel();
    }

    @Override
    public void setDefaultStateOfPlayer(Player player) {
        player.setFoodLevel(20);
        player.setHealth(player.getMaxHealth());
        player.setLevel(0);
        getGame().getScoreboardManager().setBoard(SPlayer.getPlayer(player));
    }

    @Override
    public int getUpdateScoreboardDelay() {
        return 1;
    }


    private void spawnPlayers() {
        World world = getGame().getWorld();
        List<Location> locations = new ArrayList<>();
        locations.add(new Location(world,0,40,100));
        locations.add(new Location(world,100,40,0));
        locations.add(new Location(world,100,40,100));
        locations.add(new Location(world,200,40,100));
        locations.add(new Location(world,100,40,200));
        locations.add(new Location(world,200,40,200));
        int idx = 0;
        for(Team team: getGame().getTeamManager().getTeams()) {
            for(UUID playerUUID: team.getPlayersUUID()) {
                Player p = Bukkit.getPlayer(playerUUID);
                Location loc = locations.get(idx);
                if(world.getBlockAt(loc).getType() == Material.WATER) {
                    while(world.getBlockAt(loc).getType() == Material.WATER) {
                  //      Messenger.broadcast("water");
                        loc.add(5,-4,0);
                    }
                }
                loc.add(0,100,0);
                if(world.getBlockAt(loc.clone().add(0,-2,0)).getType() == Material.AIR) {
                    while(world.getBlockAt(loc).getType() == Material.AIR) {
                        loc.add(0,-2,0);
                    }
                    loc.add(0,2,0);
                }
                p.teleport(loc);
                BPlayer.getPlayer(p).setGameSpawnLocation(loc);
            }
            idx++;
        }
    }

    private void updateInventories() {
        getGame().getPlayers().forEach(pl -> {
            pl.getInventory().clear();
            pl.getInventory().setItem(8,new ItemBuilder(Material.PAPER)
                    //   .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .setColoredName("&eПредметы")
                    .setLore(Arrays.asList("§7Здесь находятся предметы,","§7которые вам нужно собрать"))
                    .create());
            pl.setHealth(20D);
            pl.setGameMode(GameMode.SURVIVAL);
            pl.setFoodLevel(20);
        });
    }

    private void setBingoForTeams() {
        Bingo.generate((BingoGame) getGame());
        getGame().getTeamManager().getTeams().forEach(team -> {
            ((BTeam)team).setBingo(new Bingo());
        });
    }

    private void startEndCounter() {
        endSeconds = 60*((BingoGame)getGame()).getActiveGameDurationMinutes();
        counterTask = new BukkitRunnable() {

            @Override
            public void run() {
                endSeconds--;
                if(endSeconds <= 0) {
                    BActiveState.this.getGame().setState(GameState.END);
                    this.cancel();
                }
            }

        }.runTaskTimer(getGame().getArena().getPlugin(),0,20L);
    }



  //private void setBoards() {
  //    //  getGame().getArena().getPlugin().getLogger().info("players size " + getGame().getPlayers().size());
  //    for (Player p : getGame().getPlayers()) {
  //        BPlayer pl = BPlayer.getPlayer(p);

  //        ScoreboardManager.setBoard(pl,getGame(),getEnum());
  //    }
  //}

    public int getSecondsToEnd() {
        return endSeconds;
    }

  // @Override
  // public void end() {
  //     if(counterTask != null && !counterTask.isCancelled()) {
  //         getGame().setWinSeconds( (getGame().getDurationMinutes()*60) - ((this).getSecondsToEnd()));
  //         counterTask.cancel();
  //     }
  //   //  getGame().unregisterListener(listener);
  // }


    private void distributePlayersToTeams() {
        getGame().getPlayers().forEach(player -> {
            SPlayer bp = SPlayer.getPlayer(player);
            if(!bp.hasTeam()) {
                Bukkit.getLogger().info("IN TEAM");
                Team t = getOptimalTeam();
                getGame().getTeamManager().addPlayer(bp,t);
                SPlayer.getPlayer(player).setTeam(t);
            }
        });
    }

    private Team getOptimalTeam() {
        Team optimalTeam = null;
        int players = -1;
        for (Team team: getGame().getTeamManager().getTeams()) {
            if(!team.isFull() && team.getPlayersCount() > players) {
                optimalTeam = team;
            }
        }
        return optimalTeam;
    }





}
