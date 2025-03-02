package com.al3x.minions.Instances.Minions;

import com.al3x.minions.Enums.EntityTarget;
import com.al3x.minions.Enums.MinionType;
import com.al3x.minions.Utils.ItemBuilder;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.al3x.minions.Utils.MinionUtilities.*;

public class FighterMinion extends Minion {

    private int damage;

    public FighterMinion(Player player) {
        super(player, "Fighter Minion", MinionType.FIGHTER);
        equipNPC();
        this.damage = 1;
    }

    public void update() {
        super.updatePersonality();
        Entity closestEntity = getClosestEntity(getOwner(), getRangeXZ(), getRangeY(), EntityTarget.ALL);
        if (closestEntity == null) {
            super.updateLocation();
            return;
        }
        setGoal(closestEntity.getLocation(), () -> ((LivingEntity) closestEntity).damage(damage));
    }

    private void equipNPC() {
        super.updateEquipment(Equipment.EquipmentSlot.HAND, new ItemStack(Material.IRON_SWORD));
        super.updateEquipment(Equipment.EquipmentSlot.HELMET, new ItemStack(Material.LEATHER_HELMET));
        super.updateEquipment(Equipment.EquipmentSlot.CHESTPLATE, new ItemStack(Material.LEATHER_CHESTPLATE));
        super.updateEquipment(Equipment.EquipmentSlot.LEGGINGS, new ItemStack(Material.LEATHER_LEGGINGS));
        super.updateEquipment(Equipment.EquipmentSlot.BOOTS, new ItemStack(Material.LEATHER_BOOTS));
    }

    public ItemStack getMenuItem() {
        return new ItemBuilder(Material.NETHER_STAR).setName("&6Fighter Minion").setLore("&7Level: &e" + getLevel()).build();
    }

    public int getDamage() {
        return damage;
    }

}
