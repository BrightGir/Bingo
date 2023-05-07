package me.bright.mbingo.commands;

import me.bright.mbingo.BPlayer;
import me.bright.mbingo.utils.Messenger;
import me.bright.skylib.game.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceStartCommand  implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
   //     Messenger.broadcast("command vizov");
        if(!cmd.getName().equalsIgnoreCase("forcestart")) {
            return true;
        }
        if(!(sender instanceof Player)) {
            Messenger.send(sender,"иди нахуй");
            return true;
        }
        if(args.length != 0) {
            Messenger.send(sender,"&cИспользование команды: /forcestart");
            return true;
        }
        if(!sender.hasPermission("bingo.admin")) {
          //  Messenger.broadcast("no admin");
            return true;
        }
        BPlayer.getPlayer((Player)sender).getGame().setState(GameState.ACTIVEGAME);
       // Messenger.broadcast("forcestart");

        return true;
    }
}
