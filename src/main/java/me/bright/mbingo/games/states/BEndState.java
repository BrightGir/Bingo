package me.bright.mbingo.games.states;

import me.bright.mbingo.BPlayer;
import me.bright.mbingo.DatedPlayer;
import me.bright.mbingo.MBingo;
import me.bright.mbingo.BingoGame;
import me.bright.mbingo.database.LDbType;
import me.bright.mbingo.teams.BTeam;
import me.bright.mbingo.utils.Messenger;
import me.bright.skylib.SPlayer;
import me.bright.skylib.teams.Team;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class BEndState extends me.bright.skylib.game.states.EndState {

    //private EndGameListener listener;
    private int seconds;
    private BukkitTask counter;

    public BEndState(BingoGame game) {
        super(game);
        setStartCount(false);
    }
    private void updateData() {
        MBingo pl = (MBingo) getGame().getArena().getPlugin();
        if (getGame().getWinner() != null) {
            for (UUID uuid : getGame().getWinner().getPlayersUUID()) {
                int nowGames = (int) pl.getGameInfoMysql().get("SELECT * FROM " + pl.getGameInfoMysql().getTableName() +
                        " WHERE uuid = \"?\";", LDbType.GAMES.getDbStringName(), uuid.toString());
                pl.getGameInfoMysql().get("UPDATE '" + LDbType.GAMES + "' FROM " + pl.getGameInfoMysql().getTableName() +
                        " WHERE uuid = \"?\";", uuid.toString());
                String query = "UPDATE " + pl.getGameInfoMysql().getTableName() + " SET '" + LDbType.GAMES + "' " +
                        "= " + (nowGames+1) + " WHERE uuid = " + uuid.toString() + ";";
                pl.getGameInfoMysql().execute(query);
              //  Player player = Bukkit.getPlayer(uuid);
              //  DatedPlayer dp = DatedPlayer.getPlayer(player);
              //  dp.addGame();
            }
            if (((BTeam)getGame().getWinner()).getBingo().getFinded() != 0) {
                for(UUID uuid: getGame().getWinner().getPlayersUUID()) {
                    int nowWins = (int) pl.getGameInfoMysql().get("SELECT * FROM " + pl.getGameInfoMysql().getTableName() +
                            " WHERE uuid = \"?\";", LDbType.WINS.getDbStringName(), uuid.toString());
                    pl.getGameInfoMysql().get("UPDATE '" + LDbType.WINS + "' FROM " + pl.getGameInfoMysql().getTableName() +
                            " WHERE uuid = \"?\";", uuid.toString());
                    String query = "UPDATE " + pl.getGameInfoMysql().getTableName() + " SET '" + LDbType.WINS + "' " +
                            "= " + (nowWins + 1) + " WHERE uuid = " + uuid.toString() + ";";
                    pl.getGameInfoMysql().execute(query);
                }
            }
        }
    }

    private void setWinners() {
        if(getGame().getWinner() == null) {
            BTeam winner = null;
            int finded = -1;
            for(Team team: getGame().getTeamManager().getTeams()) {

                if (((BTeam)team).getBingo().getFinded() > finded && team.getPlayersCount() >= 1) {
                    finded = ((BTeam)team).getBingo().getFinded();
                    winner = (BTeam) team;
                }
            }
            getGame().setWinner(winner);
        }
    }


    public int getDefaultEndSeconds() {
        return 21;
    }

    private void startCounterToEnd() {
        counter = new BukkitRunnable() {
            @Override
            public void run() {
                seconds--;
                if(seconds == 20 || seconds == 10 || (seconds <= 5 && seconds >= 1)) {
                    sendPlayersColorMessage("&FИгра закончится через &a" + seconds + " " + Messenger.correct(seconds,"секунду",
                            "секунды","секунд"));
                }
                if(seconds <= 0) {
                    BEndState.this.getGame().fullyEnd();
              //      redirectPlayersToLobby();
                    this.cancel();
                }
            }
        }.runTaskTimer(this.getGame().getArena().getPlugin(),0,20L);
    }

    private void sendPlayersColorMessage(String message) {
        this.getGame().getPlayers().forEach(player -> {
            Messenger.send(player,Messenger.color(message));
        });
    }


    private void redirectPlayersToLobby() {
        World world = getGame().getLobbyLocation().getWorld();
        for(Player p: getGame().getPlayers()) {
            p.teleport(new Location(world,world.getSpawnLocation().getX(),world.getSpawnLocation().getY(),world.getSpawnLocation().getZ()));
        }
    }

    private void notifyPlayers() {
        UserManager uManager = ((MBingo) getGame().getArena().getPlugin()).getLuckpermsApi().getUserManager();

        for (Player p: getGame().getPlayers()) {
            Messenger.sendNoPref(p," ");
            Messenger.sendNoPref(p,"&c&lБинго! &aИгра окончена");
            Messenger.sendNoPref(p," ");
            int smin = getGame().getWinSeconds()/60;
            int ssec = getGame().getWinSeconds()%60;
            Messenger.sendNoPref(p,"&fКоманда " + ((getGame().getWinner() == null) ?
                    "&cНет" : getGame().getWinner().getColor().getColoredName() + " &fсобрала все предметы за "
                    + smin + Messenger.correct(smin, " минуту и ", " минуты и "," минут и ")
                    + ssec + Messenger.correct(ssec, " секунду", " секунды"," секунд")));
            if(getGame().getWinner() != null) {

                if(getGame().getTeamSize() != 1) {
                    getGame().getWinner().getPlayersUUID().forEach(uuid -> {
                        Player winner = Bukkit.getPlayer(uuid);
                        if (winner != null) {
                            BPlayer bp = (BPlayer) SPlayer.getPlayer(winner);
                            User tu =
                                    (uManager.isLoaded(winner.getUniqueId()) ? uManager.getUser(winner.getUniqueId())
                                            : uManager.loadUser(winner.getUniqueId()).join());
                            String prefix = (tu.getCachedData().getMetaData().getPrefix() == null) ? "" : tu.getCachedData().getMetaData().getPrefix();
                            Messenger.sendNoPref(p, "  " + prefix + winner.getDisplayName() + "&7: &a" +
                                    bp.getFinded() + Messenger.correct(bp.getFinded(), " предмет", " предмета", " предметов"));
                        }
                    });
                }
            }
            Messenger.sendNoPref(p," ");
        }
    }

    @Override
    public void actionStartState() {
        //getGame().registerListener(listener);
        setWinners();
        seconds = getDefaultEndSeconds();
        startCounterToEnd();
        updateData();
        notifyPlayers();
           if(getGame().getWinner() != null) {
        getGame().getWinner().getPlayersUUID().forEach(uuid -> {
            Player p = Bukkit.getPlayer(uuid);
            p.sendTitle(Messenger.color("&a&lПобеда"), null, 40, 40, 40);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5f, 1f);
        });
           }
    }

    @Override
    public void endAction() {
        counter.cancel();
    }

    @Override
    public void setDefaultStateOfPlayer(Player player) {
        getGame().getScoreboardManager().setBoard(BPlayer.getPlayer(player));
    }

    @Override
    public int getUpdateScoreboardDelay() {
        return 1;
    }

}
