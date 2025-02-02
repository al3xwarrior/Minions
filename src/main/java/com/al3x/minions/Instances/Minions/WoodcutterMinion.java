package com.al3x.minions.Instances.Minions;

import com.al3x.minions.Enums.BlockTarget;
import com.al3x.minions.Enums.MinionType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import static com.al3x.minions.Utils.MinionUtilities.getClosestBlock;

public class WoodcutterMinion extends Minion {
    public WoodcutterMinion(Player player) {
        super(player, "Woodcutter Minion", MinionType.WOODCUTTER);
        equipNPC();
    }

    public void update() {
        super.updatePersonality();
        Block closestBlock = getClosestBlock(getOwner(), getRangeXZ(), getRangeY(), BlockTarget.WOOD);
        if (closestBlock == null) {
            super.updateLocation();
            return;
        }
        // TODO: Implement block break speed
        setGoal(closestBlock.getLocation(), () -> closestBlock.breakNaturally());
    }

    private void equipNPC() {
        /*
        super.updateEquipment(Equipment.EquipmentSlot.HAND, new ItemStack(Material.IRON_AXE));
        super.updateEquipment(Equipment.EquipmentSlot.HELMET, new ItemStack(Material.LEATHER_HELMET));
        super.updateEquipment(Equipment.EquipmentSlot.CHESTPLATE, new ItemStack(Material.LEATHER_CHESTPLATE));
        super.updateEquipment(Equipment.EquipmentSlot.LEGGINGS, new ItemStack(Material.LEATHER_LEGGINGS));
        super.updateEquipment(Equipment.EquipmentSlot.BOOTS, new ItemStack(Material.LEATHER_BOOTS));

         */
    }
}
