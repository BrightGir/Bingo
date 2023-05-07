package me.bright.mbingo.gui;

import me.bright.mbingo.BPlayer;
import me.bright.mbingo.BingoGame;
import me.bright.mbingo.utils.ItemBuilder;
import me.bright.mbingo.utils.Messenger;
import me.bright.skylib.teams.Team;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamGui extends GUI {

    private BingoGame game;

    public TeamGui(BingoGame game, Player holder) {
        super(holder, "Выбор команды", 5);
        this.game = game;
        soloInit();
    }

    public void soloInit() {
        int[] slots = {10,13,16,28,31,34};
        int counter = 0;
        for(Team team: game.getTeamManager().getTeams()) {
            int slot = slots[counter];
            Material mat = Material.CYAN_WOOL;
            switch(team.getColor()) {
                case BLUE:
                    mat = Material.BLUE_WOOL;
                    break;
                case RED:
                    mat = Material.RED_WOOL;
                    break;
                case PINK:
                    mat = Material.PINK_WOOL;
                    break;
                case GREEN:
                    mat = Material.GREEN_WOOL;
                    break;
                case WHITE:
                    mat = Material.WHITE_WOOL;
                    break;
                case YELLOW:
                    mat = Material.YELLOW_WOOL;
                    break;
            }

            ItemBuilder item = new ItemBuilder(mat)
                 //   .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    //.setDurability(team.getColor().getDurability())
                    .setName(team.getColor().getColoredName() +
                            Messenger.color(" &7(&a" + team.getPlayersCount() + "/" + team.getMaxPlayers() + "&7)"));

            if(team.getPlayersCount() > 0) {
                List<String> lore = new ArrayList<>();
                for(String name: team.getPlayers()) {
                    lore.add(Messenger.color("&7 - &" + team.getColor().getColorCode() + name));
                }
                item.setLore(lore);
            }
            this.setItem(item.create(),slot);
            this.setClickActionForItem(slot, event -> {

                if(!team.isFull()){
                    try {
                        //PAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(event.getWhoClicked().getUniqueId());
                        //PlayerParty party = PartyManager.getInstance().getParty(pafPlayer);
                        if (false) { // что-то про пати
                       //  if (team.getPlayersCount() == 0) {
                       //     game.getTeamManager().addPlayer(BPlayer.getPlayer((Player) event.getWhoClicked()), team);
                       //     event.getWhoClicked().closeInventory();
                       //     party.getPlayers().forEach(player -> {

                       //         if (!player.getUniqueId().equals(event.getWhoClicked().getUniqueId()) && !team.isFull()) {
                       //             game.getTeamManager().addPlayer(BPlayer.getPlayer(Bukkit.getPlayer(player.getUniqueId())), team);
                       //         }
                       //     });

                       //    } else {
                       //        game.getTeamManager().addPlayer(BPlayer.getPlayer((Player) event.getWhoClicked()), team);
                       //        event.getWhoClicked().closeInventory();
                       //    }
                       //    return;
                        }
                    } catch (Exception e) {

                    }
                    game.getTeamManager().addPlayer(BPlayer.getPlayer((Player) event.getWhoClicked()), team);
                    Messenger.send((Player)event.getWhoClicked(),"&fВы выбрали команду " + team.getColor().getColoredName());
                    event.getWhoClicked().closeInventory();
                }
            });
            counter++;
        }
    }




}
