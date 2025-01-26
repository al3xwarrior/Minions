package com.al3x.minions.Menus;

import com.al3x.minions.Instances.MinionManager;
import com.al3x.minions.Instances.Minions.Minion;
import com.al3x.minions.Main;
import com.al3x.minions.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MinionsMenu extends Menu {

    private Player player;

    public MinionsMenu(Player player) {
        super(player, "&6Minions Menu", 27);
        this.player = player;
        setupItems();
    }

    private void clickMinionSlot(Minion minion) {
        player.getInventory().addItem(minion.getPhysicalItem());
        Main.getInstance().getMinionManager().removeMinion(player, minion);
        new MinionsMenu(player).open();
    }

    // 11 13 15
    private void setupItems() {
        for (int i = 0; i < 27; i++) {
            if (i == 11 || i == 13 || i == 15) continue;
            setItem(i, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("&7").build());
        }

        MinionManager minionManager = Main.getInstance().getMinionManager();

        Minion[] minions = minionManager.getUserEquippedMinions(player);
        if (minions[0] != null) {
            setItem(11, minions[0].getMenuItem(), () -> clickMinionSlot(minions[0]));
        } else {
            setItem(11, new ItemBuilder(Material.BARRIER).setName("&cEmpty").build());
        }

        if (minions[1] != null) {
            setItem(13, minions[1].getMenuItem(), () -> clickMinionSlot(minions[1]));
        } else {
            setItem(13, new ItemBuilder(Material.BARRIER).setName("&cEmpty").build());
        }

        if (minions[2] != null) {
            setItem(15, minions[2].getMenuItem(), () -> clickMinionSlot(minions[2]));
        } else {
            setItem(15, new ItemBuilder(Material.BARRIER).setName("&cEmpty").build());
        }
    }

}
