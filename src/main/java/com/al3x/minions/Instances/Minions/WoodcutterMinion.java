package com.al3x.minions.Instances.Minions;

import com.al3x.minions.Enums.MinionType;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.al3x.minions.Utils.IsBlockType.isWood;

public class WoodcutterMinion extends Minion {

    private int radius;
    private int miningSpeed;

    public WoodcutterMinion(Player player) {
        super(player, "Woodcutter Minion", MinionType.WOODCUTTER);
        equipNPC();
        this.miningSpeed = 1;
        this.radius = 9;
    }

    public void update() {
        super.updatePersonality();

        World world = getOwner().getWorld();

        Block nearestBlock = null;
        double closestDistance = 99;

        double ownerX = getOwner().getLocation().getX();
        double ownerY = getOwner().getLocation().getY();
        double ownerZ = getOwner().getLocation().getZ();

        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    Block block  = world.getBlockAt(new Location(world, ownerX + x, ownerY + y, ownerZ + z));
                    if (isWood(block)) {
                        double distance = block.getLocation().distance(getNPC().getEntity().getLocation());
                        if (distance < closestDistance) {
                            nearestBlock = block;
                            closestDistance = distance;
                        }
                    }
                }
            }
        }

        if (nearestBlock != null) {
            Block finalNearestBlock = nearestBlock;
            super.setGoal(nearestBlock.getLocation(), () -> {
//                finalNearestBlock.
            });
        } else {
            super.updateLocation();
        }
    }

    private void equipNPC() {
        super.updateEquipment(Equipment.EquipmentSlot.HAND, new ItemStack(Material.IRON_AXE));
        super.updateEquipment(Equipment.EquipmentSlot.HELMET, new ItemStack(Material.LEATHER_HELMET));
        super.updateEquipment(Equipment.EquipmentSlot.CHESTPLATE, new ItemStack(Material.LEATHER_CHESTPLATE));
        super.updateEquipment(Equipment.EquipmentSlot.LEGGINGS, new ItemStack(Material.LEATHER_LEGGINGS));
        super.updateEquipment(Equipment.EquipmentSlot.BOOTS, new ItemStack(Material.LEATHER_BOOTS));
    }

    public int getRadius() {
        return radius;
    }
    public int getMiningSpeed() {
        return miningSpeed;
    }
}
