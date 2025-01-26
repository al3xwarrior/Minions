package com.al3x.minions.Listeners;

import com.al3x.minions.Enums.MinionType;
import com.al3x.minions.Instances.MinionManager;
import com.al3x.minions.Instances.Minions.ArcherMinion;
import com.al3x.minions.Instances.Minions.FighterMinion;
import com.al3x.minions.Instances.Minions.Minion;
import com.al3x.minions.Instances.Minions.WoodcutterMinion;
import com.al3x.minions.Main;
import com.al3x.minions.Utils.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static com.al3x.minions.Utils.Color.colorize;

public class PhysicalMinionListener implements Listener {

    private MinionManager minionManager = Main.getInstance().getMinionManager();

    @EventHandler
    public void onMinionInteract(PlayerInteractEvent e) {
        Bukkit.getLogger().info("onMinionInteract");
        if (e.getItem() == null) return;
        Bukkit.getLogger().info("onMinionInteract 2");
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Bukkit.getLogger().info("onMinionInteract 3");

        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        // where we left off was for some reason it doesnt see this nbt data on the item
        // even though we can now confirm it does exist
        if (NBTEditor.contains(item, "minionUUID") && NBTEditor.contains(item, "minionType") && NBTEditor.contains(item, "minionLevel")) {
            Bukkit.getLogger().info("onMinionInteract 4");
            e.setCancelled(true);

            Minion[] playerMinions = minionManager.getUserEquippedMinions(player);
            if (playerMinions.length >= 3) {
                player.sendMessage(colorize("&cYou can only have 3 minions equipped at a time!"));
                return;
            }

            MinionType type;
            try {
                type = MinionType.valueOf(NBTEditor.getString(item, "minionType"));
            } catch (IllegalArgumentException ex) {
                player.sendMessage(colorize("&cInvalid minion type! Contact an admin!"));
                return;
            }

            player.getInventory().remove(item);

            // TODO: better way to do this?
            switch (type) {
                case FIGHTER:
                    minionManager.addMinion(player, new FighterMinion(player));
                    break;
                case WOODCUTTER:
                    minionManager.addMinion(player, new WoodcutterMinion(player));
                    break;
                case ARCHER:
                    minionManager.addMinion(player, new ArcherMinion(player));
                    break;
            }


        }

    }

}
