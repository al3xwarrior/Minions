package com.al3x.minions.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.al3x.minions.Utils.Color.colorize;

public class ItemBuilder {

    private ItemStack item;
    private ItemMeta meta;
    private HashMap<String, Object> nbtData;

    public ItemBuilder(Material material) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
        nbtData = new HashMap<>();
    }

    public ItemBuilder setName(String name) {
        meta.setDisplayName(colorize(name));
        return this;
    }

    public ItemBuilder setLore(String lore) {
        lore = colorize(lore);
        String[] loreArray = lore.split("\n");
        meta.setLore(List.of(loreArray));
        return this;
    }

    public ItemBuilder setNBTInt(String key, int value) {
        nbtData.put(key, value);
        return this;
    }
    public ItemBuilder setNBTString(String key, String value) {
        nbtData.put(key, value);
        return this;
    }
    public ItemBuilder setNBTBoolean(String key, boolean value) {
        nbtData.put(key, value);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        NbtItemBuilder nbtItemBuilder = new NbtItemBuilder(item);
        for (String key : nbtData.keySet()) {
            Object value = nbtData.get(key);
            if (value instanceof Integer) {
                nbtItemBuilder.setInt(key, (int) value);
            } else if (value instanceof String) {
                nbtItemBuilder.setString(key, (String) value);
            } else if (value instanceof Boolean) {
                nbtItemBuilder.setBoolean(key, (boolean) value);
            }
        }
        nbtItemBuilder.build();
        return item;
    }

}
