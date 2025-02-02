package com.al3x.minions.Menus;

import com.al3x.minions.Instances.ClickableItem;
import com.al3x.minions.Listeners.MenuListener;
import com.al3x.minions.Utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.al3x.minions.Utils.Color.colorize;
import static com.al3x.minions.Utils.Color.decolorize;

public class Menu {
    private Player player;
    private String title;
    private int size;
    private Inventory inv;

    public Menu(Player player, String title, int size) {
        this.player = player;
        this.title = title;
        this.size = size;

        inv = Bukkit.createInventory(player, size, colorize(title));

        MenuListener.addMenuName(title);
    }

    public void setItem(int slot, ItemStack item) {
        inv.setItem(slot, item);
        // TODO: something better than just using null to prevent taking items from menu
        MenuListener.addItem(new ClickableItem(title, item, slot, null, null));
    }
    public void setItem(int slot, ItemStack item, Runnable leftClick) {
        inv.setItem(slot, item);
        MenuListener.addItem(new ClickableItem(title, item, slot, leftClick, null));
    }
    public void setItem(int slot, ItemStack item, Runnable leftClick, Runnable rightClick) {
        inv.setItem(slot, item);
        MenuListener.addItem(new ClickableItem(title, item, slot, leftClick, rightClick));
    }

    public void open() {
        Bukkit.getLogger().info("Opening inventory for " + player.getName());
        player.openInventory(inv);
    }
    public void close() {
        player.closeInventory();
    }
}
