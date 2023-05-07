package me.bright.mbingo.listeners;

import me.bright.mbingo.BPlayer;
import me.bright.mbingo.MBingo;
import me.bright.mbingo.BingoGame;
import me.bright.mbingo.gui.BingoGui;
import me.bright.mbingo.teams.BTeam;
import me.bright.skylib.SPlayer;
import me.bright.skylib.game.GameState;
import me.bright.skylib.teams.Team;
import me.bright.skylib.utils.ItemBuilder;
import me.bright.skylib.utils.Messenger;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ActiveGameListener implements Listener {


 //   private HashMap<UUID, List<ItemStack>> keepInventoryPlayers;
   // private BingoGame game;

    public ActiveGameListener() {
     //   keepInventoryPlayers = new HashMap<>();
        //this.game = game;
    }



    @EventHandler
    public void onDrop(PlayerDropItemEvent  event) {
        if(!can(event.getPlayer())) return;
        ItemStack item = event.getItemDrop().getItemStack();
        if(item.getType() == Material.PAPER && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if(!can(event.getPlayer())) return;

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {

            if(event.getPlayer().getInventory().getHeldItemSlot() == 8 && event.getMaterial() == Material.PAPER) {
                new BingoGui((BingoGame) SPlayer.getPlayer(event.getPlayer()).getGame(),event.getPlayer()).open(event.getPlayer());
            }

        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if(!can((Player)event.getWhoClicked())) return;

        if(event.getCursor() != null && event.getCursor().getType() == Material.PAPER) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(InventoryMoveItemEvent event) {
       // Messenger.broadcast("moveitemevent");
        if(event.getItem().getType() == Material.PAPER && event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName()) {
            event.setCancelled(true);
        }



    }


    @EventHandler
    public void onPaper(PlayerPickupItemEvent event) {
        if(!can(event.getPlayer())) return;
        BPlayer p = (BPlayer) SPlayer.getPlayer(event.getPlayer());
        String name = ((BTeam) p.getTeam()).getBingo().find(event.getItem().getItemStack());
        BTeam t = (BTeam) p.getTeam();
        if(p.getTeam() != null && name != null) {
            p.addFinded();
            String findedName = Messenger.color("&e&l" + name.replace("§f", ""));
            notifyLocalPlayerOfFind(p.getPlayer(),t,event.getItem().getItemStack(),findedName);
            notifyPlayersOfFind(t,p.getPlayer());
            if(t.getBingo().getFinded() == t.getBingo().getMaxItems()) {
                p.getGame().setWinner(p.getTeam());
                p.getGame().setState(GameState.END);
            }
        }
    }

    private boolean can(Player player) {
        BPlayer bp = (BPlayer) BPlayer.getPlayer(player);
        if(bp.getGame() == null || bp.getGame().getState().getEnum() != GameState.ACTIVEGAME) return false;
        return true;
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        if(event.getOffHandItem() != null && event.getOffHandItem().hasItemMeta() && event.getOffHandItem().getItemMeta().hasDisplayName()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(!can((Player)event.getWhoClicked())) return;
        BPlayer p = (BPlayer)SPlayer.getPlayer((Player)event.getWhoClicked());

        if(event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.PAPER &&
                event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
            event.setCancelled(true);
            return;
        }
        int slot = 8;
        if(event.getClick() == ClickType.NUMBER_KEY) {
            if(event.getHotbarButton() == slot) {
                event.setCancelled(true);
                return;
            }
        }

        if(!event.isCancelled()) {
            BTeam t = (BTeam) p.getTeam();
            String name = t.getBingo().find(event.getCurrentItem());
            if (p.getTeam() != null && name != null) {
                p.addFinded();
                String findedName = Messenger.color("&e&l" + name.replace("§f", ""));
                notifyLocalPlayerOfFind(p.getPlayer(), t, event.getCurrentItem(), findedName);
                notifyPlayersOfFind(t, p.getPlayer());
                if (t.getBingo().getFinded() == t.getBingo().getMaxItems()) {
                    p.getGame().setWinner(p.getTeam());
                    p.getGame().setState(GameState.END);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if((event.getDamager() instanceof Player) && (event.getEntity() instanceof  Player)) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(!can(event.getEntity())) return;

        for(ItemStack drop: event.getDrops()) {
            if(drop.getType() == Material.PAPER) {
                event.getDrops().remove(drop);
                return;
            }
        }
        Player p = event.getEntity();
      //  p.setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if(!can(event.getPlayer())) return;

        Player pl = event.getPlayer();
        pl.getInventory().clear();
        pl.getInventory().setItem(8,new ItemBuilder(Material.PAPER)
                //   .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .setColoredName("&eПредметы")
                .setLore(Arrays.asList("§7Здесь находятся предметы,","§7которые вам нужно собрать"))
                .create());
        BPlayer bp = (BPlayer) BPlayer.getPlayer(pl);
        pl.setInvulnerable(true);
        pl.setGameMode(GameMode.SPECTATOR);
        event.setRespawnLocation(pl.getLocation());
        new BukkitRunnable() {
            int secondsLeft = bp.getRespawn();
            @Override
            public void run() {
                secondsLeft--;
             //   Messenger.broadcast("secondsLeft == " + secondsLeft);
                if(secondsLeft <= 0 || bp.getGame().getState().getEnum() != GameState.ACTIVEGAME) {
                    pl.setInvulnerable(false);
                    pl.setGameMode(GameMode.SURVIVAL);
                    pl.teleport(BPlayer.getPlayer(pl).getGameSpawnLocation());
                    pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                    this.cancel();
                } else {
                    Messenger.sendTitle(pl,"&f" + secondsLeft, "&aвозрождение через");
                }
            }
        }.runTaskTimer(bp.getArena().getPlugin(),0,20L);
    }


  //  private void keepInventory(Player player, int percents) {
  //      List<ItemStack> savingItems = new ArrayList<>();
  //      int inventorySize = player.getInventory().getContents().length;
  //      double saveItems = ((double)inventorySize*percents)/100D;
  //      saveItems = Math.round(saveItems);
  //      Set<Integer> usedNumbers = new HashSet<>();
  //      while(saveItems > 0) {
  //          Integer number = Messenger.rnd(0,inventorySize-1);
  //          if(!usedNumbers.contains(number) && player.getInventory().getContents()[number].getType() != Material.PAPER) {
  //              usedNumbers.add(number);
  //              savingItems.add(player.getInventory().getContents()[number]);
  //              saveItems--;
  //          }
  //      }
  //      keepInventoryPlayers.put(player.getUniqueId(),savingItems);
  //  }

    private void notifyLocalPlayerOfFind(Player finder, Team team, ItemStack item, String findedName) {
        team.getPlayersUUID().forEach(playerUUID -> {
            Player p = Bukkit.getPlayer(playerUUID);
            if(!p.getName().equals(finder.getName())) {
                UserManager uManager = ((MBingo)BPlayer.getPlayer(finder).getArena().getPlugin()).getLuckpermsApi().getUserManager();
                User targetUser =
                        (uManager.isLoaded(finder.getUniqueId()) ? uManager.getUser(finder.getUniqueId())
                                : uManager.loadUser(finder.getUniqueId()).join());
                String prefix = ((targetUser.getCachedData().getMetaData().getPrefix() == null) ? "" : targetUser.getCachedData().getMetaData().getPrefix());
                Messenger.send(p, "&fВаш союзник " + prefix
                        + finder.getName() + " &fнашел предмет " + findedName);
            }
        });
   //     Messenger.broadcast("я в нотифи");
        Messenger.send(finder,"&fВы нашли предмет " + findedName);
    }

    private void notifyPlayersOfFind(Team finder, Player p) {
        BingoGame game = null;
        game = (BingoGame) BPlayer.getPlayer(p).getGame();
        for (Player player:  game.getPlayers()) {
            Messenger.send(player,"&fКоманда &l" + finder.getColor().getColoredName() + " &fнашла предмет");
        }
    }

}
