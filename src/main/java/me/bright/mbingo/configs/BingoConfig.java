package me.bright.mbingo.configs;

import me.bright.mbingo.games.GameMode;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BingoConfig extends BConfig {

    public BingoConfig(File dataFolder) {
        super(dataFolder, "bingo");
    }

    @Override
    protected void values() {
        List<String> easy = new ArrayList<>();
        List<String> medium = new ArrayList<>();
        List<String> hard = new ArrayList<>();
        easy.add(Material.LEATHER_HELMET.toString() + ";Кожаный шлем");
        easy.add(Material.LEATHER_CHESTPLATE.toString() + ";Кожаный нагрудник");
        easy.add(Material.LEATHER_BOOTS.toString() + ";Кожаные ботинки");
        easy.add(Material.LEATHER_LEGGINGS.toString() + ";Кожаные штаны");
        medium.add(Material.IRON_HELMET.toString() + ";Алмазный шлем");
        medium.add(Material.IRON_CHESTPLATE.toString() + ";Алмазный нагрудник");
        medium.add(Material.IRON_BOOTS.toString() + ";Алмазные ботинки");
        medium.add(Material.IRON_LEGGINGS.toString() + ";Алмазные штаны");
        hard.add(Material.DIAMOND_HELMET.toString() + ";Алмазный шлем");
        hard.add(Material.DIAMOND_CHESTPLATE.toString() + ";Алмазный нагрудник");
        hard.add(Material.DIAMOND_BOOTS.toString() + ";Алмазные ботинки");
        hard.add(Material.DIAMOND_LEGGINGS.toString() + ";Алмазные штаны");
        dataConfig("bingo.easy",easy);
        dataConfig("bingo.medium",medium);
        dataConfig("bingo.hard",hard);
        List<String> seeds = new ArrayList<>();
        seeds.add("xui");
        seeds.add("pizda");
        dataConfig("seeds",seeds);
        dataConfig("mainlobby.x",0);
        dataConfig("mainlobby.y",60);
        dataConfig("mainlobby.x",100);
        dataConfig("mainlobby.yaw",0);
        dataConfig("mainlobby.pitch",0);
    }
}
