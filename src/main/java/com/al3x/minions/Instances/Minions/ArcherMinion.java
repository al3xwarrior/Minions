package com.al3x.minions.Instances.Minions;

import com.al3x.minions.Enums.EntityTarget;
import com.al3x.minions.Enums.MinionType;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

import static com.al3x.minions.Utils.MinionUtilities.getClosestEntity;
import static com.al3x.minions.Utils.MinionUtilities.isFriendlyMob;

public class ArcherMinion extends Minion {

    private final int cooldown;
    private int cooldownTimer = 0;

    public ArcherMinion(Player player) {
        super(player, "Archer Minion", MinionType.ARCHER);
        equipNPC();
        this.cooldown = 5;

        setReach(4.5);
        setNeedsToSeeTarget(true);
    }

    public void update() {
        super.updatePersonality();
        Entity closestEntity = getClosestEntity(getOwner(), getRangeXZ(), getRangeY(), EntityTarget.MOBS);
        if (closestEntity == null) {
            super.updateLocation();
            return;
        }
        setGoal(closestEntity.getLocation(), () -> {
            if (cooldownTimer > 0) {
                cooldownTimer--;
                return;
            }
            getNPC().getEntity().getLocation().getWorld().spawnArrow(getNPC().getEntity().getLocation().add(0, 1.5, 0), closestEntity.getLocation().add(0, 1.5, 0).subtract(getNPC().getEntity().getLocation()).toVector(), 1.5f, 1);
            cooldownTimer = cooldown;
        });
    }

    private void equipNPC() {
        super.updateEquipment(Equipment.EquipmentSlot.HAND, new ItemStack(Material.BOW));
        super.updateEquipment(Equipment.EquipmentSlot.HELMET, new ItemStack(Material.LEATHER_HELMET));
        super.updateEquipment(Equipment.EquipmentSlot.CHESTPLATE, new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        super.updateEquipment(Equipment.EquipmentSlot.LEGGINGS, new ItemStack(Material.CHAINMAIL_LEGGINGS));
        super.updateEquipment(Equipment.EquipmentSlot.BOOTS, new ItemStack(Material.LEATHER_BOOTS));
    }

}
