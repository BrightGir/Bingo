package me.bright.mbingo.commands;

import me.bright.mbingo.BPlayer;
import me.bright.mbingo.utils.Messenger;
import me.bright.skylib.game.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceEndCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!cmd.getName().equalsIgnoreCase("forceend")) {
            return true;
        }
        if(!(sender instanceof Player)) {
            return true;
        }
        if(args.length != 0) {
            Messenger.send(sender,"&cИспользование команды: /forceend");
            return true;
        }
        if(!sender.hasPermission("bingo.admin")) {
            return true;
        }
        BPlayer.getPlayer((Player)sender).getGame().setState(GameState.END);
        return true;
    }
}
