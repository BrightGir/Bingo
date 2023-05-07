package me.bright.mbingo;



import me.bright.mbingo.MBingo;
import me.bright.mbingo.games.states.BActiveState;
import me.bright.mbingo.games.states.BEndState;
import me.bright.mbingo.games.states.BWaitingState;
import me.bright.mbingo.scoreboard.game.BActiveGameScoreboard;
import me.bright.mbingo.scoreboard.game.BEndGameScoreboard;
import me.bright.mbingo.scoreboard.game.BWaitingScoreboard;
import me.bright.mbingo.teams.BTeamManager;
import me.bright.mbingo.utils.Constants;
import me.bright.skylib.Arena;
import me.bright.skylib.game.Game;
import me.bright.skylib.game.GameMode;
import me.bright.skylib.game.GameState;
import me.bright.skylib.game.states.ActiveState;
import me.bright.skylib.game.states.EndState;
import me.bright.skylib.game.states.WaitingState;
import me.bright.skylib.scoreboard.game.ActiveGameSkelet;
import me.bright.skylib.scoreboard.game.EndGameSkelet;
import me.bright.skylib.scoreboard.game.WaitingSkelet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BingoGame extends Game {

    private MBingo plugin;
    private GameMode mode;
    private String name;
    private String mapName;
    private int minPlayersToStart;
    private int minPlayersToForcestart;
    private double borderCenterX;
    private double borderCenterZ;
    private double borderSize;
    private Location lobbyLoc;
    private int activeGameDurationMinutes;
    public BingoGame(World world, Arena arena, int maxPlayers, int teamSize, String name, GameMode mode, MBingo plugin, String mapName) {
        super(arena, maxPlayers, teamSize,world);
        this.name = name;
        this.mapName = mapName;
        this.mode = mode;
        this.plugin = plugin;
      //  plugin.getWorldManager().saveWorldsConfig();
        plugin.getWorldManager().addWorld(world.getName(), World.Environment.NORMAL,"fd", WorldType.NORMAL,true,null);
     //  this.kills = new HashMap<>();
     //  this.lblocks = new HashMap<>();
     //  deathMathLocations = new ArrayList<>();
        setBroadCastPrefix(Constants.PREFIX);
        setTeamManager(new BTeamManager(this));
        getTeamManager().registerTeams(getMaxPlayers()/getTeamSize());
        //Bukkit.getLogger().info("plugin == null " + String.valueOf(plugin == null));
        //      this.lobbyWorld = WorldUtils.copyWorld(new File(Bukkit.getWorldContainer(),
        //              arena.getLobbyWorldName()),arena.getLobbyWorldName() + "_" + getName());
        //  startGame();

    }

    public void setActiveGameDurationMinutes(int activeGameDurationMinutes) {
        this.activeGameDurationMinutes = activeGameDurationMinutes;
    }

    public void setMinPlayersToStart(int minPlayersToStart) {
        this.minPlayersToStart = minPlayersToStart;
    }

    public void setMinPlayersToForcestart(int minPlayersToForcestart) {
        this.minPlayersToForcestart = minPlayersToForcestart;
    }




    public String getName() {
        return name;
    }

    public String getHostport() {
        String res = ((Bukkit.getIp() == null || Bukkit.getIp().isEmpty())) ? "localhost:" + Bukkit.getPort() : Bukkit.getIp() + ":" + Bukkit.getPort();
        return res;
    }

    public void setLobbyLocation(double x, double y, double z, float yaw, float pitch) {
        lobbyLoc = new Location(getWorld(),x,y,z,yaw,pitch);
    }

    public void setBorderCenter(double x, double z) {
        this.borderCenterX = x;
        this.borderCenterZ = z;
    }
    public void setBorderSize(double size) {
        this.borderSize= size;
    }
    public GameMode getMode() {
        return mode;
    }

    public double getBorderCenterX() {
        return borderCenterX;
    }

    public double getBorderCenterZ() {
        return borderCenterZ;
    }

    public double getBorderSize() {
        return borderSize;
    }

    public int getActiveGameDurationMinutes() {
        return activeGameDurationMinutes;
    }

    @Override
    public int getMinPlayersToStartCounting() {
        return minPlayersToStart;
    }

    @Override
    public int getMinPlayersToSpeedStartCounting() {
        return minPlayersToForcestart;
    }

    @Override
    public WaitingState getNewWaitingState() {
        return new BWaitingState(this);
    }

    @Override
    public ActiveState getNewActiveState() {
        return new BActiveState(this);
    }

    @Override
    public EndState getNewEndState() {
        return new BEndState(this);
    }

    @Override
    public Class<? extends WaitingSkelet> getWaitingScoreboardSkeletClass() {
        return BWaitingScoreboard.class;
    }

    @Override
    public Class<? extends ActiveGameSkelet> getActiveGameScoreboardSkeletClass() {
        return BActiveGameScoreboard.class;
    }

    @Override
    public Class<? extends EndGameSkelet> getEndGameScoreboardSkeletClass() {
        return BEndGameScoreboard.class;
    }

    @Override
    public void initGameWorld() {
        String wname = this.getWorld().getName();
        plugin.getWorldManager().regenWorld(wname,true,true,"авы",true);
    }

    @Override
    public void actionResetGame() {
        getTeamManager().registerTeams(getMaxPlayers()/getTeamSize());
        this.getWorld().getWorldBorder().reset();
        this.getWorld().getWorldBorder().setSize(((BingoGame)this).getBorderSize());
        this.getWorld().getWorldBorder().setCenter(((BingoGame)this).getBorderCenterX(),getBorderCenterZ());
        this.setState(GameState.WAITING);
    }

    public Location getLobbyLocation() {
        lobbyLoc.setWorld(getWorld());
        return lobbyLoc;
    }
    /*
    private Arena arena;
    private int maxPlayers;
    private TeamManager teamManager;
    private int teamSize;
    private State gamestate;
    private Map<GameState, State> states;
    private List<Player> players;
    private Team winner;
    private String serverLobbyName;
    private int winSeconds;

    public BingoGame(Arena arena, int maxPlayers, int teamSize) {
        this.arena = arena;
        this.maxPlayers = maxPlayers;
        this.teamSize = teamSize;
        this.teamManager = new TeamManager(this);
        this.players = new ArrayList<>();
        initStates();
        regListeners();
    }

    public void setWinSeconds(int seconds) {
        this.winSeconds = seconds;
    }


    public int getWinSeconds() {
        return winSeconds;
    }

    public void setServerLobbyName(String serverLobbyName) {
        this.serverLobbyName = serverLobbyName;
    }

    public void redirectToLobby(Player player) {
        Messenger.redirect(arena.getPlugin(),player,serverLobbyName);
    }

    public int getDurationMinutes() {
        return 90;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public void addPlayer(Player player) {
        players.add(player);
        getArena().getPlugin().getServer().getPluginManager().callEvent(new ArenaJoinEvent(player,getArena()));
    }

    public void removePlayer(Player player) {
        players.remove(player);
    // if(getArena() == null) {
    //     Messenger.broadcast("arena null");
    // } else if(getArena().getPlugin() == null) {
    //     Messenger.broadcast("plugin null");
    // } else if (getArena().getPlugin().getServer() == null) {
    //     Messenger.broadcast("server null");
    // } else if (getArena().getPlugin().getServer().getPluginManager() == null) {
    //     Messenger.broadcast("pm null");
    // }
        getArena().getPlugin().getServer().getPluginManager().callEvent(new ArenaLeaveEvent(player,getArena()));
    }

    public int getPlayersSize() {
        return players.size();
    }

    public void regListeners() {
    }

    public Arena getArena() {
        return arena;
    }

    public void registerListener(Listener listener) {
        getArena().getPlugin().getServer().getPluginManager().registerEvents(listener,getArena().getPlugin());
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setState(GameState state) {
        if(state == null) {
            this.gamestate = null;
            return;
        }
        if(gamestate != null) {
            gamestate.end();
        }
        this.gamestate = states.get(state);
        this.gamestate.startState();

    }

    public void fullyEnd() {
        this.setState(null);
        getArena().getPlugin().getWorldManager().regenWorld(getArena().getGameWorld().getName(),
                true,false,getArena().getPlugin().getRandomSeed());
        winner = null;
        for (Player p: getPlayers()) {
            getArena().getPlugin().getServer().getPluginManager().callEvent(new ArenaLeaveEvent(p,getArena()));
        }
        this.getPlayers().clear();
        BingoGame.this.setState(GameState.WAITING);
    }

    private void initStates() {
        states = new HashMap<>();
        states.put(GameState.WAITING,new WaitingState(this));
        states.put(GameState.ACTIVEGAME,new ActiveState(this));
        states.put(GameState.END,new EndState(this));
    }

    public State getState() {
        return gamestate;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }
    */






}
