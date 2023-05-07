package me.bright.mbingo.listeners;

import me.bright.mbingo.BPlayer;
import me.bright.mbingo.BingoGame;
import me.bright.mbingo.MBingo;
import me.bright.mbingo.gui.TeamGui;
import me.bright.skylib.SPlayer;
import me.bright.skylib.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class WaitingListener implements Listener {



    public WaitingListener() {

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        //Выбор команды предмет
        if(!can(event.getPlayer())) return;
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {

            if(event.getMaterial() == Material.NETHER_STAR) {
                new TeamGui((BingoGame) BPlayer.getPlayer(event.getPlayer()).getGame(),event.getPlayer()).open(event.getPlayer());
                event.setCancelled(true);
            }
            if(event.getMaterial() == Material.MAGMA_CREAM) {
             //   game.redirectToLobby(event.getPlayer());
           //   World worldTo = Bukkit.getWorld("world");
           //   Location to = worldTo.getSpawnLocation();
           //   to.setWorld(worldTo);
           //   event.getPlayer().teleport(to);
                ((MBingo)BPlayer.getPlayer(event.getPlayer()).getGame().getArena().getPlugin()).redirectToLobby(event.getPlayer());
            }

        }
        event.setCancelled(true);
    }

    private boolean can(Player player) {
        BPlayer bp = (BPlayer) BPlayer.getPlayer(player);
   //   Bukkit.getLogger().info("bp = " + bp.getPlayer().getName());
   //   Bukkit.getLogger().info("bp = " + bp.getPlayer().getName());
        if(bp.getGame() != null && bp.getGame().getState() != null && bp.getGame().getState().getEnum() == GameState.WAITING) {
            return true;
        } else {
            return false;
        }
    }

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if(!can((Player)event.getEntity())) return;
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFalld(PlayerMoveEvent event) {
        SPlayer sp = SPlayer.getPlayer(event.getPlayer());
        if(event.getTo().getY() <= 0) {
            if(sp.getGame() != null && sp.getGame().getState() != null && sp.getGame().getState().getEnum() == GameState.WAITING) {
                event.getPlayer().teleport(sp.getGame().getLobbyLocation());
                return;
            }
            new EntityDamageEvent(event.getPlayer(), EntityDamageEvent.DamageCause.VOID,10000D);
        }
    }


    @EventHandler
    public void onBread(BlockBreakEvent event) {
        if(!can(event.getPlayer())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPick(PlayerPickupItemEvent event) {
        if(!can(event.getPlayer())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(!can((Player)event.getWhoClicked())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void foodChange(FoodLevelChangeEvent event) {
        if(!can((Player)(event.getEntity()))) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(!can(event.getPlayer())) return;
        event.setCancelled(true);
    }
}
