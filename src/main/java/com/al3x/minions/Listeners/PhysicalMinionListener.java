package com.al3x.minions.Listeners;

import com.al3x.minions.Enums.MinionType;
import com.al3x.minions.Instances.MinionManager;
import com.al3x.minions.Instances.Minions.ArcherMinion;
import com.al3x.minions.Instances.Minions.FighterMinion;
import com.al3x.minions.Instances.Minions.Minion;
import com.al3x.minions.Instances.Minions.WoodcutterMinion;
import com.al3x.minions.Main;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static com.al3x.minions.Utils.Color.colorize;

public class PhysicalMinionListener implements Listener {

    private MinionManager minionManager = Main.getInstance().getMinionManager();

    @EventHandler
    public void onMinionInteract(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        // where we left off was for some reason it doesnt see this nbt data on the item
        // even though we can now confirm it does exist

        MinionType type = MinionType.fromString(NBT.get(item, nbt -> (String) nbt.getString("minionType")));

        if (type != null) {
            e.setCancelled(true);

            Minion[] playerMinions = minionManager.getUserEquippedMinions(player);
            if (playerMinions[0] != null && playerMinions[1] != null && playerMinions[2] != null) {
                player.sendMessage(colorize("&cYou can only have 3 minions equipped at a time!"));
                return;
            }

            player.getInventory().remove(item);

            try {
                Constructor<? extends Minion> constructor = type.getMinionClass().getDeclaredConstructor(Player.class);
                Minion minion = constructor.newInstance(player);
                minionManager.addMinion(player, minion);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException ex) {
                Bukkit.getLogger().warning("Failed to create minion instance for type: " + type);
                new Exception(ex).printStackTrace();
            }


        }

    }

}
