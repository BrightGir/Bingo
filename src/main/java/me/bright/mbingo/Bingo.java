package me.bright.mbingo;

import me.bright.mbingo.utils.ItemBuilder;
import me.bright.skylib.game.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Bingo {
    private static List<ItemStack> finalItems;
    private Map<Integer,ItemStack> items;
    int finded;

    public Bingo() {
        items = new HashMap<>();
        finded = 0;
        pull();
    }

    public int getFinded() {
        return finded;
    }

    private void pull() {
        int c = 1;
        for (ItemStack mat: finalItems) {
            items.put(c,mat);
            c++;
        }
    }

    public ItemStack get(int index) {
        return items.get(index);
    }

    public String find(ItemStack findItem) {
        for(int c = 1; c <= 9; c++) {
            if(findItem != null && items.get(c) != null && !items.get(c).getType().toString().contains("STAINED_GLASS_PANE") &&
                    items.get(c).getType() == findItem.getType()) {

                boolean can = (isTool(items.get(c))) ? true : items.get(c).getDurability() == findItem.getDurability();

                if(can) {
                    String displayName = items.get(c).getItemMeta().getDisplayName();
                    items.put(c, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                            .setColoredName("&a&lПредмет найден")
                            .create());
                    finded++;
                    // Messenger.broadcast("NAME == " + items.get(c).getItemMeta().getDisplayName());
                    return displayName;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private boolean isTool(ItemStack item) {
        String type = item.getType().toString();
        if(type.contains("PICKAXE") || type.contains("LEGGINGS") || type.contains("BOOTS") || type.contains("SWORD")
        || type.contains("AXE") || type.contains("SPADE") || type.contains("CHESTPLATE") || type.contains("HELMET") || type.contains("HOE")) {
            return true;
        }
        return false;

    }

    public int getMaxItems() {
        return 9;
    }

    public static void generate(BingoGame game) {
        FileConfiguration config = ((MBingo)game.getArena().getPlugin()).getBingoConfig().getConfig();
        List<String> easy = config.getStringList("bingo.easy");
        List<String> medium = config.getStringList("bingo.medium");
        List<String> hard = config.getStringList("bingo.hard");
        boolean solo = game.getMode() == GameMode.SOLO;
        int easyCount = (solo) ? 3 : 2;
        int mediumCount = (solo) ? 4 : 3;
        int hardCount = (solo) ? 2 : 4;
    //    int[] idxs = {12,13,14,21,22,23,30,31,32};

        List<ItemStack> finalEasy = getRandomList(easyCount,easy);
        List<ItemStack> finalMedium = getRandomList(mediumCount,medium);
        List<ItemStack> finalHard = getRandomList(hardCount,hard);
        List<ItemStack> items = finalEasy;
        addToList(items,finalMedium);
        addToList(items,finalHard);
        finalItems = items;
    }

    private static void addToList(List<ItemStack> old, List<ItemStack> list) {
        for (ItemStack mat: list) {
            old.add(mat);
        }
    }

    private static List<ItemStack> getRandomList(int count, List<String> list) {
        List<ItemStack> finalList = new ArrayList<>();
        while(count != 0) {
            count--;
            int idx = rnd(0,list.size()-1);
            String matString = list.get(idx);
            int durability = 0;
            if(matString.split(":").length > 1) {
                durability = Integer.parseInt(matString.split(";")[0].split(":")[1]);
            }
            finalList.add(new ItemBuilder(Material.valueOf(matString.split(";")[0].split(":")[0]))
               //     .setDurability(durability)
                            .setColoredName("&f" + matString.split(";")[1])
                    .create());
            list.remove(idx);
        }
        return finalList;
    }

    private static int rnd(int min, int max) {
        return new Random().nextInt(max-min+1) + min;
    }

}
