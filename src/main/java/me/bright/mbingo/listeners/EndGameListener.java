package me.bright.mbingo.listeners;

import me.bright.mbingo.BPlayer;
import me.bright.mbingo.BingoGame;
import me.bright.mbingo.gui.BingoGui;
import me.bright.skylib.game.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EndGameListener implements Listener {

   // private BingoGame game;

    public EndGameListener() {

    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(!can(event.getPlayer())) return;
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {

            if(event.getPlayer().getInventory().getHeldItemSlot() == 2 && event.getMaterial() == Material.PAPER) {
                new BingoGui((BingoGame)(BPlayer.getPlayer(event.getPlayer()).getGame()),event.getPlayer());
                event.setCancelled(true);
            }

        }
    }

    private boolean can(Player player) {
        BPlayer bp = (BPlayer) BPlayer.getPlayer(player);
        if(bp.getGame() == null || bp.getGame().getState().getEnum() != GameState.END) return false;
        return true;
    }

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if(!can((Player)event.getEntity())) return;
            event.setCancelled(true);
        }
    }
}
