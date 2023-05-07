package me.bright.mbingo.placeholders;


import me.bright.mbingo.BPlayer;
import me.bright.mbingo.DatedPlayer;
import me.bright.mbingo.MBingo;
import me.bright.mbingo.database.GameInfoMySQL;
import me.bright.mbingo.database.LDbType;
import me.bright.mbingo.teams.BTeam;
import me.bright.mbingo.utils.Messenger;

import me.bright.skylib.SPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class BingoHolder extends PlaceholderExpansion {

    private MBingo plugin;

    @Override
    public String getIdentifier() {
        return "bingo";
    }

    public BingoHolder(MBingo plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "bright";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("games")) {
            GameInfoMySQL db = plugin.getGameInfoMysql();
            int nowGames = (int) db.get("SELECT * FROM " + db.getTableName() +
                    " WHERE uuid = ?;", LDbType.GAMES.getDbStringName(), player.getUniqueId().toString());
            return String.valueOf(nowGames);
        } else if(params.equalsIgnoreCase("wins")) {
            GameInfoMySQL db = plugin.getGameInfoMysql();
            int nowWins = (int) db.get("SELECT * FROM " + db.getTableName() +
                    " WHERE uuid = ?;", LDbType.WINS.getDbStringName(), player.getUniqueId().toString());
            return String.valueOf(nowWins);
        } else if(params.equalsIgnoreCase("teamcolor")) {
            BPlayer bp = (BPlayer) BPlayer.getPlayer(player.getPlayer());
            if(!bp.hasTeam()) {
                return "&f";
            }
            return "&" + bp.getTeam().getColor().getColorCode();
        } else if(params.equalsIgnoreCase("teamname")) {
            BTeam BTeam =  (BTeam)SPlayer.getPlayer(player.getPlayer()).getTeam();
            if(BTeam == null) {
                return Messenger.color("&f");
            } else {
                return BTeam.getColor().getColoredName();
            }
        }
        return null;
    }


}
