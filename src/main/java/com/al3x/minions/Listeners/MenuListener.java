package com.al3x.minions.Listeners;

import com.al3x.minions.Instances.ClickableItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class MenuListener implements Listener {

    private static ArrayList<String> menuNames = new ArrayList<>();
    private static ArrayList<ClickableItem> items = new ArrayList<>();

    /**
     * Add a menu name to the list
     * @param name
     */
    public static void addMenuName(String name) {
        for (String n : menuNames) {
            if (n.equals(name)) return;
        }
        menuNames.add(name);
    }

    /**
     * Add a clickable item to the list (duhhh)
     * @param item
     */
    public static void addItem(ClickableItem item) {
        for (ClickableItem i : items) {
            if (i.getItem().equals(item.getItem()) && i.getSlot() == item.getSlot()) return;
        }
        items.add(item);
    }

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (e.getCurrentItem() == null) return;

        for (String name : menuNames) {
            if (e.getView().getTitle().equals(name)) {
                e.setCancelled(true);
                break;
            }
        }

        for (ClickableItem item : items) {
            if (e.getCurrentItem().equals(item.getItem()) && e.getSlot() == item.getSlot()) {
                if (e.isLeftClick()) {
                    if (item.getLeftClick() != null) item.getLeftClick().run();
                } else if (e.isRightClick()) {
                    if (item.getRightClick() != null) item.getRightClick().run();
                }
                return;
            }
        }
    }

}
