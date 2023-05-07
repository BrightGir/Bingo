package me.bright.mbingo;


import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.utils.WorldManager;
import me.bright.mbingo.commands.ForceEndCommand;
import me.bright.mbingo.commands.ForceStartCommand;
import me.bright.mbingo.configs.GamesConfig;
import me.bright.mbingo.configs.BingoConfig;
import me.bright.mbingo.configs.SettingsConfig;
import me.bright.mbingo.database.DbGameInformationUpdater;
import me.bright.mbingo.database.GameConnectorMySQL;
import me.bright.mbingo.database.GameInfoMySQL;
import me.bright.mbingo.listeners.*;
import me.bright.mbingo.utils.Messenger;
import me.bright.skylib.game.GameMode;
import net.luckperms.api.LuckPerms;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;


public final class MBingo extends JavaPlugin {

    private BingoConfig bingoConfig;
    private GameInfoMySQL dbinfo;
    private GameConnectorMySQL db;
    //    private MultiverseCore core;
    //    private MVWorldManager wmanager;
    private List<BingoGame> games;
    private GamesConfig gamesConfig;
    private LuckPerms api;
  //  private Set<String> seeds = new HashSet<>();
    private List<String> seeds = new ArrayList<>();
    private Location mainLobbySpawnLocation;
    private static JavaPlugin plugin;
    private SettingsConfig settingsConfig;
    private String serverLobbyName;
    private WorldManager wmanager;


