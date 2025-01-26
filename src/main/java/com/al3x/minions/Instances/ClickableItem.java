package com.al3x.minions.Instances;

import org.bukkit.inventory.ItemStack;

public class ClickableItem {

    private String title;
    private ItemStack item;
    private int slot;
    private Runnable leftClick;
    private Runnable rightClick;

    public ClickableItem(String title, ItemStack item, int slot, Runnable leftClick) {
        this.title = title;
        this.item = item;
        this.slot = slot;
        this.leftClick = leftClick;
        this.rightClick = null;
    }

    public ClickableItem(String title, ItemStack item, int slot, Runnable leftClick, Runnable rightClick) {
        this.title = title;
        this.item = item;
        this.slot = slot;
        this.leftClick = leftClick;
        this.rightClick = rightClick;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Runnable getLeftClick() {
        return leftClick;
    }

    public void setLeftClick(Runnable leftClick) {
        this.leftClick = leftClick;
    }

    public Runnable getRightClick() {
        return rightClick;
    }

    public void setRightClick(Runnable rightClick) {
        this.rightClick = rightClick;
    }
}
