package me.bright.mbingo.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private ItemStack item;
    private ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();

    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder setDurability(int durability) {
        // item.d((short)durability);
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        item.setType(material);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment ench, int level) {
        meta.addEnchant(ench,level,false);
        return this;
    }

    public ItemBuilder setColoredName(String name) {
        meta.displayName(Component.text(Messenger.color(name)));
        return this;
    }

    public ItemBuilder setName(String name) {
        meta.displayName(Component.text(name));
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder addLore(String... strings) {
        List<String> list = new ArrayList<>();
        for (String string: strings) {
            list.add(Messenger.color(string));
        }
        meta.setLore(list);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flags) {
        item.addItemFlags(flags);
        return this;
    }

    public ItemStack create() {
        item.setItemMeta(meta);
        return item;
    }
}