    @Override
    public void onEnable() {
        plugin = this;
        loadComponents();
        loadConfigs();
         load();
        loadSeeds();
        setupPerms();
        //DatedPlayer.setDataBase(db);
   //     new BingoHolder().register();
        loadGames();
        Bukkit.getWorld("world").setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS,false);
    }

    private void load() {
        settingsConfig = new SettingsConfig(this);
        gamesConfig = new GamesConfig(this);
        try {
            db = new GameConnectorMySQL(settingsConfig.getConfig());
            dbinfo = new GameInfoMySQL(settingsConfig.getConfig());
            db.loadDb();
            dbinfo.loadDb();
            if (db.getConnection() == null) {
                Bukkit.getLogger().warning("[LuckyWars] Database didn't load");
            } else {
                Bukkit.getLogger().info("[LuckyWars] Database has loaded");
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("[LuckyWars] Database didn't loaded");
        }
        serverLobbyName = settingsConfig.getConfig().getString("settings.lobby_server_name");
        //  slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        //   wmanager = ((MultiverseCore)Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core")).getMVWorldManager();
    }

    public GameConnectorMySQL getGameMySQL() {
        return db;
    }

    public GameInfoMySQL getGameInfoMysql() {return dbinfo; }


    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public Location getMainLobbySpawnLocation() {
        return mainLobbySpawnLocation;
    }

    private void loadSeeds() {
        seeds = bingoConfig.getConfig().getStringList("seeds");

    }

    public List<String> getSeeds() {
        return seeds;
    }

    public String getRandomSeed() {

        return String.valueOf(Messenger.rnd(1,Integer.MAX_VALUE));
    }


    private void loadGames() {
        this.games = new ArrayList<>();
        me.bright.skylib.Arena arena = new me.bright.skylib.Arena(this);
        arena.setServerLobbyName(serverLobbyName);
        ConfigurationSection gamesSec = gamesConfig.getConfig().getConfigurationSection("games");
        Iterator<String> it = gamesSec.getKeys(false).iterator();
        //   String lobbyWorldName = gamesConfig.getConfig().getString("lobby_world_name");
        //     arena.setLobbyWorldName(lobbyWorldName);
        while(it.hasNext()) {
            ConfigurationSection gameSec = gamesSec.getConfigurationSection(it.next());
            String worldName = gameSec.getString("worldname");
            // new WorldCreator(worldName).environment(World.Environment.NORMAL).createWorld();
            Bukkit.getLogger().info("Load world, worldName = " + ((worldName == null) ? "null" : worldName));
            Bukkit.createWorld(new WorldCreator(worldName));
            //    Bukkit.unloadWorld(worldName,false);
            // Bukkit.createWorld(new WorldCreator(this.getDataFolder() + File.separator + "maps" + File.separator + worldName));
            //Bukkit.createWorld(new WorldCreator(worldName).environment(World.Environment.NORMAL));
            //World world = new WorldCreator(worldName).environment(World.Environment.NORMAL).createWorld();
            //  Bukkit.getWorlds().add(world);
            int maxPlayers = gameSec.getInt("max_players");
            int teamSize = gameSec.getInt("team_size");
            me.bright.skylib.game.GameMode mode = GameMode.valueOf(gameSec.getString("mode"));
            String mapName = gameSec.getString("map_name");
            BingoGame game = new BingoGame(Bukkit.getWorld(worldName),arena,maxPlayers,teamSize,gameSec.getName(),mode,this,mapName);

            double borderCenterX = gameSec.getDouble("border_center_x");
            double borderCenterZ = gameSec.getDouble("border_center_z");
            double borderSize = gameSec.getDouble("border_size");

            game.setBorderCenter(borderCenterX,borderCenterZ);
            game.setBorderSize(borderSize);
            //  Bukkit.getLogger().info(String.valueOf("world == null + " + game.getWorld() == null));

            for(String sloc: gameSec.getStringList("islands_locations")) {
                String[] loc = sloc.split(", ");
                game.addIslandsLocation(Double.parseDouble(loc[0]),Double.parseDouble(loc[1]),Double.parseDouble(loc[2]),
                        Float.parseFloat(loc[3]),Float.parseFloat(loc[4]));
            }

            //  String[] dloc = gameSec.getString("death_match_location").split(", ");


            int minPlayersToStart = gameSec.getInt("min_start");
            int minPlayersToForceStart = gameSec.getInt("min_forcestart");
            game.setMinPlayersToStart(minPlayersToStart);
            game.setMinPlayersToForcestart(minPlayersToForceStart);
            game.setActiveGameDurationMinutes(gameSec.getInt("activeGameDurationMinutes"));
            game.getTeamManager().registerTeams(game.getMaxPlayers()/game.getTeamSize());
            String[] lobbyLoc = gameSec.getString("lobby_spawn_location").split(", ");
            game.setLobbyLocation(Double.parseDouble(lobbyLoc[0]),Double.parseDouble(lobbyLoc[1]),Double.parseDouble(lobbyLoc[2]),
                    Float.parseFloat(lobbyLoc[3]),Float.parseFloat(lobbyLoc[4]));
            // game.backupWorld();
            //    importWorld(worldName);
            game.setMapname(mapName);
            try {
                db.insertGame(game);
                if(db != null && db.getConnection() != null) {
                    new DbGameInformationUpdater(game).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            games.add(game);
            arena.loadGame(game);
        }
    }

    public void redirectToLobby(Player player) {
        me.bright.skylib.utils.Messenger.redirect(this,player,serverLobbyName);
    }
    private void loadComponents() {
        getServer().getPluginManager().registerEvents(new GuiListener(),this);
        getServer().getPluginManager().registerEvents(new ActiveGameListener(),this);
        getServer().getPluginManager().registerEvents(new WaitingListener(),this);
        getServer().getPluginManager().registerEvents(new EndGameListener(),this);
        getServer().getPluginManager().registerEvents(new StatesListener(this),this);
        getCommand("forcestart").setExecutor(new ForceStartCommand());
        getCommand("forceend").setExecutor(new ForceEndCommand());
      //  getCommand("bingo").setExecutor(new BingoCommand(this));
        Bukkit.getMessenger().registerOutgoingPluginChannel(this,"BungeeCord");
    }


    public void setupPerms() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        }
        Plugin plugin1 = getServer().getPluginManager().getPlugin("Multiverse-Core");
        MultiverseCore plugin = (MultiverseCore) plugin1;
        if(plugin == null) {
            Bukkit.getLogger().info("MultiVerse core dont load");
        }
        wmanager = new WorldManager(plugin);
    }

    public LuckPerms getLuckpermsApi() {
        return api;
    }

    private void loadConfigs() {
        bingoConfig = new BingoConfig(this.getDataFolder());
    }

    @Override
    public void onDisable() {

    }

    public BingoConfig getBingoConfig() {
        return bingoConfig;
    }

    public List<BingoGame> getGames() {
        return games;
    }

    public MVWorldManager getWorldManager() {
        return wmanager;
    }


}
