package me.bright.mbingo.utils;


import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.bright.mbingo.MBingo;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class Messenger {


    private static String PREFIX = "&bBingo &8|&f ";

 //  private static FileConfiguration msgCfg = Configs.MESSAGES_CFG.getConfig();
 //
 //  public static void log(String msg) {
 //      BossesAPI.getPlugin().getServer().getLogger().info(msg);
 //  }

    public static String color(String message) {
        return (message != null) ? ChatColor.translateAlternateColorCodes('&',message) : null;
    }

    public static void log(String message, JavaPlugin plugin) {
        plugin.getServer().getConsoleSender().sendMessage(Component.text(message));
    }

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }
    public static void sendNoPref(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    public static void sendActionBar(Player player, String message) {
        player.sendActionBar(Component.text(message));
    }

    public static void broadcast(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            send(player,message);
        });
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        player.sendTitle(Messenger.color(title),Messenger.color(subtitle),10,20,10);
    }

    public static int rnd(int min, int max) {
        return new Random().nextInt(max-min+1) + min;
    }
    public static void redirect(JavaPlugin plugin, final Player player, final String server) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }


    public static String correct(int number, String var1, String var2, String var3) {
        String correctString = "ошибка";
        int number1 = number%10;
        int number2 = number%100;
        if(number1 == 1) {
            correctString = var1;
        }
        if(number1 >= 2 && number1 <= 4) {
            correctString = var2;
        }
        if((number1 >= 5) && (number1 <= 9) || (number1 == 0) || (number2 >= 11) && (number2 <= 14)) {
            correctString = var3;
        }
        return correctString;
    }
}
