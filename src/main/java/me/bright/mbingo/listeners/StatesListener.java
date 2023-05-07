package me.bright.mbingo.listeners;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import me.bright.mbingo.BPlayer;
import me.bright.mbingo.MBingo;
import me.bright.mbingo.BingoGame;
import me.bright.mbingo.utils.Messenger;
import me.bright.skylib.events.GameJoinEvent;
import me.bright.skylib.events.GameLeaveEvent;
import me.bright.skylib.game.GameState;
import me.bright.skylib.game.states.State;
import me.bright.skylib.game.states.WaitingState;
import net.kyori.adventure.text.Component;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Collections;
import java.util.List;

import static me.bright.skylib.game.GameState.WAITING;

public class StatesListener implements Listener {

    private MBingo plugin;

    public StatesListener(MBingo plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
      //  Bukkit.getLogger().info("JOIB, create");
        BPlayer.create(event.getPlayer());
        event.getPlayer().getInventory().clear();
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
       // event.getPlayer().teleport(plugin.getMainLobbySpawnLocation());
       // game.getState().addPlayer(event.getPlayer());
      //  BPlayer.getPlayer(event.getPlayer()).setArena(game.getArena());

        BingoGame game = getOptimalGame();
        event.getPlayer().setInvulnerable(true);
        event.getPlayer().setInvisible(false);
        //   plugin.getLogger().info("Game = " + game.get);
        event.getPlayer().setCollidable(true);
        try {
            game.addPlayer(event.getPlayer());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                plugin.redirectToLobby(event.getPlayer());
            } catch (Exception e1) {
                event.getPlayer().kick(Component.text("Лобби недоступно!"));
            }
        }
        if(plugin.getGameInfoMysql().getConnection() != null) {
            //   Bukkit.getLogger().info("INSSEEEEEEEEEEEEEEERT");
            plugin.getGameInfoMysql().insertPlayer(event.getPlayer());
        }
    }

    public BingoGame getOptimalGame(){
        BingoGame optimal = null;
        List<BingoGame> gamesList = plugin.getGames();
        Collections.shuffle(gamesList);

        for(BingoGame game: gamesList) {
            boolean open = game.getState() != null && game.getState().getEnum() == WAITING;
            if(open && (optimal == null || (game.getPlayersSize() > optimal.getPlayersSize()))) {
                optimal = game;
            }
        }
        return optimal;
    }



    @EventHandler
    public void onActive(PlayerAdvancementCriterionGrantEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onArenaJoin(GameJoinEvent event) {
      //  SPlayer.getPlayer(event.getPlayer()).setArena(event.getArena());
      //  Player p = event.getPlayer();
      //  Location to = event.getGame().getArena().getLobbySpawnLocation();
      //  to.setWorld(event.getArena().getLobbyWorld());
      //  event.getPlayer().teleport(to);
        Player p = event.getPlayer();
        p.setGameMode(GameMode.ADVENTURE);
        p.setHealth(20D);
        UserManager uManager = plugin.getLuckpermsApi().getUserManager();
        User tu =
                (uManager.isLoaded(p.getUniqueId()) ? uManager.getUser(p.getUniqueId())
                        : uManager.loadUser(p.getUniqueId()).join());
        for (Player pl: event.getGame().getPlayers()) {
            String prefix = (tu.getCachedData().getMetaData().getPrefix() == null) ? "" : tu.getCachedData().getMetaData().getPrefix();
            Messenger.send(pl, prefix + p.getDisplayName() + " &fподключился к игре (&a"
                    + event.getGame().getPlayersSize() + "/" + event.getGame().getMaxPlayers()+ "&f)");
        }
    }

    @EventHandler
    public void onArenaLeave(GameLeaveEvent event) {
        BingoGame game = (BingoGame) event.getGame();
        State state = game.getState();
        if(game.getPlayersSize() <= 0 && (state != null && state.getEnum() != WAITING)) {
            game.startGame();
            return;
        }
        if(state != null && state.getEnum() == WAITING) {
            game.broadCastColor("&fИгрок &a" + event.getPlayer().getName() + " &fпокинул игру (&a" +
                    game.getPlayersSize() + "/&a" + game.getMaxPlayers() + "&f)",true);
        } else if(state != null && state.getEnum() == GameState.ACTIVEGAME) {
            game.broadCastColor("&fИгрок &a" + event.getPlayer().getName() + " &fпокинул игру",true);
        }
        if(game.getState() != null && game.getState().getEnum() == WAITING
                && game.getPlayersSize() < game.getMinPlayersToStartCounting()) {
            ((WaitingState)game.getState()).stopCounting();
        }

    }



    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        BPlayer bp = (BPlayer) BPlayer.getPlayer(event.getPlayer());
        if(bp.getArena() != null && bp.getGame() != null) {
            bp.getGame().removePlayer(event.getPlayer());
            bp.remove();
        }
    }


}
